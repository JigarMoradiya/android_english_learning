package com.example.myapplication.data.generation.letter

object LetterRepository {
    val all: List<LetterData> = listOf(

        LetterData("A", "Ant", listOf("Apple", "Airplane", "Axe", "Alligator", "Arrow"), "a_sound.mp3"),
        LetterData("B", "Bat", listOf("Bee", "Ball", "Butterfly", "Boy", "Banana", "Balloon"), "b_sound.mp3"),
        LetterData("C", "Cat", listOf("Car", "Cake", "Candle", "Cow", "Carrot"), "c_sound.mp3"),
        LetterData("D", "Drum", listOf( "Dog", "Duck", "Door", "Deer", "Doll"), "d_sound.mp3"),
        LetterData("E", "Elephant", listOf("Egg", "Eagle", "Ear", "Envelope", "Eye"), "e_sound.mp3"),
        LetterData("F", "Fish", listOf("Fan", "Frog", "Fox", "Flower"), "f_sound.mp3"),
        LetterData("G", "Grapes", listOf("Goat", "Giraffe", "Gift", "Girl", "Guitar"), "g_sound.mp3"),
        LetterData("H", "Hen", listOf("Horse", "Hat", "House", "Hand", "Hammer"), "h_sound.mp3"),
        LetterData("I", "Icecream", listOf("Igloo", "Island", "Ice"), "i_sound.mp3"),
        LetterData("J", "Joker", listOf("Jug", "Jellyfish", "Jam", "Jet", "Jar"), "j_sound.mp3"),
        LetterData("K", "Kiwi", listOf("King", "Kite", "Key", "Knife", "Kangaroo"), "k_sound.mp3"),
        LetterData("L", "Lion", listOf("Lamp", "Leaf", "Leg", "Ladder", "Lock"), "l_sound.mp3"),
        LetterData("M", "Monkey", listOf("Moon", "Mango", "Mushroom", "Mouse", "Mitten"), "m_sound.mp3"),
        LetterData("N", "Nose", listOf("Nurse", "Net", "Nest", "Neck"), "n_sound.mp3"),
        LetterData("O", "Owl", listOf("Orange", "Ox", "Octopus", "Onion", "Ostrich"), "o_sound.mp3"),
        LetterData("P", "Parrot", listOf("Pig", "Pen", "Penguin", "Pot", "Pumpkin"), "p_sound.mp3"),
        LetterData("Q", "Queen", listOf("Quill", "Quilt", "Question Mark", "Quiet"), "q_sound.mp3"),
        LetterData("R", "Rose", listOf("Rabbit", "Road", "Rope", "Rocket"), "r_sound.mp3"),
        LetterData("S", "Sun", listOf("Star", "Ship", "Snake", "Spoon", "Soap"), "s_sound.mp3"),
        LetterData("T", "Tiger", listOf("Tree", "Train", "Table", "Teeth", "Tomato"), "t_sound.mp3"),
        LetterData("U", "Umbrella", listOf("Unicorn", "Uniform", "Utensil", "UFO"), "u_sound.mp3"),
        LetterData("V", "Van", listOf("Vase", "Volcano", "Violin", "Vegetable", "Vulture"), "v_sound.mp3"),
        LetterData("W", "Watermelon", listOf("Wall", "Wolf", "Wheel", "Watch", "Window"), "w_sound.mp3"),
        LetterData("X", "Xylophone", listOf("X-ray", "Xmas-tree", "Xenops"), "x_sound.mp3"),
        LetterData("Y", "Yoga", listOf("Yak", "Yacht", "Yarn", "Yo-Yo"), "y_sound.mp3"),
        LetterData("Z", "Zebra", listOf("Zoo", "Zero", "Zip", "Zigzag", "Zucchini"), "z_sound.mp3")
    )

    val missingLetterEasyWords4Basic = listOf(
        "Book","Ball","Cake","Doll","Fish","Frog","Girl","Home","Hand",
        "King","Neck","Kite","Leaf","Lock","Moon","Nest","Rose","Star",
        "Tree","Wall","Zero"
    )

    val missingLetterEasyWords = listOf(
        "Ant","Axe","Bee","Boy","Bus","Bat","Cow","Car","Cat","Dog",
        "Egg","Ear","Eye","Fan","Fox","Hen","Ice","Jet","Jug","Jar",
        "Jam","Key","Map","Pen","Pot","Leg","Net","Pig","Pen","Sun",
        "UFO","Van","Yak","Zoo","Zip"
    )

    val missingLetterMediumWords = listOf(
        "Boat","Goat","Gift","Lamp","Rope","Road","Ship","Vase","Wolf","Yarn",
        "Apple","Arrow","Eagle","Igloo","Horse","House","Knife","Mango","Mouse",
        "Nurse","Onion","Queen","Quill","Quilt","Snake","Spoon","Tiger","Train",
        "Table","Teeth","Wheel","Watch","Yacht","Zebra",
        "Office","Garden","School","Teacher","Banana","Balloon","Candle",
        "Guitar","Hammer","Rabbit","Window"
    )

    val missingLetterHardWords = listOf(
        "Soap","Mitten","Elephant","Dinosaur","Computer","Banana","Jupiter","Astonaut"
    )
}