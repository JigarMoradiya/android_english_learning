package com.example.myapplication.main.age_category.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_category.view_model.LearningAgesCategoriesViewModel
import com.example.myapplication.main.age_category.presentation.model.LearningCategory
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppTypography
import com.example.myapplication.ui.theme.PrimaryGreen

// --- UI Screen ---
@Composable
fun MainLearningAgesCategoriesScreen(
    navController: NavController,
    viewModel: LearningAgesCategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimens.Dimens16)
    ) {
        // Title
        Text(
            text = stringResource(R.string.learn_english),
            fontSize = AppDimens.FontExtraLarge36,
            style = AppTypography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(AppDimens.Dimens16))

        // Scrollable list of Category Buttons
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppDimens.Dimens16)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { navController.navigate(category.destination) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(category: LearningCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(AppDimens.CardCornerRadius),
        elevation = CardDefaults.cardElevation(AppDimens.CardElevation),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen) // Green
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppDimens.Dimens12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = category.title,
                fontSize = AppDimens.FontLarge22,
                style = AppTypography.titleLarge,
                color = Color.White
            )
            Text(
                text = category.ageRange,
                fontSize = AppDimens.FontMedium16,
                style = AppTypography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
