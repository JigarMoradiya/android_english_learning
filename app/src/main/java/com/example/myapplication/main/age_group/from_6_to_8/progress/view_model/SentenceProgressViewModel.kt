package com.example.myapplication.main.age_group.from_6_to_8.progress.view_model

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.generation.loader.SentenceProgress
import com.example.myapplication.data.generation.loader.SentenceProgressSummary
import com.example.myapplication.data.progress.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Age 6-8 progress-over-time screen (item 5.3).
@HiltViewModel
class SentenceProgressViewModel @Inject constructor(
    sessionRepository: SessionRepository
) : ViewModel() {
    val summary: SentenceProgressSummary =
        SentenceProgress.summary(sessionRepository.allSessions())
}
