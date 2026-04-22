package com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.match_the_picture

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_6_to_8.choose_the_right_sentence.match_the_picture.view_model.MatchThePictureViewModel
import com.example.myapplication.main.age_group.from_6_to_8.sentence_check.view_model.SentenceCheckViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI


@Composable
fun MatchThePicturePage(
    navController: NavController,
    viewModel: MatchThePictureViewModel = hiltViewModel()
) {

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundUI(isGreenGrassShow = false)
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            BackButtonWithText(
                title = stringResource(R.string.match_the_picture),
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.weight(1f))


        }
    }
}
