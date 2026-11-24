package com.example.myapplication.data.notifications

/**
 * Copy for the once-a-day learning reminder (posted by [StreakReminderWorker]).
 *
 * The daily nudge rotates across different activity types so it stays fresh instead of
 * repeating the same line, and encourages the child to try something each day. It stays
 * **one notification per day** — variety comes from rotating the message, not from firing
 * more often (over-notifying a kids app hurts retention and draws policy scrutiny).
 *
 * When there's a live streak the message leads with protecting it (stronger pull), and
 * still points at a concrete activity. With no streak it's a plain, friendly invite.
 */
object ReminderMessages {

    /** A rotating activity to mention — kept to general learning, not premium-gated modules. */
    private data class Activity(val phrase: String, val emoji: String)

    private val activities = listOf(
        Activity("today's phonics sounds", "🔤"),   // 🔤
        Activity("some letter practice", "✏️"),      // ✏️
        Activity("a new reading story", "📖"),       // 📖
        Activity("building new words", "🧩"),        // 🧩
        Activity("rhyming fun", "🎶"),               // 🎶
        Activity("spelling practice", "🔡"),         // 🔡
        Activity("a quick learning game", "🎯")      // 🎯
    )

    /**
     * Builds the (title, body) for today's reminder.
     *
     * @param currentStreak the child's active streak (0 = none)
     * @param dayOfYear used to rotate the activity deterministically, so it varies day to day
     */
    fun forToday(currentStreak: Int, dayOfYear: Int): Pair<String, String> {
        val activity = activities[((dayOfYear % activities.size) + activities.size) % activities.size]
        return if (currentStreak > 0) {
            "🔥 Keep your $currentStreak-day streak!" to
                "Try ${activity.phrase} today to keep it going. ${activity.emoji}"
        } else {
            "Time to learn! ${activity.emoji}" to
                "Come practice ${activity.phrase} — just a few minutes today."
        }
    }
}
