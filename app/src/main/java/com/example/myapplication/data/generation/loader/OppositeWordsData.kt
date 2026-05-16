package com.example.myapplication.data.generation.loader

data class OppositeWordPair(val word: String, val opposite: String)

enum class OppositeDifficulty { EASY, MEDIUM, HARD }

object OppositeWordsData {

    val easyPairs = listOf(
        OppositeWordPair("Big", "Small"), OppositeWordPair("Hot", "Cold"),
        OppositeWordPair("Happy", "Sad"), OppositeWordPair("Fast", "Slow"),
        OppositeWordPair("Up", "Down"), OppositeWordPair("Tall", "Short"),
        OppositeWordPair("Open", "Close"), OppositeWordPair("Day", "Night"),
        OppositeWordPair("Full", "Empty"), OppositeWordPair("Clean", "Dirty"),
        OppositeWordPair("Heavy", "Light"), OppositeWordPair("Young", "Old"),
        OppositeWordPair("Near", "Far"), OppositeWordPair("Come", "Go"),
        OppositeWordPair("In", "Out"), OppositeWordPair("Left", "Right"),
        OppositeWordPair("Sweet", "Sour"), OppositeWordPair("Bright", "Dark"),
        OppositeWordPair("Start", "Stop"), OppositeWordPair("Push", "Pull"),
        OppositeWordPair("High", "Low"), OppositeWordPair("Wet", "Dry"),
        OppositeWordPair("Hard", "Soft"), OppositeWordPair("Early", "Late"),
        OppositeWordPair("Front", "Back"), OppositeWordPair("Good", "Bad"),
        OppositeWordPair("Black", "White"), OppositeWordPair("On", "Off"),
        OppositeWordPair("Over", "Under"), OppositeWordPair("First", "Last"),
        OppositeWordPair("Quiet", "Noisy"), OppositeWordPair("Love", "Hate"),
        OppositeWordPair("Rich", "Poor"), OppositeWordPair("Short", "Long"),
        OppositeWordPair("Wake", "Sleep"), OppositeWordPair("Boy", "Girl"),
    )

    val mediumPairs = listOf(
        OppositeWordPair("Accept", "Reject"), OppositeWordPair("Agree", "Disagree"),
        OppositeWordPair("Appear", "Disappear"), OppositeWordPair("Arrive", "Leave"),
        OppositeWordPair("Build", "Destroy"), OppositeWordPair("Deep", "Shallow"),
        OppositeWordPair("Expand", "Shrink"), OppositeWordPair("Fail", "Pass"),
        OppositeWordPair("Freeze", "Melt"), OppositeWordPair("Hide", "Show"),
        OppositeWordPair("Increase", "Decrease"), OppositeWordPair("Join", "Separate"),
        OppositeWordPair("Lead", "Follow"), OppositeWordPair("Lose", "Find"),
        OppositeWordPair("Maximum", "Minimum"), OppositeWordPair("Messy", "Neat"),
        OppositeWordPair("Polite", "Rude"), OppositeWordPair("Possible", "Impossible"),
        OppositeWordPair("Present", "Absent"), OppositeWordPair("Private", "Public"),
        OppositeWordPair("Remember", "Forget"), OppositeWordPair("Save", "Spend"),
        OppositeWordPair("Send", "Receive"), OppositeWordPair("Sharp", "Blunt"),
        OppositeWordPair("Simple", "Complicated"), OppositeWordPair("Smooth", "Rough"),
        OppositeWordPair("Success", "Failure"), OppositeWordPair("Truth", "Lie"),
        OppositeWordPair("Wide", "Narrow"), OppositeWordPair("Win", "Lose"),
        OppositeWordPair("Brave", "Cowardly"), OppositeWordPair("Calm", "Excited"),
        OppositeWordPair("Cheap", "Expensive"), OppositeWordPair("Honest", "Dishonest"),
        OppositeWordPair("Natural", "Artificial"), OppositeWordPair("Major", "Minor"),
    )

    val hardPairs = listOf(
        OppositeWordPair("Abundant", "Scarce"), OppositeWordPair("Admire", "Despise"),
        OppositeWordPair("Advance", "Retreat"), OppositeWordPair("Approve", "Disapprove"),
        OppositeWordPair("Attract", "Repel"), OppositeWordPair("Benefit", "Harm"),
        OppositeWordPair("Capture", "Release"), OppositeWordPair("Chaos", "Order"),
        OppositeWordPair("Conceal", "Reveal"), OppositeWordPair("Construct", "Demolish"),
        OppositeWordPair("Courage", "Fear"), OppositeWordPair("Defend", "Attack"),
        OppositeWordPair("Despair", "Hope"), OppositeWordPair("Flexible", "Rigid"),
        OppositeWordPair("Generous", "Stingy"), OppositeWordPair("Genuine", "Fake"),
        OppositeWordPair("Grateful", "Ungrateful"), OppositeWordPair("Harmony", "Conflict"),
        OppositeWordPair("Humble", "Proud"), OppositeWordPair("Inferior", "Superior"),
        OppositeWordPair("Justice", "Injustice"), OppositeWordPair("Knowledge", "Ignorance"),
        OppositeWordPair("Liberty", "Captivity"), OppositeWordPair("Mercy", "Cruelty"),
        OppositeWordPair("Neglect", "Care"), OppositeWordPair("Permanent", "Temporary"),
        OppositeWordPair("Permit", "Forbid"), OppositeWordPair("Prosperity", "Poverty"),
        OppositeWordPair("Reality", "Fantasy"), OppositeWordPair("Respect", "Disrespect"),
        OppositeWordPair("Rural", "Urban"), OppositeWordPair("Strength", "Weakness"),
        OppositeWordPair("Transparent", "Opaque"), OppositeWordPair("Violence", "Peace"),
        OppositeWordPair("Wisdom", "Foolishness"), OppositeWordPair("Bravery", "Cowardice"),
    )

    fun getPairsForDifficulty(difficulty: OppositeDifficulty): List<OppositeWordPair> = when (difficulty) {
        OppositeDifficulty.EASY   -> easyPairs
        OppositeDifficulty.MEDIUM -> mediumPairs
        OppositeDifficulty.HARD   -> hardPairs
    }
}
