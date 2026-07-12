package com.example.myapplication.main.age_group.from_3_to_5.phonics_reading

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.progress.PhonicsLevelProgressRepository
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhonicsJourneyViewModel @Inject constructor(
    val levelProgress: PhonicsLevelProgressRepository,
    private val prefs: AppPreferencesHelper
) : ViewModel() {

    companion object {
        private const val KEY_SEEN_DONE = "phonics_journey_seen_done_count"
    }

    // Intro (road draw-in + node pop-in) plays once per screen lifetime — returning
    // from a level must refresh progress/counts silently (matches iOS hasAppeared).
    var introPlayed = false

    // Last fraction the progress ring showed, so on return it animates from there
    // to the new value instead of replaying from zero.
    var lastRingFraction = 0f

    fun markVisited(level: PhonicsListenLevelKey) = levelProgress.markVisited(level)

    /** True once when a new level got completed since the last visit to the journey map. */
    fun consumeCelebration(): Boolean {
        val done = levelProgress.doneCount
        val seen = prefs.getCustomParamInt(KEY_SEEN_DONE, 0)
        prefs.setCustomParamInt(KEY_SEEN_DONE, done)
        return done > seen
    }
}
