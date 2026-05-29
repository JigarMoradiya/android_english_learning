package com.example.myapplication.main.age_group.from_3_to_5.fill_blank

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.LastPage
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.FirstPage
import androidx.compose.material.icons.rounded.LastPage
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.LetterMode
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.extensions.scaled
import androidx.core.graphics.toColorInt

@Composable
fun FillBlankSetupScreen(navController: NavController) {

    var selectedPosition by rememberSaveable { mutableStateOf(BlankPosition.FIRST) }
    var selectedMode     by rememberSaveable { mutableStateOf(LetterMode.UPPERCASE) }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.purpleBlue, shape = KidsFloatingShape.stars)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header — BackButtonWithText manages its own horizontal padding
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.fill_the_blank),
                    onBackClick = { navController.popBackStack() }
                )
            }

            Spacer(Modifier.height(Dimens8))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens20),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SectionLabel(stringResource(R.string.fill_blank_where_blank))
                Spacer(Modifier.height(Dimens8))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    BlankPosition.entries.forEach { position ->
                        PositionCard(
                            position = position,
                            isSelected = selectedPosition == position,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPosition = position }
                        )
                    }
                }

                Spacer(Modifier.height(Dimens16))

                SectionLabel(stringResource(R.string.fill_blank_letter_style))
                Spacer(Modifier.height(Dimens8))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens12)
                ) {
                    LetterMode.entries.forEach { mode ->
                        CaseCard(
                            mode = mode,
                            isSelected = selectedMode == mode,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedMode = mode }
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            KidsActionButton(
                text = stringResource(R.string.lets_go),
                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                type = ButtonType.BLUE,
                isSmall = true,
                isIconStart = false,
                onClick = {
                    navController.navigate(
                        RouteNavigation.FillTheBlankLettersPlay.createRoute(
                            position = selectedPosition.name,
                            mode = selectedMode.name
                        )
                    )
                }
            )

            Spacer(Modifier.weight(1f))
        }
    }
}

// ── Helpers ──────────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.scaled(),
        fontWeight = FontWeight.Bold,
        color = Color(0xFF4A2B8C),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun BlankPosition.icon(): ImageVector = when (this) {
    BlankPosition.FIRST  -> Icons.Rounded.FirstPage
    BlankPosition.MIDDLE -> Icons.Rounded.SwapHoriz
    BlankPosition.LAST   -> Icons.AutoMirrored.Rounded.LastPage
    BlankPosition.RANDOM -> Icons.Rounded.Shuffle
}

@Composable
private fun PositionCard(
    position: BlankPosition,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val accent = Color(position.accentHex.toColorInt())
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) accent else Color.White,
        animationSpec = spring(), label = "posBg"
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(Dimens12),
        color = bgColor,
        shadowElevation = if (isSelected) Dimens8 else Dimens4,
        tonalElevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens8),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens12)
        ) {
            Icon(
                imageVector = position.icon(),
                contentDescription = position.name,
                tint = if (isSelected) Color.White else accent,
                modifier = Modifier.size(Dimens24)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
                position.preview.forEach { slot ->
                    MiniLetterBox(
                        letter = slot,
                        isRandom = position == BlankPosition.RANDOM,
                        isSelected = isSelected,
                        accent = accent
                    )
                }
            }

            Text(
                text = stringResource(position.displayNameRes),
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MiniLetterBox(
    letter: String?,
    isRandom: Boolean,
    isSelected: Boolean,
    accent: Color
) {
    val isBlank = letter == null
    val boxBg = when {
        isBlank    -> if (isSelected) Color.White.copy(0.22f) else Color.Gray.copy(0.12f)
        else       -> if (isSelected) Color.White.copy(0.18f) else accent.copy(0.12f)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(Dimens24)
            .background(boxBg, RoundedCornerShape(Dimens4))
            .then(
                if (isBlank) Modifier.border(
                    width = 1.5.dp,
                    color = if (isSelected) Color.White.copy(0.5f) else Color.Gray.copy(0.35f),
                    shape = RoundedCornerShape(Dimens4)
                ) else Modifier
            )
    ) {
        val displayText = when {
            isBlank  -> ""
            isRandom -> "?"
            else     -> letter
        }
        if (displayText.isNotEmpty()) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Black,
                color = when {
                    isRandom -> if (isSelected) Color.White.copy(0.6f) else accent.copy(0.5f)
                    else     -> if (isSelected) Color.White else accent
                }
            )
        }
    }
}

@Composable
fun CaseCard(
    mode: LetterMode,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val color = Color(0xFF5532D2)
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) color else Color.White,
        animationSpec = spring(), label = "caseBg"
    )
    val letters   = if (mode == LetterMode.UPPERCASE) "A B C" else "a b c"
    val sublabel  = if (mode == LetterMode.UPPERCASE) stringResource(R.string.fill_blank_uppercase)
                   else stringResource(R.string.fill_blank_lowercase)

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(Dimens12),
        color = bgColor,
        shadowElevation = if (isSelected) Dimens8 else Dimens4,
        tonalElevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens8, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens8, horizontal = Dimens12)
        ) {
            Text(
                text = letters,
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Black,
                color = if (isSelected) Color.White else color
            )
            Text(
                text = sublabel,
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = if (isSelected) Color.White.copy(0.85f) else Color.Gray
            )
        }
    }
}
