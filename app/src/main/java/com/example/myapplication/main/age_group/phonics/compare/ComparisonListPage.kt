package com.example.myapplication.main.age_group.phonics.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.kidsGlassCard
import com.example.myapplication.main.common.sheets.LocalAccessSheetViewModel
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens14
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.utils.AudioPlayerManager
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.launch

// ComparisonListPage.kt
// "Compare & Choose" hub — all 16 battles in two groups
// (📝 Which SPELLING? / 🔊 Which SOUND?), plus the fun-facts card.
// Keep identical to iOS ComparisonListView.

@Composable
fun ComparisonListPage(navController: NavController) {
    val accessVM = LocalAccessSheetViewModel.current
    val scope = rememberCoroutineScope()

    val spellingItems = remember { phonicsComparisons.filter { it.group == ComparisonGroup.spelling } }
    val soundItems = remember { phonicsComparisons.filter { it.group == ComparisonGroup.sound } }
    val columns = if (AppDimens.isTablet) 3 else 2

    val openComparison: (PhonicsComparison) -> Unit = { comparison ->
        AudioPlayerManager.playSoundMenuClick()
        // Free bonus content — same daily-limit rule as the free levels.
        scope.launch {
            val allowed = accessVM.checkAccess(ModuleID.PHONICS_READING)
            if (allowed) {
                navController.navigate(RouteNavigation.PhonicsComparison.createRoute(comparison.id))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.skyLavender, shape = KidsFloatingShape.sparkles)

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            BackButtonWithText(
                title = "Compare & Choose",
                onBackClick = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens20)
                    .padding(bottom = Dimens24),
                verticalArrangement = Arrangement.spacedBy(Dimens14),
            ) {

                // Tagline (the page title already lives in the back header)
                Text(
                    text = "🕵️ Same sound? Same letters? Learn which one wins!",
                    style = MaterialTheme.typography.labelMedium.scaled(),
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens8),
                )

                GroupSection(
                    title = "📝 Which SPELLING?",
                    subtitle = "Same sound — which letters win?",
                    items = spellingItems,
                    accent = Color(0xFF1565C0),
                    columns = columns,
                    onTap = openComparison,
                )

                GroupSection(
                    title = "🔊 Which SOUND?",
                    subtitle = "Same letters — which sound wins?",
                    items = soundItems,
                    accent = Color(0xFFE65100),
                    columns = columns,
                    onTap = openComparison,
                )

                // Did-you-know fun facts
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(0xFFE65100))
                        .padding(Dimens12),
                    verticalArrangement = Arrangement.spacedBy(Dimens4),
                ) {
                    Text(
                        text = "💡 Did you know?",
                        style = MaterialTheme.typography.titleSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100),
                    )
                    phonicsFunFacts.forEach { fact ->
                        Text(
                            text = "• $fact",
                            style = MaterialTheme.typography.labelMedium.scaled(),
                            color = Color(0xFF37474F),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupSection(
    title: String,
    subtitle: String,
    items: List<PhonicsComparison>,
    accent: Color,
    columns: Int,
    onTap: (PhonicsComparison) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens8)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = accent,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color(0xFF546E7A),
            )
        }

        items.chunked(columns).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens10)) {
                rowItems.forEach { comparison ->
                    Box(modifier = Modifier.weight(1f)) {
                        ComparisonPosterCard(comparison = comparison) { onTap(comparison) }
                    }
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ── Battle poster card ───────────────────────────────────────────────────────

@Composable
fun ComparisonPosterCard(comparison: PhonicsComparison, onTap: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .kidsGlassCard(cornerRadius = Dimens12, strokeColor = Color(comparison.teams[0].colorHex))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap,
            )
            .padding(vertical = Dimens10, horizontal = Dimens8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens6),
    ) {
        // Rivals with ⚡ between, each in its team color
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4),
        ) {
            comparison.teams.forEachIndexed { index, team ->
                if (index > 0) {
                    Text(
                        text = "⚡",
                        style = MaterialTheme.typography.titleSmall.scaled(),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // For same-spelling battles the emoji tells the teams apart
                    if (comparison.group == ComparisonGroup.sound) {
                        Text(
                            text = team.zoneEmoji,
                            style = MaterialTheme.typography.labelMedium.scaled(),
                        )
                        Spacer(modifier = Modifier.width(Dimens1))
                    }
                    Text(
                        text = team.marker,
                        style = MaterialTheme.typography.titleLarge.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = Color(team.colorHex),
                        maxLines = 1,
                    )
                }
            }
        }

        Text(
            text = comparison.example,
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color(0xFF546E7A),
            maxLines = 1,
        )
    }
}
