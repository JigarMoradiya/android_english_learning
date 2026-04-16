package com.example.myapplication.main.age_group.from_5_to_7.sight_words.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_5_to_7.sight_words.view_model.SightWordsViewModel
import com.example.myapplication.main.common.buttons.KidsIconButton
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.SightWordFont
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.PrimaryBlue
import com.example.myapplication.utils.extensions.scaled

@Composable
fun SightWordsScreen(
    viewModel: SightWordsViewModel,
    modifier: Modifier
) {

    val currentWord = viewModel.currentWord

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens16),
        horizontalArrangement = Arrangement.spacedBy(Dimens24)
    ) {

        // 🔹 LEFT SIDE
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(Dimens20),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = currentWord.word,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = SightWordFont
                ),
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            KidsIconButton(
                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                onClick = { viewModel.speak(currentWord.word) },
                type = ButtonType.BLUE
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // 🔹 RIGHT SIDE
        LazyColumn(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(Dimens12)
        ) {

            items(currentWord.useCases) { useCase ->

                UseCaseCard(
                    description = useCase.description,
                    example = useCase.example,
                    currentWord = currentWord.word,
                    onSpeak = { viewModel.speak(it) }
                )
            }
        }
    }
}