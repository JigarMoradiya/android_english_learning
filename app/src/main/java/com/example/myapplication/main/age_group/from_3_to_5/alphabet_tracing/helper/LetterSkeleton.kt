package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.helper

import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode

object LetterSkeleton {

    const val startY = 0.1f
    const val endY = 0.9f

    private const val topWeight = 1f
    private const val middleWeight = 1f
    private const val bottomWeight = 1f

    private fun totalWeight(mode: LetterMode): Float {
        return if (mode == LetterMode.UPPERCASE) {
            topWeight + middleWeight
        } else {
            topWeight + middleWeight + bottomWeight
        }
    }

    fun line1(mode: LetterMode) = startY

    fun line2(mode: LetterMode): Float {
        return if (mode == LetterMode.UPPERCASE) {
            (startY + endY) / 2f
        } else {
            startY + (topWeight / totalWeight(mode)) * (endY - startY)
        }
    }

    fun line3(mode: LetterMode): Float {
        return if (mode == LetterMode.UPPERCASE) {
            endY
        } else {
            line2(mode) + (middleWeight / totalWeight(mode)) * (endY - startY)
        }
    }

    fun line4(mode: LetterMode) = endY

    fun center12(mode: LetterMode) = (line1(mode) + line2(mode)) / 2f
    fun center23(mode: LetterMode) = (line2(mode) + line3(mode)) / 2f
}