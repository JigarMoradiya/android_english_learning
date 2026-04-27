package com.example.myapplication.main.age_category

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_category.presentation.CategoriesHorizontalList
import com.example.myapplication.main.age_category.view_model.AgeCategoriesViewModel
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.base.notification.OneSignalSubscriptionWatcher
import com.example.myapplication.main.common.permission.NotificationPermissionHandler
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import com.example.myapplication.utils.AppConstants

// --- UI Screen ---
@Composable
fun MainLearningAgesCategoriesScreen(
    navController: NavController,
    viewModel: AgeCategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current

    NotificationPermissionHandler()
    OneSignalSubscriptionWatcher()

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens16)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Title
            Text(
                text = stringResource(R.string.learn_english),
                fontSize = AppDimens.FontExtraLarge36,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens16))

            CategoriesHorizontalList(categories, {
                AudioPlayerManager.playSoundMenuClick()
                navController.navigate(it.destination)
            })
        }

        // 🔥 Privacy Policy (Bottom Right)
        Text(
            text = stringResource(R.string.privacy_policy),
            color = Color.Black,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodySmall.scaled(),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = AppConstants.PRIVACY_POLICY.toUri()
                        addCategory(Intent.CATEGORY_BROWSABLE)
                    }
                    context.startActivity(intent)
                }
                .padding(Dimens16)
        )
    }
}

