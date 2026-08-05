package com.example.myapplication.data.progress

import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.age_group.phonics.listen.view_model.PhonicsListenLevelKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Human-readable level names for parent-report rows — "L4 CVC Words" etc.
 * Keep identical to iOS PhonicsLevelTitles.
 */
object PhonicsLevelTitles {

    private val titles = mapOf(
        PhonicsListenLevelKey.letterSounds to "L1 Letter Sounds",
        PhonicsListenLevelKey.shortVowels to "L2 Short Vowels",
        PhonicsListenLevelKey.blending to "L3 2-Sound Blending",
        PhonicsListenLevelKey.cvcWords to "L4 CVC Words",
        // a MILESTONE, not a level — the parent report must not call it "L5"
        PhonicsListenLevelKey.firstSentences to "Read Your First Sentences",
        PhonicsListenLevelKey.shortVowelRules to "L5 Spelling Rules",
        PhonicsListenLevelKey.wordFamilies to "L6 Word Families",
        PhonicsListenLevelKey.beginningBlends to "L7 Beginning Blends",
        PhonicsListenLevelKey.endingBlends to "L8 Ending Blends",
        PhonicsListenLevelKey.digraphs to "L9 Digraphs",
        PhonicsListenLevelKey.specialEndings to "L10 Special Endings",
        PhonicsListenLevelKey.openSyllable to "L11 Open Syllable",
        PhonicsListenLevelKey.magicE to "L12 Magic E",
        PhonicsListenLevelKey.vowelTeams to "L13 Vowel Teams",
        PhonicsListenLevelKey.diphthongs to "L14 Diphthongs",
        PhonicsListenLevelKey.rControlled to "L15 R-Controlled Vowels",
        PhonicsListenLevelKey.ighGh to "L16 igh & gh Patterns",
        PhonicsListenLevelKey.yAsVowel to "L17 Y as a Vowel",
        PhonicsListenLevelKey.threeLetterBlends to "L18 3-Letter Blends",
        PhonicsListenLevelKey.softCSoftG to "L19 Soft C & Soft G",
        PhonicsListenLevelKey.silentLetters to "L20 Silent Letters",
        PhonicsListenLevelKey.wordEndings to "L21 Word Endings",
        PhonicsListenLevelKey.prefixes to "L22 Prefixes",
        PhonicsListenLevelKey.suffixes to "L23 Suffixes",
        PhonicsListenLevelKey.contractions to "L24 Contractions",
        PhonicsListenLevelKey.consonantLe to "L25 Consonant + -le",
        PhonicsListenLevelKey.compoundWords to "L26 Compound Words",
        PhonicsListenLevelKey.syllableDivision to "L27 Syllable Division",
        PhonicsListenLevelKey.sightWords to "L28 Sight Words",
    )

    fun title(level: PhonicsListenLevelKey): String = titles[level] ?: level.name
}

/**
 * Records phonics journey activity as LearningSessions so the parent report
 * (Phonics tab) can show results and screen time. All phonics sessions share
 * moduleId "phonics_reading" and ageGroup PHONICS; the mode travels in subConfig
 * (LEARN / LISTEN / PRACTICE) and the level in lessonTitle.
 */
@Singleton
class PhonicsSessionRecorder @Inject constructor(
    private val sessions: SessionRepository
) {

    /** Scored practice run — score, wrong/correct words, and time on screen. */
    fun recordPractice(
        level: PhonicsListenLevelKey,
        score: Int,
        total: Int,
        durationSeconds: Int,
        wrongItems: List<String>,
        correctItems: List<String>,
    ) {
        sessions.record(
            LearningSession(
                moduleId = ModuleID.PHONICS_READING,
                ageGroup = AgeGroup.PHONICS,
                durationSeconds = durationSeconds,
                score = score,
                totalQuestions = total,
                wrongItems = wrongItems,
                correctItems = correctItems,
                subConfig = "PRACTICE",
                lessonTitle = PhonicsLevelTitles.title(level),
                chapterTitle = PhonicsLevelTitles.title(level),
            )
        )
    }

    /** Scored practice run for bonus content with a custom title ("Compare: ai vs ay"). */
    fun recordPractice(
        title: String,
        score: Int,
        total: Int,
        durationSeconds: Int,
        wrongItems: List<String>,
        correctItems: List<String>,
    ) {
        sessions.record(
            LearningSession(
                moduleId = ModuleID.PHONICS_READING,
                ageGroup = AgeGroup.PHONICS,
                durationSeconds = durationSeconds,
                score = score,
                totalQuestions = total,
                wrongItems = wrongItems,
                correctItems = correctItems,
                subConfig = "PRACTICE",
                lessonTitle = title,
                chapterTitle = title,
            )
        )
    }

    /** Learning-only time (Learn or Listen screens). Skips accidental opens (<3s). */
    fun recordLearning(level: PhonicsListenLevelKey, mode: String, durationSeconds: Int) {
        recordLearning(PhonicsLevelTitles.title(level), mode, durationSeconds)
    }

    /** Learning-only time with a custom title ("Compare: ai vs ay"). */
    fun recordLearning(title: String, mode: String, durationSeconds: Int) {
        if (durationSeconds < 3) return
        sessions.record(
            LearningSession(
                moduleId = ModuleID.PHONICS_READING,
                ageGroup = AgeGroup.PHONICS,
                durationSeconds = durationSeconds,
                score = 0,
                totalQuestions = 0,
                subConfig = mode,
                lessonTitle = title,
                chapterTitle = title,
            )
        )
    }
}
