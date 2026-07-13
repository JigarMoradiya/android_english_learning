package com.example.myapplication.data.generation.letter

import com.example.myapplication.main.age_group.from_3_to_5.coloring_alphabets.view_model.ColoringAlphabetModel

object LetterRepository {
    val all: List<LetterData> = listOf(
        LetterData("A", "Ant",        listOf("Apple", "Airplane", "Axe", "Alligator", "Arrow"),       phonicsSound = "Aah Aah Aah",      highlightTimings = listOf(0.20, 0.75, 1.35, 1.90, 2.40, 3.40, 3.95, 4.20)),
        LetterData("B", "Bat",        listOf("Bee", "Ball", "Butterfly", "Boy", "Banana", "Balloon"), phonicsSound = "Buh Buh Buh",      highlightTimings = listOf(0.20, 0.65, 1.15, 1.60, 2.00, 3.05, 3.70, 3.95)),
        LetterData("C", "Cat",        listOf("Car", "Cake", "Candle", "Cow", "Carrot"),               phonicsSound = "Kuh Kuh Kuh",      highlightTimings = listOf(0.15, 0.70, 1.35, 1.80, 2.10, 3.25, 3.75, 4.00)),
        LetterData("D", "Drum",       listOf("Dog", "Duck", "Door", "Deer", "Doll"),                  phonicsSound = "Duh Duh Duh",      highlightTimings = listOf(0.20, 0.70, 1.25, 1.65, 2.05, 3.20, 3.85, 4.10)),
        LetterData("E", "Elephant",   listOf("Egg", "Eagle", "Ear", "Envelope", "Eye"),               phonicsSound = "Eh Eh Eh",         highlightTimings = listOf(0.35, 0.90, 1.60, 2.10, 2.55, 3.60, 4.30, 4.65)),
        LetterData("F", "Fish",       listOf("Fan", "Frog", "Fox", "Flower"),                         phonicsSound = "Fuh Fuh Fuh",      highlightTimings = listOf(0.20, 0.70, 1.45, 1.85, 2.25, 3.50, 4.10, 4.60)),
        LetterData("G", "Goat",       listOf("Grapes", "Giraffe", "Gift", "Girl", "Guitar"),          phonicsSound = "Guh Guh Guh",      highlightTimings = listOf(0.20, 0.70, 1.50, 1.90, 2.30, 3.35, 4.05, 4.30)),
        LetterData("H", "Hat",        listOf("Horse", "Hen", "House", "Hand", "Hammer"),              phonicsSound = "Huh Huh Huh",      highlightTimings = listOf(0.20, 0.70, 1.50, 1.90, 2.30, 3.35, 4.05, 4.30)),
        LetterData("I", "Icecream",   listOf("Igloo", "Island", "Ice"),                               phonicsSound = "Ih Ih Ih",         highlightTimings = listOf(0.25, 0.80, 1.35, 1.80, 2.25, 3.45, 4.20, 4.60)),
        LetterData("J", "Joker",      listOf("Jug", "Jellyfish", "Jam", "Jet", "Jar"),                phonicsSound = "Juh Juh Juh",      highlightTimings = listOf(0.20, 0.75, 1.55, 1.95, 2.35, 3.45, 4.10, 4.45)),
        LetterData("K", "Kiwi",       listOf("King", "Kite", "Key", "Knife", "Kangaroo"),             phonicsSound = "Kuh Kuh Kuh",      highlightTimings = listOf(0.20, 0.75, 1.50, 1.95, 2.50, 3.45, 4.10, 4.35)),
        LetterData("L", "Lion",       listOf("Lamp", "Leaf", "Leg", "Ladder", "Lock"),                phonicsSound = "Luh Luh Luh",      highlightTimings = listOf(0.20, 0.75, 1.50, 2.00, 2.65, 3.85, 4.65, 4.95)),
        LetterData("M", "Monkey",     listOf("Moon", "Mango", "Mushroom", "Mouse", "Mitten"),         phonicsSound = "Muh Muh Muh",      highlightTimings = listOf(0.20, 0.75, 1.60, 2.10, 2.70, 3.80, 4.45, 4.70)),
        LetterData("N", "Nest",       listOf("Nose", "Nurse", "Net", "Neck"),                         phonicsSound = "Nuh Nuh Nuh",      highlightTimings = listOf(0.20, 0.75, 1.60, 2.15, 2.70, 3.65, 4.25, 4.50)),
        LetterData("O", "Orange",     listOf("Owl", "Ox", "Octopus", "Onion", "Ostrich"),             phonicsSound = "Oh Oh Oh",         highlightTimings = listOf(0.25, 0.85, 1.40, 1.80, 2.35, 3.50, 4.00, 4.50)),
        LetterData("P", "Parrot",     listOf("Pig", "Pen", "Penguin", "Pot", "Pumpkin"),              phonicsSound = "Puh Puh Puh",      highlightTimings = listOf(0.20, 0.75, 1.40, 1.80, 2.20, 3.30, 3.85, 4.30)),
        LetterData("Q", "Queen",      listOf("Quill", "Quilt", "Question Mark", "Quiet"),             phonicsSound = "Kwuh Kwuh Kwuh",   highlightTimings = listOf(0.25, 0.85, 1.55, 2.00, 2.45, 3.50, 4.30, 4.65)),
        LetterData("R", "Rose",       listOf("Rabbit", "Road", "Rope", "Rocket"),                     phonicsSound = "Ruh Ruh Ruh",      highlightTimings = listOf(0.20, 0.75, 1.25, 1.60, 2.10, 3.20, 3.90, 4.30)),
        LetterData("S", "Sun",        listOf("Star", "Ship", "Snake", "Spoon", "Soap"),               phonicsSound = "Sss Sss Sss",      highlightTimings = listOf(0.20, 0.75, 1.65, 2.35, 2.90, 4.10, 4.70, 5.00)),
        LetterData("T", "Tiger",      listOf("Tree", "Train", "Table", "Teeth", "Tomato"),            phonicsSound = "Tuh Tuh Tuh",      highlightTimings = listOf(0.20, 0.75, 1.30, 1.70, 2.10, 3.05, 3.70, 4.05)),
        LetterData("U", "Umbrella",   listOf("Unicorn", "Uniform", "Utensil", "UFO"),                 phonicsSound = "Uh Uh Uh",         highlightTimings = listOf(0.25, 0.85, 1.70, 2.15, 2.60, 3.75, 4.65, 4.90)),
        LetterData("V", "Van",        listOf("Vase", "Volcano", "Violin", "Vegetable", "Vulture"),    phonicsSound = "Vuh Vuh Vuh",      highlightTimings = listOf(0.20, 0.75, 1.45, 1.95, 2.35, 3.40, 4.10, 4.35)),
        LetterData("W", "Watermelon", listOf("Wall", "Wolf", "Wheel", "Watch", "Window"),             phonicsSound = "Wuh Wuh Wuh",      highlightTimings = listOf(0.25, 0.85, 1.65, 2.20, 2.65, 3.85, 4.40, 4.95)),
        LetterData("X", "Xylophone",  listOf("X-ray", "Xmas-tree", "Xenops"),                         phonicsSound = "Ks Ks Ks",         highlightTimings = listOf(0.30, 0.95, 1.45, 1.90, 2.35, 3.60, 4.20, 4.70)),
        LetterData("Y", "Yoga",       listOf("Yak", "Yacht", "Yarn", "Yo-Yo"),                        phonicsSound = "Yuh Yuh Yuh",      highlightTimings = listOf(0.20, 0.80, 1.50, 1.95, 2.40, 3.70, 4.50, 4.75)),
        LetterData("Z", "Zebra",      listOf("Zoo", "Zero", "Zip", "Zigzag", "Zucchini"),             phonicsSound = "Zuh Zuh Zuh",      highlightTimings = listOf(0.20, 0.80, 1.65, 2.10, 2.55, 3.70, 4.50, 4.75)),
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

    val vocabularyCategoryAnimals = listOf("Dog","Cat","Cow","Tiger","Monkey","Elephant","Horse","Sheep","Lion","Kangaroo","Camel","Goat","Giraffe","Donkey","Cheetah","Mouse","Rabbit","Zebra","Frog","Leopard","Turtle","Squirrel","Hippopotamus","Deer","Ox","Bear","Wolf","Fox","Pig","Alligator","Snake","Antelope","Gorilla","Rhinoceros","Koala","Panda","Hyena","Hedgehog","Raccoon","Yak")

    val vocabularyCategoryBirds = listOf("Sparrow","Parrot","Peacock","Crow","Eagle","Pigeon","Owl","Duck","Swan","Hen","Rooster","Turkey","Penguin","Woodpecker","Flamingo","Seagull","Kingfisher","Canary","Dove","Vulture","Ostrich")

    val vocabularyCategoryFruits = listOf("Banana","Apple","Orange","Mango","Kiwi","Grapes","Pineapple","Papaya","Strawberry","Blueberry","Raspberry","Watermelon","Muskmelon","Guava","Pomegranate","Peach","Pear","Plum","Lychee","Coconut","Cherry","Apricot","Fig","Dragonfruit")

    val vocabularyCategoryVegetables = listOf("Carrot","Potato","Tomato","Onion","Garlic","Cabbage","Brinjal","Capsicum","Ladyfinger","Cauliflower","Broccoli","Spinach","Lettuce","Peas","Cucumber","Pumpkin","Radish","Beetroot","Corn","Chili","Turnip","Celery","Zucchini")

    val vocabularyCategoryColor = listOf("Red","Blue","Yellow","Green","Sky Blue","Purple","Orange","Pink","Brown","Gray","Black","White","Navy","Crimson","Scarlet","Turquoise","Violet","Beige")

    val vocabularyCategoryShape = listOf("Circle","Square","Triangle","Rectangle","Star","Oval","Heart","Arrow","Cross","Trapezoid","Parallelogram","Crescent","Cube","Sphere","Cone","Cylinder","Diamond","Pentagon","Hexagon","Octagon")

    val vocabularyCategoryVehicle = listOf("Car","Bus","Bike","Train","Airplane","Truck","Van","Bicycle","Scooter","Ship","Boat","Helicopter","Jeep","Tractor","Ambulance","Firetruck","Police Car","Submarine","Rocket","Taxi","Forklift","Dump Truck","JCB")

    val vocabularyCategoryAllForListenAndSelect: List<String> = vocabularyCategoryAnimals + vocabularyCategoryBirds + vocabularyCategoryFruits + vocabularyCategoryVegetables + vocabularyCategoryColor + vocabularyCategoryShape + vocabularyCategoryVehicle
    val vocabularyCategoryAllForWordMatchImage: List<String> = vocabularyCategoryAnimals + vocabularyCategoryBirds + vocabularyCategoryFruits + vocabularyCategoryVegetables + vocabularyCategoryShape + vocabularyCategoryVehicle

    val colorAlphabets: List<ColoringAlphabetModel>
        get() = all.map { data ->

            // SAME AS iOS
            val chosenWord = listOf(data.mainWord).randomOrNull() ?: data.mainWord
            // if you want altWords also:
            // val chosenWord = (listOf(data.mainWord) + data.altWords).random()

            ColoringAlphabetModel(
                letter = data.letter,
                word = chosenWord,
                outlineImageName = "${data.letter.lowercase()}_outline_c",
//                outlineImageName = "a_outline_circle",
                audioName = null
            )
        }
}