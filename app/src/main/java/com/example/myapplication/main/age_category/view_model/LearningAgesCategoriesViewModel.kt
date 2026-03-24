package com.example.myapplication.main.age_category.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.R
import com.example.myapplication.main.age_category.presentation.model.LearningCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LearningAgesCategoriesViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) { // use AndroidViewModel to get context

    private val context = getApplication<Application>()

    private val _categories = MutableStateFlow(
        listOf(
            LearningCategory(
                title = context.getString(R.string.beginner_level),
                ageRange = context.getString(R.string.ages_3_5),
                destination = "age_group_3_5"
            ),
            LearningCategory(
                title = context.getString(R.string.early_words),
                ageRange = context.getString(R.string.ages_5_7),
                destination = "age_group_5_7"
            ),
            LearningCategory(
                title = context.getString(R.string.advanced_learning),
                ageRange = context.getString(R.string.ages_6_9),
                destination = "age_group_6_9"
            )
        )
    )
    val categories: StateFlow<List<LearningCategory>> = _categories.asStateFlow()
}