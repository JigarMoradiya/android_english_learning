package com.example.myapplication.data.generation.loader

data class ListenQuestion(
    val answer: String,
    val options: List<String>,
    /** What TTS speaks: the bare word, or a meaning-giving sentence for homophones */
    val spokenText: String,
    val isHomophone: Boolean,
    /** Shown in feedback for homophone questions */
    val hintSentence: String? = null,
)

/**
 * Builds Listen & Select questions that train the ear:
 * - word questions prefer sound-alike distractors (B/P, F/V, sh/ch …)
 * - homophone questions speak a sentence and ask which word was heard
 */
object ListenQuestionFactory {

    // Words that sound almost alike — used as distractors instead of random words
    private val confusableDistractors: Map<String, List<String>> = mapOf(
        "Bat" to listOf("Pat", "Mat", "Bad"),
        "Pig" to listOf("Big", "Dig", "Bag"),
        "Bear" to listOf("Pear", "Deer", "Hare"),
        "Goat" to listOf("Coat", "Boat", "Note"),
        "Boat" to listOf("Goat", "Coat", "Bowl"),
        "Van" to listOf("Fan", "Pan", "Man"),
        "Fan" to listOf("Van", "Pan", "Man"),
        "Cat" to listOf("Cap", "Bat", "Cut"),
        "Cap" to listOf("Cab", "Cat", "Gap"),
        "Ship" to listOf("Sheep", "Chip", "Shop"),
        "Sheep" to listOf("Ship", "Sheet", "Jeep"),
        "Bag" to listOf("Bat", "Back", "Big"),
        "Pen" to listOf("Pin", "Hen", "Ten"),
        "Hen" to listOf("Pen", "Ten", "Hand"),
        "Bell" to listOf("Ball", "Well", "Bill"),
        "Ball" to listOf("Bell", "Bowl", "Wall"),
        "Tree" to listOf("Three", "Free", "Tea"),
        "Bus" to listOf("Bug", "Boss", "Bush"),
        "Dog" to listOf("Dot", "Fog", "Duck"),
        "Duck" to listOf("Dog", "Truck", "Dark"),
        "Mouse" to listOf("Mouth", "House", "Moose"),
        "Fox" to listOf("Box", "Fog", "Socks"),
        "Bin" to listOf("Pin", "Bun", "Been"),
        "Pear" to listOf("Bear", "Hair", "Pair"),
    )

    // Same sound, different word — the sentence tells you which one it is
    private val homophones: List<Triple<String, String, String>> = listOf(
        Triple("Bear", "Bare", "I saw a big bear at the zoo."),
        Triple("Sea", "See", "Fish swim in the sea."),
        Triple("See", "Sea", "I can see the moon at night."),
        Triple("Week", "Weak", "There are seven days in a week."),
        Triple("Weak", "Week", "The baby bird was too weak to fly."),
        Triple("Know", "No", "I know all my ABC letters."),
        Triple("Sun", "Son", "The sun is hot and bright."),
        Triple("Ate", "Eight", "I ate all my breakfast."),
        Triple("Eight", "Ate", "Eight comes after seven."),
        Triple("Flower", "Flour", "The red flower smells sweet."),
        Triple("Write", "Right", "I write with my pencil."),
        Triple("Hear", "Here", "I can hear the birds singing."),
        Triple("Won", "One", "We won the football game!"),
        Triple("Two", "Too", "I have two little hands."),
        Triple("Four", "For", "Four ducks swam in the pond."),
        Triple("Bare", "Bear", "The baby walked with bare feet."),
        Triple("Son", "Sun", "The king had one little son."),
        Triple("No", "Know", "No, the milk is not cold."),
        Triple("Right", "Write", "Raise your right hand."),
        Triple("Here", "Hear", "Come here, please!"),
        Triple("One", "Won", "I have one nose and two eyes."),
        Triple("Too", "Two", "I want to come too!"),
        Triple("For", "Four", "This gift is for you."),
        Triple("Flour", "Flower", "We bake bread with flour."),
        Triple("Pear", "Pair", "A pear is a sweet green fruit."),
        Triple("Pair", "Pear", "I got a new pair of shoes."),
        Triple("Tail", "Tale", "The happy dog wags its tail."),
        Triple("Tale", "Tail", "Grandma told us a fairy tale."),
        Triple("Meat", "Meet", "Lions like to eat meat."),
        Triple("Meet", "Meat", "I will meet my friend at the park."),
        Triple("Night", "Knight", "The stars come out at night."),
        Triple("Knight", "Night", "The brave knight rode a white horse."),
        Triple("Blue", "Blew", "The sky is big and blue."),
        Triple("Blew", "Blue", "The wind blew my hat away."),
        Triple("Hair", "Hare", "I brush my hair every morning."),
        Triple("Hare", "Hair", "A hare hops fast like a rabbit."),
        Triple("Deer", "Dear", "A deer lives in the green forest."),
        Triple("Buy", "By", "We buy apples at the shop."),
        Triple("By", "Buy", "The school is by my house."),
        Triple("Bee", "Be", "A bee makes sweet honey."),
        Triple("Be", "Bee", "Always be kind to your friends."),
        Triple("Hi", "High", "Hi! How are you today?"),
        Triple("High", "Hi", "The kite flew high in the sky."),
        Triple("Eye", "I", "Cover one eye and look."),
        Triple("Knot", "Not", "I tied a knot in the rope."),
        Triple("Not", "Knot", "The soup is not hot yet."),
        Triple("Knew", "New", "I knew the answer at school!"),
        Triple("New", "Knew", "I have new red shoes."),
        Triple("Nose", "Knows", "I smell with my nose."),
        Triple("Knows", "Nose", "She knows all the colours."),
        Triple("Plane", "Plain", "The plane flew over the clouds."),
        Triple("Road", "Rode", "Cars drive on the road."),
        Triple("Rode", "Road", "I rode my bicycle to school."),
        Triple("Sail", "Sale", "The boat has a big white sail."),
        Triple("Sale", "Sail", "Toys were on sale at the shop."),
        Triple("Wood", "Would", "The table is made of wood."),
        Triple("Would", "Wood", "Would you like some juice?"),
        Triple("Hole", "Whole", "The mouse hid in a small hole."),
        Triple("Whole", "Hole", "I ate the whole apple."),
        Triple("Berry", "Bury", "A berry is a small sweet fruit."),
        Triple("Bury", "Berry", "Dogs bury their bones in the garden."),
        Triple("Hour", "Our", "One hour is sixty minutes."),
        Triple("Our", "Hour", "This is our happy home."),
        Triple("Which", "Witch", "Which ice cream do you want?"),
        Triple("Witch", "Which", "The witch has a magic broom."),
        Triple("There", "Their", "The park is over there."),
        Triple("Their", "There", "The kids put on their shoes."),
        Triple("Piece", "Peace", "I ate a piece of cake."),
        Triple("Board", "Bored", "The teacher writes on the board."),
        Triple("Bored", "Board", "I was bored on the rainy day."),
        Triple("Made", "Maid", "We made a big sandcastle."),
        Triple("Toe", "Tow", "I hurt my little toe."),
        Triple("Wait", "Weight", "Please wait for your turn."),
    )

    fun wordQuestion(word: String, pool: List<String>): ListenQuestion {
        val options = mutableListOf(word)
        confusableDistractors[word]?.let { options += it.shuffled().take(3) }
        val filler = pool.filter { it != word && it !in options }.shuffled()
        for (f in filler) {
            if (options.size >= 4) break
            options += f
        }
        return ListenQuestion(
            answer = word,
            options = options.take(4).shuffled(),
            spokenText = word,
            isHomophone = false
        )
    }

    fun homophoneQuestion(): ListenQuestion {
        val (answer, other, sentence) = homophones.random()
        return ListenQuestion(
            answer = answer,
            options = listOf(answer, other).shuffled(),
            spokenText = sentence,
            isHomophone = true,
            hintSentence = sentence
        )
    }

    fun buildBatch(pool: List<String>, total: Int = 5): List<ListenQuestion> {
        val homophoneCount = 2.coerceAtMost(total)
        val wordCount = total - homophoneCount

        // Prefer at least one ear-training word per batch when the pool has one
        val confusableInPool = pool.filter { confusableDistractors.containsKey(it) }.shuffled()
        val plainWords = pool.filter { it !in confusableDistractors.keys }.shuffled()
        val wordTargets = (confusableInPool.take(1) + (confusableInPool.drop(1) + plainWords))
            .distinct()
            .take(wordCount)

        val homophoneQs = mutableListOf<ListenQuestion>()
        while (homophoneQs.size < homophoneCount) {
            val q = homophoneQuestion()
            if (homophoneQs.none { it.answer == q.answer }) homophoneQs += q
        }

        return (wordTargets.map { wordQuestion(it, pool) } + homophoneQs).shuffled()
    }
}
