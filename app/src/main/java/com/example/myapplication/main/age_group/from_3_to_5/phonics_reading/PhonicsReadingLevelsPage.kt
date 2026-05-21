package com.example.myapplication.main.age_group.from_3_to_5.phonics_reading

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.LetterRecognitionLetterSize
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.utils.extensions.scaled
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground

// ── Data ─────────────────────────────────────────────────────────────────────

data class PhonicsLevelItem(
    val number: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val color: Color,
    val route: String,
    val isAvailable: Boolean,
)

val phonicsLevelItems = listOf(
    PhonicsLevelItem(1,  "Letter Sounds",      "A says /a/, B says /b/",  "🔤", Color(0xFF2E7D32), RouteNavigation.LetterPhonicsSoundRoute.route, true),
    PhonicsLevelItem(2,  "Short Vowels",       "apple, egg, igloo",        "🍎", Color(0xFFE64A19), "",  false),
    PhonicsLevelItem(3,  "Blend 2 Sounds",     "at, ma, su",               "🧩", Color(0xFF1565C0), "",  false),
    PhonicsLevelItem(4,  "CVC Words",          "cat, dog, pig",            "🐱", Color(0xFFAD1457), "",  false),
    PhonicsLevelItem(5,  "Word Families",      "-at, -an, -og",            "👪", Color(0xFFE91E63), "",  false),
    PhonicsLevelItem(6,  "Beginning Blends",   "bl, cr, st, tr",           "🌟", Color(0xFFD32F2F), "",  false),
    PhonicsLevelItem(7,  "Ending Blends",      "nd, nt, st, mp",           "🎯", Color(0xFF4527A0), "",  false),
    PhonicsLevelItem(8,  "3-Letter Blends",    "str, spl, thr",            "💪", Color(0xFFF9A825), "",  false),
    PhonicsLevelItem(9,  "Digraphs",           "ch, sh, th, wh",           "🔊", Color(0xFF0277BD), "",  false),
    PhonicsLevelItem(10, "-tch / -dge",        "catch, bridge",            "🎨", Color(0xFF7E57C2), "",  false),
    PhonicsLevelItem(11, "Magic E",            "cap→cape, hop→hope",       "✨", Color(0xFF880E4F), "",  false),
    PhonicsLevelItem(12, "Vowel Teams",        "ai, ee, oa, ea",           "🤝", Color(0xFFEF6C00), "",  false),
    PhonicsLevelItem(13, "R-Controlled",       "car, fork, bird",          "🌀", Color(0xFF283593), "",  false),
    PhonicsLevelItem(14, "Diphthongs",         "coin, house, cow",         "🔄", Color(0xFF6D4C41), "",  false),
    PhonicsLevelItem(15, "Y as a Vowel",       "fly, happy, gym",          "🦋", Color(0xFF0097A7), "",  false),
    PhonicsLevelItem(16, "Soft C / G",         "city, gem",                "🎭", Color(0xFFBF360C), "",  false),
    PhonicsLevelItem(17, "Silent Letters",     "knife, write, lamb",       "🤫", Color(0xFF6A1B9A), "",  false),
    PhonicsLevelItem(18, "Inflected Endings",  "-ing, -ed, -er",           "🔧", Color(0xFF1A237E), "",  false),
    PhonicsLevelItem(19, "Compound Words",     "sunflower, rainbow",       "🌈", Color(0xFF455A64), "",  false),
    PhonicsLevelItem(20, "Syllable Division",  "rab-bit, but-ter-fly",     "✂️", Color(0xFFF57F17), "",  false),
    PhonicsLevelItem(21, "Sight Words",        "the, was, said",           "👁️", Color(0xFF5D4037), "",  false),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun PhonicsReadingLevelsPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.aquaGreen, shape = KidsFloatingShape.leaves)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize()
        ) {
            // ── LEFT PANEL ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.phonics_reading),
                    onBackClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Decorative info card
                Card(
                    modifier = Modifier
                        .padding(horizontal = Dimens16),
                    shape = RoundedCornerShape(Dimens24),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.90f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens24),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📖", fontSize = LetterRecognitionLetterSize)

                        Spacer(modifier = Modifier.height(Dimens12))

                        Text(
                            text = "Phonics Journey",
                            style = MaterialTheme.typography.titleMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )

                        Spacer(modifier = Modifier.height(Dimens8))

                        Text(
                            text = "Complete word reading\nfrom sounds to sentences",
                            style = MaterialTheme.typography.bodySmall.scaled(),
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(Dimens16))

                        // Progress indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens6)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(Dimens20)
                            )
                            Text(
                                text = "1 / 21",
                                style = MaterialTheme.typography.titleLarge.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Text(
                            text = "Levels Completed",
                            style = MaterialTheme.typography.labelSmall.scaled(),
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            // ── RIGHT PANEL ─────────────────────────────────────────────────
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isTablet) 4 else 3),
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Dimens12),
                horizontalArrangement = Arrangement.spacedBy(Dimens12),
                contentPadding = PaddingValues(
                    start = Dimens16,
                    end = Dimens16,
                    bottom = Dimens24,
                    top = Dimens8
                )
            ) {
                items(phonicsLevelItems) { level ->
                    PhonicLevelCard(
                        level = level,
                        onClick = {
                            if (level.isAvailable && level.route.isNotEmpty()) {
                                navController.navigate(level.route)
                            }
                        }
                    )
                }
            }
        }
    }
}

// ── Level Card ────────────────────────────────────────────────────────────────

@Composable
private fun PhonicLevelCard(
    level: PhonicsLevelItem,
    onClick: () -> Unit,
) {
    val bgColor = if (level.isAvailable) level.color else level.color.copy(alpha = 0.15f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens20))
            .background(bgColor)
            .border(Dimens2, level.color, RoundedCornerShape(Dimens20))
            .clickable(onClick = onClick)
            .padding(Dimens12)
    ) {
        Column {
            // Top row: level number + emoji
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Level number circle
                Box(
                    modifier = Modifier
                        .size(Dimens28)
                        .clip(CircleShape)
                        .background(if (level.isAvailable) Color.White.copy(alpha = 0.85f) else level.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.number.toString(),
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.Bold,
                        color = if (level.isAvailable) level.color else Color.White
                    )
                }

                Text(
                    text = level.emoji,
                    style = MaterialTheme.typography.headlineSmall.scaled()
                )
            }

            Spacer(modifier = Modifier.height(Dimens6))

            // Title
            Text(
                text = level.title,
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = if (level.isAvailable) Color.White else Color(0xFF212121)
            )

            // Subtitle
            Text(
                text = level.subtitle,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (level.isAvailable) Color.White.copy(alpha = 0.85f) else Color.Gray
            )

            Spacer(modifier = Modifier.height(Dimens8))

            // Status badge
            if (level.isAvailable) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens12))
                        .background(Color(0xFF1B5E20))
                        .padding(horizontal = Dimens8, vertical = Dimens3)
                ) {
                    Text(
                        text = "✓ Done",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens12))
                        .background(Color(0xFFBDBDBD))
                        .padding(horizontal = Dimens8, vertical = Dimens3)
                ) {
                    Text(
                        text = "Coming Soon",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF424242)
                    )
                }
            }
        }
    }
}
