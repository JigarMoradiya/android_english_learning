package com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.from_5_to_7.singular_plural.match_form.view_model.MatchSingularPluralViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.BackgroundUI
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.MatchWordBoxHeight
import com.example.myapplication.ui.theme.AppDimens.MatchWordBoxWidth
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.scaled

@Composable
fun MatchSingularPluralPage(
    navController: NavController,
    viewModel: MatchSingularPluralViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundUI(isGreenGrassShow = false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            BackButtonWithText(
                title = "Match Singular & Plural",
                onBackClick = { navController.popBackStack() }
            )

            if (uiState.isCompleted) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🎉 All Matched!",
                        style = MaterialTheme.typography.displaySmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Spacer(Modifier.height(Dimens24))
                    KidsActionButton(
                        text = "Play Again",
                        type = ButtonType.ORANGE,
                        onClick = { viewModel.loadPairs() }
                    )
                    Spacer(Modifier.height(Dimens12))
                    KidsActionButton(
                        text = "Go Back",
                        type = ButtonType.BLUE,
                        onClick = { navController.popBackStack() }
                    )
                }
            } else {
                Spacer(Modifier.weight(1f))

                Text(
                    text = "Match each word with its plural form!",
                    style = MaterialTheme.typography.titleSmall.scaled(),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens16)
                )
                Spacer(Modifier.height(Dimens16))

                // Column headers
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens24),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        "Singular",
                        style = MaterialTheme.typography.labelLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Plural",
                        style = MaterialTheme.typography.labelLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(Dimens8))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens24),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens12),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        uiState.leftWords.forEach { word ->
                            val isMatched = uiState.matchedKeys.contains(word)
                            val isSelected = uiState.selectedLeft == word
                            val isWrong = uiState.wrongFlashLeft == word
                            MatchChip(
                                text = word,
                                isMatched = isMatched,
                                isSelected = isSelected,
                                isWrong = isWrong,
                                onClick = { if (!isMatched) viewModel.selectLeft(word) }
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens12),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        uiState.rightWords.forEach { word ->
                            val matchedKey = uiState.pairs.find { it.plural == word }?.singular
                            val isMatched = matchedKey != null && uiState.matchedKeys.contains(matchedKey)
                            val isSelected = uiState.selectedRight == word
                            val isWrong = uiState.wrongFlashRight == word
                            MatchChip(
                                text = word,
                                isMatched = isMatched,
                                isSelected = isSelected,
                                isWrong = isWrong,
                                onClick = { if (!isMatched) viewModel.selectRight(word) }
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MatchChip(
    text: String,
    isMatched: Boolean,
    isSelected: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = when {
            isMatched  -> Color(0xFF4CAF50)
            isWrong    -> Color(0xFFF44336)
            isSelected -> Color(0xFF42A5F5)
            else       -> Color.White
        },
        label = "chipColor"
    )
    val textColor = if (isMatched || isSelected || isWrong) Color.White else Color.Black
    Box(
        modifier = Modifier
            .width(MatchWordBoxWidth)
            .height(MatchWordBoxHeight)
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .border(Dimens2, if (isSelected) Color(0xFF005EA9) else Color.Transparent, RoundedCornerShape(50))
            .clickable(enabled = !isMatched, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
