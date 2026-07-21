package com.example.myapplication.main.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.utils.extensions.scaled
import java.io.File

/**
 * "🎤 Say it!" chip for learn pages: tap → record, tap again → hear yourself.
 * No scoring — the kid just compares their voice with the model word.
 * The temp file is overwritten on every take and never leaves the device.
 */
@Composable
fun PhonicsSayItButton(
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    val recorderHolder = remember { arrayOfNulls<MediaRecorder>(1) }
    val file = remember { File(context.cacheDir, "phonics_say_it.m4a") }

    fun startRecording() {
        val recorder = if (Build.VERSION.SDK_INT >= 31) MediaRecorder(context) else @Suppress("DEPRECATION") MediaRecorder()
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            recorder.setOutputFile(file.absolutePath)
            recorder.prepare()
            recorder.start()
            recorderHolder[0] = recorder
            isRecording = true
        } catch (_: Exception) {
            recorder.release()
        }
    }

    fun stopAndPlayBack() {
        try { recorderHolder[0]?.stop() } catch (_: Exception) {}
        recorderHolder[0]?.release()
        recorderHolder[0] = null
        isRecording = false
        if (file.exists()) {
            MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnCompletionListener { it.release() }
                prepare()
                start()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) startRecording() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens6),
        modifier = modifier
            .background(
                if (isRecording) Color(0xFFC62828) else accentColor.copy(alpha = 0.12f),
                CircleShape
            )
            .border(1.5.dp, (if (isRecording) Color(0xFFC62828) else accentColor).copy(alpha = 0.4f), CircleShape)
            .clickable {
                if (isRecording) {
                    stopAndPlayBack()
                } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    startRecording()
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        Text(
            text = if (isRecording) "⏹" else "🎤",
            style = MaterialTheme.typography.labelLarge.scaled()
        )
        Text(
            text = if (isRecording) "Stop & hear yourself!" else "Say it — then hear yourself!",
            style = MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.Bold,
            color = if (isRecording) Color.White else accentColor
        )
    }
}
