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
                img = R.drawable._menu_age_3_5,
                destination = RouteNavigation.AgeGroup3to5.route
            ),
            AgeCategoryData(
                img = R.drawable._menu_age_5_7,
                destination = RouteNavigation.AgeGroup5to7.route
            ),
            AgeCategoryData(
                img = R.drawable._menu_age_6_8,
                destination = RouteNavigation.AgeGroup6to8.route
            )
        )
    )
    val categories: StateFlow<List<AgeCategoryData>> = _categories.asStateFlow()
}