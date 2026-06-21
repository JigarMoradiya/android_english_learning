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
import com.example.myapplication.data.model.UnitSelectionScreen
import com.example.myapplication.data.model.VocabularyCategoryData
import com.example.myapplication.data.model.VocabularyCategoryType
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.base.nav.RouteNavigation


// List of activities — accent colors match the Claude Design HTML tile palette
val activities_age_3_5 = if (DeviceInfo.isTablet) {
    listOf(
        LearningActivityModel(R.string.alphabet_tracing,          R.drawable.age_3_5_alphabet_tracing_icon,           RouteNavigation.AlphabetTracing.route,         Color(0xFFE63946), moduleId = ModuleID.ALPHABET_TRACING),
        LearningActivityModel(R.string.menu_letter_phonics_sound,        R.drawable.age_3_5_letter_phonics_icon,         RouteNavigation.LetterPhonicsSoundRoute.route,        Color(0xFF6F3300), moduleId = ModuleID.LETTER_PHONICS),
        LearningActivityModel(R.string.letter_recognition,        R.drawable.age_3_5_letter_recognition_icon,         RouteNavigation.LetterRecognition.route,        Color(0xFF3674B5), moduleId = ModuleID.LETTER_RECOGNITION),
        LearningActivityModel(R.string.abcd_with_images,          R.drawable.age_3_5_abcd_images_icon,                RouteNavigation.ABCDWithImages.route,           Color(0xFFE67639), moduleId = ModuleID.ABCD_WITH_IMAGES),
        LearningActivityModel(R.string.coloring_alphabet,         R.drawable.age_3_5_coloring_alphabets_icon,         RouteNavigation.ColoringAlphabets.route,        Color(0xFFFF3872), moduleId = ModuleID.COLORING_ALPHABETS),
        LearningActivityModel(R.string.match_letters,             R.drawable.age_3_5_match_letters_icon,              RouteNavigation.MatchLetters.route,             Color(0xFF7A4481), moduleId = ModuleID.MATCH_UPPER_LOWER),
        LearningActivityModel(R.string.fill_the_blank,            R.drawable.age_3_5_fill_the_blank_icon,             RouteNavigation.FillTheBlankLetters.route,      Color(0xFF016754), moduleId = ModuleID.FILL_THE_BLANK_LETTER),
        LearningActivityModel(R.string.arrange_letter_in_sequence,R.drawable.age_3_5_arrange_letters_in_sequence_icon,RouteNavigation.ArrangeLetterInSequence.route,  Color(0xFF156419), moduleId = ModuleID.ARRANGE_LETTER_SEQUENCE),
        LearningActivityModel(R.string.match_letter_image,        R.drawable.age_3_5_match_letter_image_icon,         RouteNavigation.MatchLetterWithImage.route,     Color(0xFF008C87), moduleId = ModuleID.MATCH_LETTER_WITH_IMAGE),
        LearningActivityModel(R.string.missing_letter,            R.drawable.age_3_5_missing_letter_icon,             RouteNavigation.MissingLetterEasy.route,        Color(0xFF943256), moduleId = ModuleID.MISSING_LETTER),
    )
}else{
    listOf(
        LearningActivityModel(R.string.alphabet_tracing,          R.drawable.age_3_5_alphabet_tracing_icon,           RouteNavigation.AlphabetTracing.route,         Color(0xFFE63946), moduleId = ModuleID.ALPHABET_TRACING),
        LearningActivityModel(R.string.match_letters,             R.drawable.age_3_5_match_letters_icon,              RouteNavigation.MatchLetters.route,             Color(0xFF7A4481), moduleId = ModuleID.MATCH_UPPER_LOWER),
        LearningActivityModel(R.string.menu_letter_phonics_sound,        R.drawable.age_3_5_letter_phonics_icon,         RouteNavigation.LetterPhonicsSoundRoute.route,        Color(0xFF6F3300), moduleId = ModuleID.LETTER_PHONICS),
        LearningActivityModel(R.string.fill_the_blank,            R.drawable.age_3_5_fill_the_blank_icon,             RouteNavigation.FillTheBlankLetters.route,      Color(0xFF016754), moduleId = ModuleID.FILL_THE_BLANK_LETTER),
        LearningActivityModel(R.string.letter_recognition,        R.drawable.age_3_5_letter_recognition_icon,         RouteNavigation.LetterRecognition.route,        Color(0xFF3674B5), moduleId = ModuleID.LETTER_RECOGNITION),
        LearningActivityModel(R.string.arrange_letter_in_sequence,R.drawable.age_3_5_arrange_letters_in_sequence_icon,RouteNavigation.ArrangeLetterInSequence.route,  Color(0xFF156419), moduleId = ModuleID.ARRANGE_LETTER_SEQUENCE),
        LearningActivityModel(R.string.abcd_with_images,          R.drawable.age_3_5_abcd_images_icon,                RouteNavigation.ABCDWithImages.route,           Color(0xFFE67639), moduleId = ModuleID.ABCD_WITH_IMAGES),
        LearningActivityModel(R.string.match_letter_image,        R.drawable.age_3_5_match_letter_image_icon,         RouteNavigation.MatchLetterWithImage.route,     Color(0xFF008C87), moduleId = ModuleID.MATCH_LETTER_WITH_IMAGE),
        LearningActivityModel(R.string.coloring_alphabet,         R.drawable.age_3_5_coloring_alphabets_icon,         RouteNavigation.ColoringAlphabets.route,        Color(0xFFFF3872), moduleId = ModuleID.COLORING_ALPHABETS),
        LearningActivityModel(R.string.missing_letter,            R.drawable.age_3_5_missing_letter_icon,             RouteNavigation.MissingLetterEasy.route,        Color(0xFF943256), moduleId = ModuleID.MISSING_LETTER),
    )
}
//    LearningActivityModel(R.string.drag_drop_words,           R.drawable.age_3_5_drag_drop_word_icon,             RouteNavigation.DragDropWord.route,             Color(0xFF0567BD), moduleId = ModuleID.DRAG_DROP_LETTERS),

val activities_age_5_7 = if (DeviceInfo.isTablet){
    listOf(
        LearningActivityModel(R.string.vocabulary_building,    R.drawable.age_5_7_vocabulary_building,  RouteNavigation.VocabularyBuilding.route,       Color(0xFF3674B5)),
        LearningActivityModel(R.string.opposite_words,         R.drawable.age_5_7_opposite_words,     RouteNavigation.OppositeWords.route,            Color(0xFF2F7D32), moduleId = ModuleID.OPPOSITES_WORD),
        LearningActivityModel(R.string.singular_plural,        R.drawable.age_5_7_singular_plural,          RouteNavigation.SingularPlural.route,           Color(0xFF7D4584), moduleId = ModuleID.SINGULAR_PLURAL),
        LearningActivityModel(R.string.word_match_picture,     R.drawable.age_5_7_match_word_image,     RouteNavigation.WordMatchImage.route,           Color(0xFF9929EA), moduleId = ModuleID.MATCH_WORD_WITH_PICTURE),
        LearningActivityModel(R.string.listen_and_select_answer,R.drawable.age_5_7_listen_and_select,  RouteNavigation.ListenAndSelectWord.route,      Color(0xFFA53860), moduleId = ModuleID.LISTEN_AND_SELECT),
        LearningActivityModel(R.string.missing_letter,          R.drawable.age_5_7_missing_letter,       RouteNavigation.MissingLetterMedium.route,      Color(0xFFF97A00), moduleId = ModuleID.MISSING_LETTER_57),
        LearningActivityModel(R.string.word_jigsaw,            R.drawable.age_5_7_word_jigsaw,          RouteNavigation.WordJigsaw.route,               Color(0xFF143D60), moduleId = ModuleID.WORD_JIGSAW),
        LearningActivityModel(R.string.articles_a_an,          R.drawable.age_5_7_articles_a_an,        RouteNavigation.ArticlesAAn.route,              Color(0xFFB22222), moduleId = ModuleID.ARTICLES_A_AN),
        LearningActivityModel(R.string.sight_words,            R.drawable.age_5_7_sight_words,    RouteNavigation.SightWords.route,               Color(0xFF26675A), moduleId = ModuleID.SIGHT_WORDS),
        LearningActivityModel(R.string.article_choice,         R.drawable.age_5_7_article_choice,       RouteNavigation.ArticleChoice.route,            Color(0xFF3F7D58), moduleId = ModuleID.ARTICLES_CHOICE),
        LearningActivityModel(R.string.sight_word_choice,      R.drawable.age_5_7_sight_word_choice,    RouteNavigation.SightWordChoice.route,          Color(0xFFB45253), moduleId = ModuleID.SIGHT_WORD_CHOICE),
        LearningActivityModel(R.string.phonics_reading,                  R.drawable.age_5_7_phonics_reading_icon,         RouteNavigation.PhonicsReadingLevels.route,           Color(0xFF2C2EB9), moduleId = ModuleID.PHONICS_READING),
    )
}else{
    listOf(
        LearningActivityModel(R.string.vocabulary_building,    R.drawable.age_5_7_vocabulary_building,  RouteNavigation.VocabularyBuilding.route,       Color(0xFF3674B5)),
        LearningActivityModel(R.string.word_jigsaw,            R.drawable.age_5_7_word_jigsaw,          RouteNavigation.WordJigsaw.route,               Color(0xFF143D60), moduleId = ModuleID.WORD_JIGSAW),
        LearningActivityModel(R.string.opposite_words,         R.drawable.age_5_7_opposite_words,     RouteNavigation.OppositeWords.route,            Color(0xFF2F7D32), moduleId = ModuleID.OPPOSITES_WORD),
        LearningActivityModel(R.string.articles_a_an,          R.drawable.age_5_7_articles_a_an,        RouteNavigation.ArticlesAAn.route,              Color(0xFFB22222), moduleId = ModuleID.ARTICLES_A_AN),
        LearningActivityModel(R.string.singular_plural,        R.drawable.age_5_7_singular_plural,          RouteNavigation.SingularPlural.route,           Color(0xFF7D4584), moduleId = ModuleID.SINGULAR_PLURAL),
        LearningActivityModel(R.string.sight_words,            R.drawable.age_5_7_sight_words,    RouteNavigation.SightWords.route,               Color(0xFF26675A), moduleId = ModuleID.SIGHT_WORDS),
        LearningActivityModel(R.string.word_match_picture,     R.drawable.age_5_7_match_word_image,     RouteNavigation.WordMatchImage.route,           Color(0xFF9929EA), moduleId = ModuleID.MATCH_WORD_WITH_PICTURE),
        LearningActivityModel(R.string.article_choice,         R.drawable.age_5_7_article_choice,       RouteNavigation.ArticleChoice.route,            Color(0xFF3F7D58), moduleId = ModuleID.ARTICLES_CHOICE),
        LearningActivityModel(R.string.listen_and_select_answer,R.drawable.age_5_7_listen_and_select,  RouteNavigation.ListenAndSelectWord.route,      Color(0xFFA53860), moduleId = ModuleID.LISTEN_AND_SELECT),
        LearningActivityModel(R.string.sight_word_choice,      R.drawable.age_5_7_sight_word_choice,    RouteNavigation.SightWordChoice.route,          Color(0xFFB45253), moduleId = ModuleID.SIGHT_WORD_CHOICE),
        LearningActivityModel(R.string.missing_letter,          R.drawable.age_5_7_missing_letter,       RouteNavigation.MissingLetterMedium.route,      Color(0xFFF97A00), moduleId = ModuleID.MISSING_LETTER_57),
        LearningActivityModel(R.string.phonics_reading,                  R.drawable.age_5_7_phonics_reading_icon,         RouteNavigation.PhonicsReadingLevels.route,           Color(0xFF2C2EB9), moduleId = ModuleID.PHONICS_READING),
    )
}
//    LearningActivityModel(R.string.phonics_reading,                  R.drawable.age_5_7_phonics_reading_icon,         RouteNavigation.PhonicsReadingLevels.route,           Color(0xFF2C2EB9), moduleId = ModuleID.PHONICS_READING),
//    LearningActivityModel(R.string.missing_letter,          R.drawable.age_5_7_coloring_word,       RouteNavigation.MissingLetterMedium.route,      Color(0xFFE91E63), moduleId = ModuleID.MISSING_LETTER_57),

val vocabularyCategoryDataList = listOf(
    VocabularyCategoryData(VocabularyCategoryType.SHAPES.name, R.string.shapes, R.drawable.age_5_7_vocabulary_category_shapes, vocabularyCategoryShape),
    VocabularyCategoryData(VocabularyCategoryType.COLORS.name, R.string.colors, R.drawable.age_5_7_vocabulary_category_colors, vocabularyCategoryColor, moduleId = ModuleID.VOCABULARY_COLORS),
    VocabularyCategoryData(VocabularyCategoryType.ANIMALS.name, R.string.animals, R.drawable.age_5_7_vocabulary_category_animals, vocabularyCategoryAnimals, moduleId = ModuleID.VOCABULARY_ANIMALS),
    VocabularyCategoryData(VocabularyCategoryType.FRUITS.name, R.string.fruits, R.drawable.age_5_7_vocabulary_category_fruits, vocabularyCategoryFruits, moduleId = ModuleID.VOCABULARY_FRUITS),
    VocabularyCategoryData(VocabularyCategoryType.BIRDS.name, R.string.birds, R.drawable.age_5_7_vocabulary_category_birds, vocabularyCategoryBirds, moduleId = ModuleID.VOCABULARY_BIRDS),
    VocabularyCategoryData(VocabularyCategoryType.VEGETABLES.name, R.string.vegetables, R.drawable.age_5_7_vocabulary_category_vegetables, vocabularyCategoryVegetables, moduleId = ModuleID.VOCABULARY_VEGETABLES),
    VocabularyCategoryData(VocabularyCategoryType.VEHICLES.name, R.string.vehicles, R.drawable.age_5_7_vocabulary_category_vehicles, vocabularyCategoryVehicle, moduleId = ModuleID.VOCABULARY_VEHICLES)
)

val activities_age_6_8 = if (DeviceInfo.isTablet){
    listOf(
        LearningActivityModel(R.string.readListen,            R.drawable.age_6_8_read_sentences,          RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name), Color(0xFFD62828), moduleId = ModuleID.READ_LISTEN),  // FREE for all users
        LearningActivityModel(R.string.oneWordAnswer,         R.drawable.age_6_8_one_word_answer,          RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.ONE_WORD_ANSWER.name),          Color(0xFFFF4400), moduleId = ModuleID.ONE_WORD_ANSWER),
        LearningActivityModel(R.string.fillTheMissingWord,    R.drawable.age_6_8_fill_the_missing_word,    RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.FILL_THE_MISSING_WORD.name),    Color(0xFF85479D), moduleId = ModuleID.FILL_MISSING_WORD),
        LearningActivityModel(R.string.sentenceCheck,         R.drawable.age_6_8_sentence_check,           RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.SENTENCE_CHECK.name),           Color(0xFFBC6900), moduleId = ModuleID.SENTENCE_CHECK),
        LearningActivityModel(R.string.chooseTheRightSentence,R.drawable.age_6_8_choose_the_right_sentence,RouteNavigation.ChooseTheRightSentence.route,Color(0xFF3265D3), moduleId = ""),  // FREE entry; inner pages gate themselves
        LearningActivityModel(R.string.sentenceBuilder,       R.drawable.age_6_8_build_the_sentence,       RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.BUILD_THE_SENTENCE.name),       Color(0xFF237227), moduleId = ModuleID.SENTENCE_BUILDER),
        LearningActivityModel(R.string.grammarBasics,         R.drawable.age_6_8_grammer_basics,           RouteNavigation.GrammarBasic.route,  Color(0xFF7315B7), moduleId = ModuleID.GRAMMAR_NOUNS),
        LearningActivityModel(R.string.grammar_challenge,         R.drawable.age_6_8_grammar_challenge,           RouteNavigation.MixedGrammarChallenge.route,Color(0xFFD5074E), moduleId = ModuleID.GRAMMAR_NOUNS),
    )
}else{
    listOf(
        LearningActivityModel(R.string.readListen,            R.drawable.age_6_8_read_sentences,          RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.READ_AND_LISTEN_SENTENCE.name), Color(0xFFD62828), moduleId = ModuleID.READ_LISTEN),  // FREE for all users
        LearningActivityModel(R.string.chooseTheRightSentence,R.drawable.age_6_8_choose_the_right_sentence,RouteNavigation.ChooseTheRightSentence.route,Color(0xFF3265D3), moduleId = ""),  // FREE entry; inner pages gate themselves
        LearningActivityModel(R.string.oneWordAnswer,         R.drawable.age_6_8_one_word_answer,          RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.ONE_WORD_ANSWER.name),          Color(0xFFFF4400), moduleId = ModuleID.ONE_WORD_ANSWER),
        LearningActivityModel(R.string.sentenceBuilder,       R.drawable.age_6_8_build_the_sentence,       RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.BUILD_THE_SENTENCE.name),       Color(0xFF237227), moduleId = ModuleID.SENTENCE_BUILDER),
        LearningActivityModel(R.string.fillTheMissingWord,    R.drawable.age_6_8_fill_the_missing_word,    RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.FILL_THE_MISSING_WORD.name),    Color(0xFF85479D), moduleId = ModuleID.FILL_MISSING_WORD),
        LearningActivityModel(R.string.grammarBasics,         R.drawable.age_6_8_grammer_basics,           RouteNavigation.GrammarBasic.route,  Color(0xFF7315B7), moduleId = ModuleID.GRAMMAR_NOUNS),
        LearningActivityModel(R.string.sentenceCheck,         R.drawable.age_6_8_sentence_check,           RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.SENTENCE_CHECK.name),           Color(0xFFBC6900), moduleId = ModuleID.SENTENCE_CHECK),
        LearningActivityModel(R.string.grammar_challenge,         R.drawable.age_6_8_grammar_challenge,           RouteNavigation.MixedGrammarChallenge.route,Color(0xFFD5074E), moduleId = ModuleID.GRAMMAR_NOUNS),
    )
}

val activities_age_6_8_right_sentence = listOf(
    LearningActivityModel(R.string.match_the_picture_desc,              R.drawable.age_6_8_right_sentence_match_the_picture,            RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.MATCH_THE_PICTURE.name),            Color(0xFF872404), moduleId = ModuleID.MATCH_THE_PICTURE),        // PREMIUM
    LearningActivityModel(R.string.which_sentence_sounds_right_desc,    R.drawable.age_6_8_right_sentence_which_sentence_sounds_right,  RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.WHICH_SENTENCE_SOUNDS_RIGHT.name),  Color(0xFF872404), moduleId = ModuleID.WHICH_SENTENCE_RIGHT),   // PREMIUM
    LearningActivityModel(R.string.find_the_correct_writing_desc,       R.drawable.age_6_8_right_sentence_find_the_correct_writing,     RouteNavigation.SentenceUnitList.sentenceUnitList(UnitSelectionScreen.FIND_THE_CORRECT_WRITING.name),     Color(0xFF872404), moduleId = ModuleID.FIND_CORRECT_WRITING),   // PREMIUM
)

val activities_age_6_8_grammar_basics = listOf(
    LearningActivityModel(R.string.nouns,      R.drawable.age_6_8_grammar_basic_noun,      RouteNavigation.GrammarBasicNoun.route,       Color(0xFF237227).copy(alpha = 0.20f), moduleId = ModuleID.GRAMMAR_NOUNS),
    LearningActivityModel(R.string.verbs,      R.drawable.age_6_8_grammar_basic_verb,      RouteNavigation.GrammarBasicVerb.route,       Color(0xFF85479D).copy(alpha = 0.20f), moduleId = ModuleID.GRAMMAR_VERBS),
    LearningActivityModel(R.string.adjectives, R.drawable.age_6_8_grammar_basic_adjective, RouteNavigation.GrammarBasicAdjectives.route, Color(0xFFFF9D00).copy(alpha = 0.20f), moduleId = ModuleID.GRAMMAR_ADJECTIVES),
    LearningActivityModel(R.string.pronouns,   R.drawable.age_6_8_grammar_basic_pronoun,   RouteNavigation.GrammarBasicPronouns.route,   Color(0xFFD62828).copy(alpha = 0.20f), moduleId = ModuleID.GRAMMAR_PRONOUNS),
)
