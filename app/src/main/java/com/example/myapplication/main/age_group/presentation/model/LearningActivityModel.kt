package com.example.myapplication.main.age_group.presentation.model

import androidx.compose.ui.graphics.Color
import com.example.myapplication.R
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryAnimals
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryBirds
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryColor
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryFruits
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryShape
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryVegetables
import com.example.myapplication.data.generation.letter.LetterRepository.vocabularyCategoryVehicle
import com.example.myapplication.data.model.LearningActivityModel
import com.example.myapplication.data.model.VocabularyCategoryData
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.main.base.nav.RouteNavigation


// List of activities
val activities_age_3_5 = listOf(
    LearningActivityModel(R.string.alphabet_tracing, R.drawable.age_3_5_alphabet_tracing_icon, RouteNavigation.AlphabetTracing.route),
    LearningActivityModel(R.string.letter_recognition, R.drawable.age_3_5_letter_recognition_icon,RouteNavigation.LetterRecognition.route),
    LearningActivityModel(R.string.abcd_with_images, R.drawable.age_3_5_abcd_images_icon,RouteNavigation.ABCDWithImages.route),
    LearningActivityModel(R.string.coloring_alphabet, R.drawable.age_3_5_coloring_alphabets_icon,RouteNavigation.ColoringAlphabets.route),
    LearningActivityModel(R.string.match_letters, R.drawable.age_3_5_match_letters_icon,RouteNavigation.MatchLetters.route),
    LearningActivityModel(R.string.match_letter_image, R.drawable.age_3_5_match_letter_image_icon,RouteNavigation.MatchLetterWithImage.route),
    LearningActivityModel(R.string.missing_letter, R.drawable.age_3_5_missing_letter_icon,RouteNavigation.MissingLetterEasy.route),
    LearningActivityModel(R.string.drag_drop_words, R.drawable.age_3_5_drag_drop_word_icon,RouteNavigation.DragDropWord.route),
)

val activities_age_5_7 = listOf(
    LearningActivityModel(R.string.vocabulary_building,R.drawable.age_5_7_vocabulary_building, RouteNavigation.VocabularyBuilding.route,Color(0xFF3674B5)),
//    LearningActivityModel(R.string.coloring_word, R.drawable.age_5_7_coloring_word,RouteNavigation.ColoringWord.route,Color(0xFF3E5F44)),
    LearningActivityModel(R.string.word_match_picture, R.drawable.age_5_7_match_word_image,RouteNavigation.WordMatchImage.route,Color(0xFF9929EA)),
    LearningActivityModel(R.string.listen_and_select_answer, R.drawable.age_5_7_listen_and_select,RouteNavigation.ListenAndSelectWord.route,Color(0xFFA53860)),
    LearningActivityModel(R.string.match_letters, R.drawable.age_5_7_missing_letter,RouteNavigation.MissingLetterMedium.route,Color(0xFFF97A00)),
    LearningActivityModel(R.string.word_jigsaw, R.drawable.age_5_7_word_jigsaw,RouteNavigation.WordJigsaw.route,Color(0xFF143D60)),
    LearningActivityModel(R.string.articles_a_an, R.drawable.age_5_7_articles_a_an,RouteNavigation.ArticlesAAn.route,Color(0xFFB22222)),
    LearningActivityModel(R.string.sight_words, R.drawable.age_5_7_sight_words_title,RouteNavigation.SightWords.route,Color(0xFF7C4585)),
    LearningActivityModel(R.string.article_choice, R.drawable.age_5_7_article_choice,RouteNavigation.ArticleChoice.route,Color(0xFF3F7D58)),
    LearningActivityModel(R.string.sight_word_choice, R.drawable.age_5_7_sight_word_choice,RouteNavigation.SightWordChoice.route,Color(0xFFB45253)),
)

val vocabularyCategoryDataList = listOf(
    VocabularyCategoryData(VocabularyCategoryType.COLORS.name, R.string.colors, R.drawable.age_5_7_vocabulary_category_colors, vocabularyCategoryColor),
    VocabularyCategoryData(VocabularyCategoryType.SHAPES.name, R.string.shapes, R.drawable.age_5_7_vocabulary_category_shapes, vocabularyCategoryShape),
    VocabularyCategoryData(VocabularyCategoryType.ANIMALS.name, R.string.animals, R.drawable.age_5_7_vocabulary_category_animals, vocabularyCategoryAnimals),
    VocabularyCategoryData(VocabularyCategoryType.FRUITS.name, R.string.fruits, R.drawable.age_5_7_vocabulary_category_fruits, vocabularyCategoryFruits),
    VocabularyCategoryData(VocabularyCategoryType.BIRDS.name, R.string.birds, R.drawable.age_5_7_vocabulary_category_birds, vocabularyCategoryBirds),
    VocabularyCategoryData(VocabularyCategoryType.VEGETABLES.name, R.string.vegetables, R.drawable.age_5_7_vocabulary_category_vegetables, vocabularyCategoryVegetables),
    VocabularyCategoryData(VocabularyCategoryType.VEHICLES.name, R.string.vehicles, R.drawable.age_5_7_vocabulary_category_vehicles, vocabularyCategoryVehicle)
)