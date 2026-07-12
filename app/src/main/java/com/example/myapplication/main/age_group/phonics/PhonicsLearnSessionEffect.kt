package com.example.myapplication.main.age_group.phonics

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.progress.PhonicsSessionRecorder
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Times a phonics Learn screen and records it as a learning-only LearningSession
 * for the parent report when the screen is left. The ViewModel lives exactly as
 * long as the screen's nav entry, so onCleared == screen closed.
 */
@HiltViewModel
class PhonicsLearnSessionViewModel @Inject constructor(
    private val recorder: PhonicsSessionRecorder
) : ViewModel() {

    private val startMs = System.currentTimeMillis()
    var level: PhonicsListenLevelKey? = null

    override fun onCleared() {
        val seconds = ((System.currentTimeMillis() - startMs) / 1000).toInt()
        level?.let { recorder.recordLearning(it, "LEARN", seconds) }
        super.onCleared()
    }
}

/** Drop into any phonics Learn page: `PhonicsLearnSessionEffect(PhonicsListenLevelKey.xxx)`. */
@Composable
fun PhonicsLearnSessionEffect(
    level: PhonicsListenLevelKey,
    viewModel: PhonicsLearnSessionViewModel = hiltViewModel(),
) {
    viewModel.level = level
}
