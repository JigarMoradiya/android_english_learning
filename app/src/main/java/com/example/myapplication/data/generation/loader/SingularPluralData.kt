package com.example.myapplication.data.generation.loader

data class SingularPluralPair(
    val singular: String,
    val plural: String,
)

val singularPluralWords = listOf(
    SingularPluralPair("Ant", "Ants"),
    SingularPluralPair("Apple", "Apples"),
    SingularPluralPair("Axe", "Axes"),
    SingularPluralPair("Arrow", "Arrows"),
    SingularPluralPair("Bat", "Bats"),
    SingularPluralPair("Bee", "Bees"),
    SingularPluralPair("Ball", "Balls"),
    SingularPluralPair("Balloon", "Balloons"),
    SingularPluralPair("Banana", "Bananas"),
    SingularPluralPair("Cat", "Cats"),
    SingularPluralPair("Car", "Cars"),
    SingularPluralPair("Cake", "Cakes"),
    SingularPluralPair("Candle", "Candles"),
    SingularPluralPair("Carrot", "Carrots"),
    SingularPluralPair("Cow", "Cows"),
    SingularPluralPair("Dog", "Dogs"),
    SingularPluralPair("Doll", "Dolls"),
    SingularPluralPair("Door", "Doors"),
    SingularPluralPair("Duck", "Ducks"),
    SingularPluralPair("Drum", "Drums"),
    SingularPluralPair("Egg", "Eggs"),
    SingularPluralPair("Eagle", "Eagles"),
    SingularPluralPair("Elephant", "Elephants"),
    SingularPluralPair("Fan", "Fans"),
    SingularPluralPair("Fish", "Fish"),
    SingularPluralPair("Flower", "Flowers"),
    SingularPluralPair("Fox", "Foxes"),
    SingularPluralPair("Frog", "Frogs"),
    SingularPluralPair("Goat", "Goats"),
    SingularPluralPair("Giraffe", "Giraffes"),
    SingularPluralPair("Gift", "Gifts"),
    SingularPluralPair("Guitar", "Guitars"),
    SingularPluralPair("Hat", "Hats"),
    SingularPluralPair("Hand", "Hands"),
    SingularPluralPair("Hammer", "Hammers"),
    SingularPluralPair("Hen", "Hens"),
    SingularPluralPair("Horse", "Horses"),
    SingularPluralPair("House", "Houses"),
    SingularPluralPair("Jar", "Jars"),
    SingularPluralPair("Jug", "Jugs"),
    SingularPluralPair("Kangaroo", "Kangaroos"),
    SingularPluralPair("Key", "Keys"),
    SingularPluralPair("King", "Kings"),
    SingularPluralPair("Kite", "Kites"),
    SingularPluralPair("Knife", "Knives"),
    SingularPluralPair("Kiwi", "Kiwis"),
    SingularPluralPair("Lamp", "Lamps"),
    SingularPluralPair("Leaf", "Leaves"),
    SingularPluralPair("Ladder", "Ladders"),
    SingularPluralPair("Lion", "Lions"),
    SingularPluralPair("Lock", "Locks"),
    SingularPluralPair("Mango", "Mangoes"),
    SingularPluralPair("Monkey", "Monkeys"),
    SingularPluralPair("Moon", "Moons"),
    SingularPluralPair("Mouse", "Mice"),
    SingularPluralPair("Mushroom", "Mushrooms"),
    SingularPluralPair("Nest", "Nests"),
    SingularPluralPair("Net", "Nets"),
    SingularPluralPair("Octopus", "Octopuses"),
    SingularPluralPair("Onion", "Onions"),
    SingularPluralPair("Orange", "Oranges"),
    SingularPluralPair("Owl", "Owls"),
    SingularPluralPair("Parrot", "Parrots"),
    SingularPluralPair("Pen", "Pens"),
    SingularPluralPair("Penguin", "Penguins"),
    SingularPluralPair("Pig", "Pigs"),
    SingularPluralPair("Pot", "Pots"),
    SingularPluralPair("Pumpkin", "Pumpkins"),
    SingularPluralPair("Rabbit", "Rabbits"),
    SingularPluralPair("Rocket", "Rockets"),
    SingularPluralPair("Rope", "Ropes"),
    SingularPluralPair("Rose", "Roses"),
    SingularPluralPair("Snake", "Snakes"),
    SingularPluralPair("Spoon", "Spoons"),
    SingularPluralPair("Star", "Stars"),
    SingularPluralPair("Tiger", "Tigers"),
    SingularPluralPair("Tomato", "Tomatoes"),
    SingularPluralPair("Train", "Trains"),
    SingularPluralPair("Tree", "Trees"),
    SingularPluralPair("Umbrella", "Umbrellas"),
    SingularPluralPair("Unicorn", "Unicorns"),
    SingularPluralPair("Van", "Vans"),
    SingularPluralPair("Vase", "Vases"),
    SingularPluralPair("Volcano", "Volcanoes"),
    SingularPluralPair("Wall", "Walls"),
    SingularPluralPair("Watch", "Watches"),
    SingularPluralPair("Wheel", "Wheels"),
    SingularPluralPair("Window", "Windows"),
    SingularPluralPair("Wolf", "Wolves"),
    SingularPluralPair("Yak", "Yaks"),
    SingularPluralPair("Zebra", "Zebras")
)
// Irregular plurals — the "tricky" set (practised in the match game; word-only, no images)
val singularPluralIrregularWords = listOf(
    SingularPluralPair("Child", "Children"),
    SingularPluralPair("Man", "Men"),
    SingularPluralPair("Woman", "Women"),
    SingularPluralPair("Tooth", "Teeth"),
    SingularPluralPair("Foot", "Feet"),
    SingularPluralPair("Person", "People"),
    SingularPluralPair("Goose", "Geese"),
)

// "Spot the wrong plural" — one fake plural hidden among real ones
data class WrongPluralEntry(
    val singular: String,
    val correctPlural: String,
    val wrongPlural: String,
    val ruleHint: String,
)

val wrongPluralEntries = listOf(
    WrongPluralEntry("Leaf", "Leaves", "Leafs", "Leaf ends in f → change f to ves: Leaves"),
    WrongPluralEntry("Knife", "Knives", "Knifes", "Knife ends in fe → change fe to ves: Knives"),
    WrongPluralEntry("Wolf", "Wolves", "Wolfs", "Wolf ends in f → change f to ves: Wolves"),
    WrongPluralEntry("Mouse", "Mice", "Mouses", "Mouse is special → Mice"),
    WrongPluralEntry("Child", "Children", "Childs", "Child is special → Children"),
    WrongPluralEntry("Man", "Men", "Mans", "Man is special → Men"),
    WrongPluralEntry("Woman", "Women", "Womans", "Woman is special → Women"),
    WrongPluralEntry("Tooth", "Teeth", "Tooths", "Tooth is special → Teeth"),
    WrongPluralEntry("Foot", "Feet", "Foots", "Foot is special → Feet"),
    WrongPluralEntry("Goose", "Geese", "Gooses", "Goose is special → Geese"),
    WrongPluralEntry("Box", "Boxes", "Boxs", "Box ends in x → add es: Boxes"),
    WrongPluralEntry("Watch", "Watches", "Watchs", "Watch ends in ch → add es: Watches"),
    WrongPluralEntry("Bus", "Buses", "Buss", "Bus ends in s → add es: Buses"),
    WrongPluralEntry("Brush", "Brushes", "Brushs", "Brush ends in sh → add es: Brushes"),
    WrongPluralEntry("Baby", "Babies", "Babys", "Baby ends in y → change y to ies: Babies"),
    WrongPluralEntry("City", "Cities", "Citys", "City ends in y → change y to ies: Cities"),
    WrongPluralEntry("Story", "Stories", "Storys", "Story ends in y → change y to ies: Stories"),
    WrongPluralEntry("Sheep", "Sheep", "Sheeps", "Sheep never changes → Sheep"),
    WrongPluralEntry("Deer", "Deer", "Deers", "Deer never changes → Deer"),
    WrongPluralEntry("Tomato", "Tomatoes", "Tomatos", "Tomato ends in o → add es: Tomatoes"),
)

object WrongPluralQuestionFactory {
    data class Question(val options: List<String>, val wrongOption: String, val entry: WrongPluralEntry)

    fun make(entry: WrongPluralEntry, all: List<WrongPluralEntry>): Question {
        val distractors = all
            .filter { it != entry && it.correctPlural != entry.wrongPlural }
            .shuffled()
            .take(3)
            .map { it.correctPlural }
        return Question((distractors + entry.wrongPlural).shuffled(), entry.wrongPlural, entry)
    }
}
