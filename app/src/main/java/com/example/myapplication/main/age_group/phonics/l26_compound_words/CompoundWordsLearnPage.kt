package com.example.myapplication.main.age_group.phonics.l26_compound_words

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model.CWGroup
import com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model.CWLearnViewModel
import com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model.CWWord
import com.example.myapplication.main.age_group.phonics.l26_compound_words.view_model.cwGroups
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.utils.extensions.scaled

private val cwAccent = Color(0xFFF57C00)

@Composable
fun CompoundWordsLearnPage(
    navController: NavController,
    viewModel: CWLearnViewModel = hiltViewModel()
) {
    val uiState       = viewModel.uiState
    val selectedGroup = viewModel.selectedGroup

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.peachYellow, shape = KidsFloatingShape.speechBubbles)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT 30% ──────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(0.30f).fillMaxHeight()) {
                BackButtonWithText(title = "Compound Words", onBackClick = { navController.popBackStack() })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens10)
                        .padding(top = Dimens8, bottom = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    Text(
                        text       = "Tap a word to snap it together!",
                        style      = MaterialTheme.typography.labelSmall.scaled(),
                        color      = Color(0xFF78909C),
                        modifier   = Modifier
                            .padding(bottom = Dimens4)
                            .padding(horizontal = Dimens4)
                    )
                    cwGroups.forEachIndexed { index, group ->
                        CWGroupButton(
                            group      = group,
                            isSelected = uiState.selectedGroupIndex == index,
                            onClick    = { viewModel.onGroupTap(index) }
                        )
                    }
                }
            }

            // ── RIGHT 70% ─────────────────────────────────────────────────────
            AnimatedContent(
                targetState    = selectedGroup,
                transitionSpec = { fadeIn(spring()) togetherWith fadeOut(spring()) },
                modifier       = Modifier.weight(1f).fillMaxHeight(),
                label          = "cwLearnContent"
            ) { group ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens14),
                    verticalArrangement = Arrangement.spacedBy(Dimens10)
                ) {
                    CWGroupHeader(group = group)
                    CWSnapRowList(
                        group        = group,
                        activeWord   = uiState.activeWordFull,
                        onWordTap    = { viewModel.onWordTap(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CWGroupButton(group: CWGroup, isSelected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg = if (isSelected)
        Modifier.background(Brush.linearGradient(listOf(group.accentColor, group.shadowColor)), RoundedCornerShape(12.dp))
    else
        Modifier.background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.30f) else group.accentColor.copy(alpha = 0.25f)

    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .then(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = Dimens8, horizontal = Dimens10)
    ) {
        Text(text = group.emoji, style = MaterialTheme.typography.bodyMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(
                text       = group.name,
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = if (isSelected) Color.White else group.accentColor
            )
            Text(
                text     = "${group.words.size} words",
                style    = MaterialTheme.typography.labelSmall.scaled(),
                color    = if (isSelected) Color.White.copy(alpha = 0.80f) else Color(0xFF78909C)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Text(
                text  = "›",
                style = MaterialTheme.typography.titleMedium.scaled(),
                color = Color.White.copy(alpha = 0.80f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CWGroupHeader(group: CWGroup) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens12),
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = group.accentColor)
            .padding(Dimens14)
    ) {
        Text(text = group.emoji, style = MaterialTheme.typography.headlineMedium.scaled())
        Column(verticalArrangement = Arrangement.spacedBy(Dimens2)) {
            Text(
                text       = group.name,
                style      = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.ExtraBold,
                color      = group.accentColor
            )
            Text(
                text  = "${group.words.size} compound words",
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF78909C)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text  = "Tap to snap! 🔗",
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color(0xFF90A4AE)
        )
    }
}

@Composable
private fun CWSnapRowList(
    group:      CWGroup,
    activeWord: String?,
    onWordTap:  (CWWord) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier.fillMaxWidth()
    ) {
        group.words.forEach { word ->
            CWSnapRow(
                word      = word,
                group     = group,
                isActive  = activeWord == word.full,
                onClick   = { onWordTap(word) }
            )
        }
    }
}

@Composable
private fun CWSnapRow(
    word:    CWWord,
    group:   CWGroup,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue   = if (isActive) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label         = "cwSnapScale"
    )
    val strokeColor = if (isActive) group.accentColor else Color(0xFFB0BEC5)

    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .kidsGlassCard(cornerRadius = 12.dp, strokeColor = strokeColor)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = Dimens16, vertical = Dimens12)
    ) {
        if (isActive) {
            // Merged capsule
            Row(
                modifier = Modifier
                    .background(group.accentColor.copy(alpha = 0.12f), RoundedCornerShape(50.dp))
                    .border(1.5.dp, group.accentColor.copy(alpha = 0.35f), RoundedCornerShape(50.dp))
                    .padding(horizontal = Dimens12, vertical = Dimens6),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(
                    text       = word.part1,
                    style      = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = group.accentColor
                )
                Text(
                    text       = word.part2,
                    style      = MaterialTheme.typography.titleMedium.scaled(),
                    fontWeight = FontWeight.ExtraBold,
                    color      = group.accentColor.copy(alpha = 0.65f)
                )
            }
            Text(text = word.emoji, style = MaterialTheme.typography.titleMedium.scaled())
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector        = Icons.Default.CheckCircle,
                contentDescription = null,
                tint               = group.accentColor,
                modifier           = Modifier.size(20.dp)
            )
        } else {
            // Split chips
            PartChip(text = word.part1)
            Text(
                text       = "+",
                style      = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF90A4AE)
            )
            PartChip(text = word.part2)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text     = word.emoji,
                style    = MaterialTheme.typography.titleMedium.scaled(),
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

@Composable
private fun PartChip(text: String) {
    Text(
        text       = text,
        style      = MaterialTheme.typography.titleSmall.scaled(),
        fontWeight = FontWeight.Bold,
        color      = Color(0xFF546E7A),
        modifier   = Modifier
            .background(Color(0xFFECEFF1), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFFB0BEC5), RoundedCornerShape(6.dp))
            .padding(horizontal = Dimens10, vertical = Dimens4)
    )
}
