package com.example.myapplication.main.base.force_update

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class ForceUpdateViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ForceUpdateUiState>(ForceUpdateUiState.Idle)
    val uiState: StateFlow<ForceUpdateUiState> = _uiState.asStateFlow()

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS
                }
            )
            setDefaultsAsync(mapOf(KEY_ANDROID_VERSION to 0L))
        }
    }

    fun dismiss() {
        _uiState.value = ForceUpdateUiState.UpToDate
    }

    fun checkForUpdate() {
        viewModelScope.launch {
            runCatching {
                fetchAndActivate()
                val requiredVersion = remoteConfig.getLong(KEY_ANDROID_VERSION).toInt()
                val installedVersion = currentVersionCode()
                Log.d(TAG, "installed=$installedVersion required=$requiredVersion")

                _uiState.value = if (requiredVersion > installedVersion) {
                    ForceUpdateUiState.UpdateRequired(
                        installedVersion = installedVersion,
                        requiredVersion = requiredVersion
                    )
                } else {
                    ForceUpdateUiState.UpToDate
                }
            }.onFailure { error ->
                Log.w(TAG, "remote config fetch failed", error)
                _uiState.value = ForceUpdateUiState.UpToDate
            }
        }
    }

    private suspend fun fetchAndActivate(): Boolean = suspendCancellableCoroutine { cont ->
        remoteConfig.fetchAndActivate()
            .addOnSuccessListener { activated -> if (cont.isActive) cont.resume(activated) }
            .addOnFailureListener { e -> if (cont.isActive) cont.resumeWithException(e) }
    }

    @Suppress("DEPRECATION")
    private fun currentVersionCode(): Int {
        val context = getApplication<Application>()
        return runCatching {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                info.longVersionCode.toInt()
            } else {
                info.versionCode
            }
        }.getOrDefault(0)
    }

    companion object {
        private const val TAG = "ForceUpdate"
        private const val KEY_ANDROID_VERSION = "android_version"
        private const val MIN_FETCH_INTERVAL_SECONDS = 3600L
    }
}
