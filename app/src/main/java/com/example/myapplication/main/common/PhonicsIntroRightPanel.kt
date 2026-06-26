package com.example.myapplication.main.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.myapplication.R
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

data class PhonicsIntroBtnConfig(
    val text: String,
    val icon: ImageVector,
    val type: ButtonType,
    val onClick: () -> Unit
)

@Composable
fun PhonicsIntroRightPanel(
    screenHeight: Dp,
    strokeColor: Color,
    title: String,
    titleColor: Color,
    descLine1: String,
    descLine2: String = "",
    learnButton: PhonicsIntroBtnConfig,
    practiceButton: PhonicsIntroBtnConfig? = null,
    listenButton: PhonicsIntroBtnConfig? = null,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens20)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens20),
            modifier = Modifier
                .kidsGlassCard(cornerRadius = Dimens20, strokeColor = strokeColor)
                .padding(Dimens24)
        ) {
            Image(
                painter = painterResource(R.drawable._mascot_),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.28f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = descLine1,
                    style = MaterialTheme.typography.bodyMedium.scaled(),
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    softWrap = true,
                    overflow = TextOverflow.Visible
                )
                if (descLine2.isNotEmpty()) {
                    Text(
                        text = descLine2,
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        overflow = TextOverflow.Visible
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens10)
            ) {
                KidsActionButton(
                    text = learnButton.text,
                    icon = learnButton.icon,
                    type = learnButton.type,
                    isIconStart = false,
                    isSmall = true,
                    onClick = learnButton.onClick
                )
                if (practiceButton != null && listenButton != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens10)) {
                        KidsActionButton(
                            text = practiceButton.text,
                            icon = practiceButton.icon,
                            type = practiceButton.type,
                            isIconStart = true,
                            isSmall = true,
                            modifier = Modifier.weight(1f),
                            onClick = practiceButton.onClick
                        )
                        KidsActionButton(
                            text = listenButton.text,
                            icon = listenButton.icon,
                            type = listenButton.type,
                            isIconStart = true,
                            isSmall = true,
                            modifier = Modifier.weight(1f),
                            onClick = listenButton.onClick
                        )
                    }
                } else {
                    (practiceButton ?: listenButton)?.let { btn ->
                        KidsActionButton(
                            text = btn.text,
                            icon = btn.icon,
                            type = btn.type,
                            isIconStart = true,
                            isSmall = true,
                            onClick = btn.onClick
                        )
                    }
                }
            }
        }
    }
}
