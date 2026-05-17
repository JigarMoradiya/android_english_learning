package com.example.myapplication.data.access

import com.example.myapplication.data.access.AccessLevel.*

/**
 * Central registry of every module's access rules.
 *
 * To change what is free or premium, edit ONLY this file.
 * No other file needs to know about access levels.
 *
 * Daily limits:
 *   guestDailyLimit  = attempts allowed per day without login
 *   loginDailyLimit  = attempts allowed per day when logged in (free user)
 */
object AccessConfig {

    val modules: Map<String, ModuleAccess> = mapOf(

        // ── Age 3–5: Little Explorers ─────────────────────────────────

        ModuleID.ABCD_WITH_IMAGES to ModuleAccess(
            moduleId = ModuleID.ABCD_WITH_IMAGES,
            accessLevel = FREE
        ),
        ModuleID.LETTER_RECOGNITION to ModuleAccess(
            moduleId = ModuleID.LETTER_RECOGNITION,
            accessLevel = FREE
        ),
        ModuleID.ALPHABET_TRACING to ModuleAccess(
            moduleId = ModuleID.ALPHABET_TRACING,
            accessLevel = FREE                          // Entry is free; letter gating handled inside the screen
        ),
        ModuleID.ALPHABET_TRACING_NZ to ModuleAccess(
            moduleId = ModuleID.ALPHABET_TRACING_NZ,
            accessLevel = LOGIN_REQUIRED                // Letters N–Z require login
        ),
        ModuleID.COLORING_ALPHABETS to ModuleAccess(
            moduleId = ModuleID.COLORING_ALPHABETS,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.MATCH_UPPER_LOWER to ModuleAccess(
            moduleId = ModuleID.MATCH_UPPER_LOWER,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.MATCH_LETTER_WITH_IMAGE to ModuleAccess(
            moduleId = ModuleID.MATCH_LETTER_WITH_IMAGE,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.FILL_THE_BLANK_LETTER to ModuleAccess(
            moduleId = ModuleID.FILL_THE_BLANK_LETTER,
            accessLevel = PREMIUM
        ),
        ModuleID.ARRANGE_LETTER_SEQUENCE to ModuleAccess(
            moduleId = ModuleID.ARRANGE_LETTER_SEQUENCE,
            accessLevel = PREMIUM
        ),
        ModuleID.MISSING_LETTER to ModuleAccess(
            moduleId = ModuleID.MISSING_LETTER,
            accessLevel = PREMIUM
        ),
        ModuleID.DRAG_DROP_LETTERS to ModuleAccess(
            moduleId = ModuleID.DRAG_DROP_LETTERS,
            accessLevel = PREMIUM
        ),

        // ── Age 5–7: Word Adventure ───────────────────────────────────

        ModuleID.VOCABULARY_ANIMALS to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_ANIMALS,
            accessLevel = FREE
        ),
        ModuleID.VOCABULARY_FRUITS to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_FRUITS,
            accessLevel = LOGIN_REQUIRED
        ),
        ModuleID.VOCABULARY_BIRDS to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_BIRDS,
            accessLevel = PREMIUM
        ),
        ModuleID.VOCABULARY_VEGETABLES to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_VEGETABLES,
            accessLevel = PREMIUM
        ),
        ModuleID.VOCABULARY_COLORS to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_COLORS,
            accessLevel = PREMIUM
        ),
        ModuleID.VOCABULARY_SHAPES to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_SHAPES,
            accessLevel = PREMIUM
        ),
        ModuleID.VOCABULARY_VEHICLES to ModuleAccess(
            moduleId = ModuleID.VOCABULARY_VEHICLES,
            accessLevel = PREMIUM
        ),
        ModuleID.ARTICLES_A_AN to ModuleAccess(
            moduleId = ModuleID.ARTICLES_A_AN,
            accessLevel = FREE
        ),
        ModuleID.SIGHT_WORDS to ModuleAccess(
            moduleId = ModuleID.SIGHT_WORDS,
            accessLevel = FREE
        ),
        ModuleID.OPPOSITES_WORD to ModuleAccess(
            moduleId = ModuleID.OPPOSITES_WORD,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.MATCH_WORD_WITH_PICTURE to ModuleAccess(
            moduleId = ModuleID.MATCH_WORD_WITH_PICTURE,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.ARTICLES_CHOICE to ModuleAccess(
            moduleId = ModuleID.ARTICLES_CHOICE,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.SIGHT_WORD_CHOICE to ModuleAccess(
            moduleId = ModuleID.SIGHT_WORD_CHOICE,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.MISSING_LETTER_57 to ModuleAccess(
            moduleId = ModuleID.MISSING_LETTER_57,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.SINGULAR_PLURAL to ModuleAccess(
            moduleId = ModuleID.SINGULAR_PLURAL,
            accessLevel = PREMIUM
        ),
        ModuleID.COLORING_WORDS to ModuleAccess(
            moduleId = ModuleID.COLORING_WORDS,
            accessLevel = PREMIUM
        ),
        ModuleID.LISTEN_AND_SELECT to ModuleAccess(
            moduleId = ModuleID.LISTEN_AND_SELECT,
            accessLevel = PREMIUM
        ),
        ModuleID.WORD_JIGSAW to ModuleAccess(
            moduleId = ModuleID.WORD_JIGSAW,
            accessLevel = PREMIUM
        ),

        // ── Age 6–8: Sentence Builder ─────────────────────────────────

        ModuleID.READ_LISTEN_UNIT1 to ModuleAccess(
            moduleId = ModuleID.READ_LISTEN_UNIT1,
            accessLevel = FREE
        ),
        ModuleID.READ_LISTEN_ALL to ModuleAccess(
            moduleId = ModuleID.READ_LISTEN_ALL,
            accessLevel = PREMIUM
        ),
        ModuleID.GRAMMAR_NOUNS to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_NOUNS,
            accessLevel = FREE
        ),
        ModuleID.GRAMMAR_VERBS to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_VERBS,
            accessLevel = PREMIUM
        ),
        ModuleID.GRAMMAR_ADJECTIVES to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_ADJECTIVES,
            accessLevel = PREMIUM
        ),
        ModuleID.GRAMMAR_PRONOUNS to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_PRONOUNS,
            accessLevel = PREMIUM
        ),
        ModuleID.ONE_WORD_ANSWER to ModuleAccess(
            moduleId = ModuleID.ONE_WORD_ANSWER,
            accessLevel = FREE                  // Free for all users
        ),
        ModuleID.SENTENCE_CHECK to ModuleAccess(
            moduleId = ModuleID.SENTENCE_CHECK,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.FILL_MISSING_WORD to ModuleAccess(
            moduleId = ModuleID.FILL_MISSING_WORD,
            accessLevel = FREE                  // Free for all users
        ),
        ModuleID.CHOOSE_RIGHT_SENTENCE to ModuleAccess(
            moduleId = ModuleID.CHOOSE_RIGHT_SENTENCE,
            accessLevel = FREE   // parent page is free; inner pages gate themselves
        ),
        ModuleID.MATCH_THE_PICTURE to ModuleAccess(
            moduleId = ModuleID.MATCH_THE_PICTURE,
            accessLevel = PREMIUM
        ),
        ModuleID.WHICH_SENTENCE_RIGHT to ModuleAccess(
            moduleId = ModuleID.WHICH_SENTENCE_RIGHT,
            accessLevel = PREMIUM
        ),
        ModuleID.FIND_CORRECT_WRITING to ModuleAccess(
            moduleId = ModuleID.FIND_CORRECT_WRITING,
            accessLevel = PREMIUM
        ),
        ModuleID.SENTENCE_BUILDER to ModuleAccess(
            moduleId = ModuleID.SENTENCE_BUILDER,
            accessLevel = PREMIUM
        ),
        ModuleID.GRAMMAR_CHALLENGE_BEGINNER to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_CHALLENGE_BEGINNER,
            accessLevel = FREE_LIMITED,
            guestDailyLimit = 3,
            loginDailyLimit = 5
        ),
        ModuleID.GRAMMAR_CHALLENGE_MEDIUM to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_CHALLENGE_MEDIUM,
            accessLevel = PREMIUM
        ),
        ModuleID.GRAMMAR_CHALLENGE_ADVANCED to ModuleAccess(
            moduleId = ModuleID.GRAMMAR_CHALLENGE_ADVANCED,
            accessLevel = PREMIUM
        ),
        ModuleID.FILL_THE_BLANKS to ModuleAccess(
            moduleId = ModuleID.FILL_THE_BLANKS,
            accessLevel = PREMIUM
        )
    )

    /** Convenience — returns ModuleAccess for a given id, or null if not registered. */
    fun get(moduleId: String): ModuleAccess? = modules[moduleId]
}
