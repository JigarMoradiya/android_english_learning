package com.example.myapplication.data.generation.loader

data class SingularPluralPair(
    val singular: String,
    val plural: String,
    val rule: String   // e.g. "add -s", "add -es", "irregular"
)

object SingularPluralData {

    val regularS = listOf(
        SingularPluralPair("cat", "cats", "add -s"),
        SingularPluralPair("dog", "dogs", "add -s"),
        SingularPluralPair("bird", "birds", "add -s"),
        SingularPluralPair("book", "books", "add -s"),
        SingularPluralPair("tree", "trees", "add -s"),
        SingularPluralPair("pen", "pens", "add -s"),
        SingularPluralPair("ball", "balls", "add -s"),
        SingularPluralPair("flower", "flowers", "add -s"),
        SingularPluralPair("table", "tables", "add -s"),
        SingularPluralPair("chair", "chairs", "add -s"),
    )

    val regularES = listOf(
        SingularPluralPair("box", "boxes", "add -es"),
        SingularPluralPair("bus", "buses", "add -es"),
        SingularPluralPair("dish", "dishes", "add -es"),
        SingularPluralPair("bench", "benches", "add -es"),
        SingularPluralPair("fox", "foxes", "add -es"),
        SingularPluralPair("class", "classes", "add -es"),
        SingularPluralPair("dress", "dresses", "add -es"),
        SingularPluralPair("branch", "branches", "add -es"),
    )

    val irregular = listOf(
        SingularPluralPair("child", "children", "irregular"),
        SingularPluralPair("mouse", "mice", "irregular"),
        SingularPluralPair("tooth", "teeth", "irregular"),
        SingularPluralPair("foot", "feet", "irregular"),
        SingularPluralPair("man", "men", "irregular"),
        SingularPluralPair("woman", "women", "irregular"),
        SingularPluralPair("goose", "geese", "irregular"),
        SingularPluralPair("ox", "oxen", "irregular"),
    )

    val allPairs: List<SingularPluralPair> = regularS + regularES + irregular
}
