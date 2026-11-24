package com.example.myapplication.main.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.myapplication.R

/** What a page does. INTRO = read-only hero; the rest are "learn by doing" mini-games. */
enum class OnbKind { INTRO, TRACE, DRAG, CHOICE }

/** Which floating decoration drifts in the background of a page. */
enum class OnboardingDeco { STARS, BUBBLES, SPARKLES }

/**
 * One onboarding page. Interactive pages ([OnbKind.TRACE]/[DRAG]/[CHOICE]) keep the Next button
 * locked until the child completes the action, mirroring the Abacus "learn by doing" tutorial.
 *
 * CHOICE is a tap-the-right-answer page: either a picture prompt ([promptEmoji], Word Adventure)
 * or a fill-the-blank sentence ([promptText] with a "___" blank, Grammar Challenge), with
 * [choiceOptions] to pick from and [choiceAnswer] the correct one.
 *
 * Pure tutorial — deliberately NO subscription / paywall page here: a proactive purchase offer
 * to a child on first launch violates the Families Ad Format policy (the v1.0.4 rejection).
 */
data class OnboardingPage(
    val kind: OnbKind,
    val stepLabel: String,
    val icon: String = "",             // decorative emoji badge shown above the title
    val title: String,
    val subtitle: String,
    val hint: String = "",             // coach line shown when the child is idle on a play page
    @DrawableRes val image: Int = 0,   // INTRO hero
    val heroFramed: Boolean = true,    // INTRO: photo in a circle (true) or a cut-out character standing free (false)
    val points: List<String> = emptyList(), // INTRO: feature chips on the "what's inside" page
    val letter: String = "A",          // TRACE / DRAG target
    val dragOptions: List<String> = emptyList(), // DRAG tiles, includes the target letter
    val promptEmoji: String = "",      // CHOICE picture prompt (Word Adventure)
    val promptText: String = "",       // CHOICE sentence prompt with a "___" blank (Grammar)
    val choiceOptions: List<String> = emptyList(), // CHOICE answers to pick from
    val choiceAnswer: String = "",     // CHOICE correct answer
    val topColor: Color,
    val bottomColor: Color,
    val accent: Color,
    val accentDark: Color,
    val deco: OnboardingDeco
)

/** The first-launch tutorial, in order: welcome → trace → drag → word → grammar → go. */
val onboardingPages: List<OnboardingPage> = listOf(
    OnboardingPage(
        kind = OnbKind.INTRO,
        stepLabel = "Hi!",
        icon = "👋",
        title = "Welcome to Vedaavi English!",
        subtitle = "Let's learn and play together! 🎉",
        image = R.drawable.owl,
        topColor = Color(0xFFEDE9FF),
        bottomColor = Color(0xFFD3C4FF),
        accent = Color(0xFF6C5CE7),
        accentDark = Color(0xFF4B37C9),
        deco = OnboardingDeco.STARS
    ),
    OnboardingPage(
        kind = OnbKind.TRACE,
        stepLabel = "Trace",
        icon = "✏️",
        title = "Trace It!",
        subtitle = "Follow the arrows to trace A ✏️",
        hint = "Trace from the green dot!",
        letter = "A",
        topColor = Color(0xFFE3ECFF),
        bottomColor = Color(0xFFC2D4FF),
        accent = Color(0xFF4C6FFF),
        accentDark = Color(0xFF2F4FD6),
        deco = OnboardingDeco.SPARKLES
    ),
    OnboardingPage(
        kind = OnbKind.DRAG,
        stepLabel = "Drag",
        icon = "🔤",
        title = "Drag & Drop!",
        subtitle = "Drag the letter B into its box ✋",
        hint = "Drag B into the box!",
        letter = "B",
        dragOptions = listOf("D", "B", "R"),
        topColor = Color(0xFFE6F8F1),
        bottomColor = Color(0xFFBDEAD6),
        accent = Color(0xFF12B886),
        accentDark = Color(0xFF0C8F69),
        deco = OnboardingDeco.BUBBLES
    ),
    OnboardingPage(
        kind = OnbKind.CHOICE,
        stepLabel = "Word",
        icon = "🔤",
        title = "Word Adventure!",
        subtitle = "Tap the missing letter 🔤",
        hint = "Tap the missing letter!",
        promptText = "🐱   c ___ t",
        choiceOptions = listOf("a", "o", "u"),
        choiceAnswer = "a",
        topColor = Color(0xFFFFE9F0),
        bottomColor = Color(0xFFFFC9DC),
        accent = Color(0xFFFF4D8D),
        accentDark = Color(0xFFD6336C),
        deco = OnboardingDeco.BUBBLES
    ),
    OnboardingPage(
        kind = OnbKind.CHOICE,
        stepLabel = "Grammar",
        icon = "✍️",
        title = "Grammar Challenge!",
        subtitle = "Pick the word that finishes it ✍️",
        hint = "Tap the word that fits!",
        promptText = "The cat ___ a hat.",
        choiceOptions = listOf("has", "have"),
        choiceAnswer = "has",
        topColor = Color(0xFFE8EAFF),
        bottomColor = Color(0xFFCBD0FF),
        accent = Color(0xFF5C6BC0),
        accentDark = Color(0xFF3F51B5),
        deco = OnboardingDeco.SPARKLES
    ),
    OnboardingPage(
        kind = OnbKind.INTRO,
        stepLabel = "Go!",
        icon = "🚀",
        title = "You're All Set! 🌟",
        subtitle = "Practice every day and grow! 🔥",
        image = R.drawable._mascot_,
        heroFramed = false,
        points = listOf(
            "🔤 Sounds",
            "✏️ Tracing",
            "🔡 Letters",
            "🧩 Phonics",
            "📖 Reading",
            "🧱 Sentences",
            "✍️ Grammar",
            "🎮 Games",
            "⭐ Streaks"
        ),
        topColor = Color(0xFFFFF2E0),
        bottomColor = Color(0xFFFFD9B0),
        accent = Color(0xFFFB8C00),
        accentDark = Color(0xFFE36A00),
        deco = OnboardingDeco.SPARKLES
    )
)
