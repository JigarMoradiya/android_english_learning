package com.example.myapplication.data.generation.loader

import com.example.myapplication.data.model.TrueFalseQuestion
import java.util.UUID

/**
 * Phase 4 content: kid-friendly knowledge True/False questions mixed into the
 * Sentence Check module (3.1 "mixes fun facts with grammar") with a clear
 * explanation for each (3.2). Mirrors iOS FunFactBank.swift.
 */
object FunFactBank {

    private data class Fact(val statement: String, val isTrue: Boolean, val explanation: String)

    private val facts = listOf(
        Fact("The sun is a star.",            true,  "Yes! The sun is a big, hot star."),
        Fact("A spider has eight legs.",      true,  "Correct! Spiders have eight legs."),
        Fact("Fish live in water.",           true,  "Yes! Fish live and breathe in water."),
        Fact("Cows give us milk.",            true,  "Right! We get milk from cows."),
        Fact("Ice is frozen water.",          true,  "Yes! Ice is water that turned very cold."),
        Fact("Bees make honey.",              true,  "Correct! Bees make sweet honey."),
        Fact("Plants need water to grow.",    true,  "Yes! Plants drink water to grow."),
        Fact("A triangle has three sides.",   true,  "Right! A triangle has three sides."),
        Fact("Rain comes from clouds.",       true,  "Yes! Rain falls down from clouds."),
        Fact("Birds have wings.",             true,  "Correct! Birds use wings to fly."),
        Fact("A baby dog is called a puppy.", true,  "Yes! A baby dog is a puppy."),
        Fact("We hear with our ears.",        true,  "Right! Our ears help us hear."),
        Fact("The moon is bigger than the sun.", false, "No — the sun is much bigger than the moon."),
        Fact("A spider has six legs.",        false, "No — a spider has eight legs."),
        Fact("Penguins can fly.",             false, "No — penguins swim, they cannot fly."),
        Fact("A week has ten days.",          false, "No — a week has seven days."),
        Fact("The sky is green.",             false, "No — the sky is usually blue."),
        Fact("Snow is hot.",                  false, "No — snow is cold."),
        Fact("Cats say moo.",                 false, "No — cats say meow. Cows say moo."),
        Fact("Fire is cold.",                 false, "No — fire is very hot."),
        Fact("Two plus two is five.",         false, "No — two plus two is four."),
        Fact("The sun comes out at night.",   false, "No — the sun comes out in the day."),
        Fact("We eat with our feet.",         false, "No — we eat with our mouth."),
        Fact("Fish can walk on land.",        false, "No — fish swim in water."),
    )

    fun questions(count: Int): List<TrueFalseQuestion> =
        facts.shuffled().take(count).map { fact ->
            TrueFalseQuestion(
                id = "fact_" + UUID.randomUUID().toString(),
                imageName = "",                    // text-only knowledge question
                statement = fact.statement,
                isTrue = if (fact.isTrue) "true" else "false",
                explanation = fact.explanation
            )
        }
}
