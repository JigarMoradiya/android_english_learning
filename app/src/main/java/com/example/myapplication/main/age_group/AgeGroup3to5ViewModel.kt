package com.example.myapplication.main.age_group

import androidx.lifecycle.ViewModel
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AgeGroup3to5ViewModel @Inject constructor(
    private val prefs: AppPreferencesHelper
) : ViewModel() {

    companion object {
        private const val KEY_ARRANGE_MODE = "arrange_letter_mode"
    }

    fun getArrangeMode(): LetterMode {
        val saved = prefs.getCustomParam(KEY_ARRANGE_MODE, LetterMode.UPPERCASE.name)
        return LetterMode.valueOf(saved)
    }

    fun saveArrangeMode(mode: LetterMode) {
        prefs.setCustomParam(KEY_ARRANGE_MODE, mode.name)
    }
}
