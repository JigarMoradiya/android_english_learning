package com.example.myapplication.main.age_category.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.R
import com.example.myapplication.main.base.nav.RouteNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AgeCategoriesViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) { // use AndroidViewModel to get context

    private val _categories = MutableStateFlow(
        listOf(
            AgeCategoryData(
                img = R.drawable._level_1_icon,
                destination = RouteNavigation.AgeGroup3to5.name
            ),
            AgeCategoryData(
                img = R.drawable._level_2_icon,
                destination = RouteNavigation.AgeGroup5to7.name
            ),
            AgeCategoryData(
                img = R.drawable._level_3_icon,
                destination = RouteNavigation.AgeGroup6to8.name
            )
        )
    )
    val categories: StateFlow<List<AgeCategoryData>> = _categories.asStateFlow()
}