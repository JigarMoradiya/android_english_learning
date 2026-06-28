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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.main.base.nav.RouteNavigation
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.LetterRecognitionLetterSize
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.utils.extensions.scaled

// ── Data ─────────────────────────────────────────────────────────────────────

data class PhonicsLevelItem(
    val number: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val color: Color,
    val ageTag: String,
    val route: String,
    val isAvailable: Boolean,
)

val phonicsLevelItems = listOf(

    // ── Foundation (Age 3–4) ──────────────────────────────────────────────────
    PhonicsLevelItem(1,  "Letter Sounds",
        "Every letter has its own sound",
        "🔤", Color(0xFF2E7D32), "Age 3-4", RouteNavigation.LetterPhonicsSoundRoute.route, true),
    PhonicsLevelItem(2,  "Short Vowels",
        "/a/ ant · /e/ egg\n/i/ ink · /o/ ox · /u/ up",
        "🍎", Color(0xFFE64A19), "Age 3-4", RouteNavigation.ShortVowelsIntro.route, true),

    // ── Early Blending (Age 4–5) ──────────────────────────────────────────────
    PhonicsLevelItem(3,  "2-Sound Blending",
        "VC: at, an, in, up\nCV: ba, ma, si, do",
        "🔗", Color(0xFF1565C0), "Age 4-5", RouteNavigation.BlendingIntro.route, true),
    PhonicsLevelItem(4,  "CVC Words",
        "cat, dog, pig, sit, bug",
        "🐱", Color(0xFFAD1457), "Age 4-5", RouteNavigation.CvcWordsIntro.route, true),
    PhonicsLevelItem(5,  "Short Vowel Spelling Rules",
        "-ff/-ll/-ss/-zz off, bell, miss\n-ck duck, back · -ng ring, song",
        "📏", Color(0xFF00695C), "Age 4-5", RouteNavigation.ShortVowelRulesIntro.route, true),

    // ── Word Patterns (Age 5–6) ───────────────────────────────────────────────
    PhonicsLevelItem(6,  "Word Families",
        "-at bat, cat · -en hen, ten\n-ig big, pig · -og log, dog\n-un sun, run",
        "👨‍👩‍👧", Color(0xFFE91E63), "Age 5-6", RouteNavigation.WordFamiliesIntro.route, true),

    // ── Consonant Blends & Digraphs (Age 6–7) ────────────────────────────────
    PhonicsLevelItem(7,  "Beginning Blends",
        "bl, cl, fl, pl, sl\nbr, cr, dr, fr, gr, tr\nsm, sn, sp, st, sw",
        "🌟", Color(0xFFD32F2F), "Age 6-7", RouteNavigation.BeginningBlendsIntro.route, true),
    PhonicsLevelItem(8,  "Ending Blends",
        "nd hand · nt tent\nmp lamp · lk milk\nsk desk · ft left",
        "🎯", Color(0xFF4527A0), "Age 6-7", RouteNavigation.EndingBlendsIntro.route, true),
    PhonicsLevelItem(9,  "Digraphs",
        "ch chip · sh ship\nth thin · wh whip\nph graph · qu quiz",
        "🔊", Color(0xFF0277BD), "Age 6-7", RouteNavigation.DigraphsIntro.route, true),
    PhonicsLevelItem(10, "Special Endings",
        "-tch catch, watch\n-dge bridge, fudge\n-nk sink, tank, honk",
        "🔚", Color(0xFF558B2F), "Age 6-7", RouteNavigation.SpecialEndingsIntro.route, true),

    // ── Long Vowels (Age 6–7) ─────────────────────────────────────────────────
    PhonicsLevelItem(11, "Open Syllable",
        "me, he, go, no, she, be · vowel at end = long sound",
        "🔓", Color(0xFF6A1B9A), "Age 6-7", RouteNavigation.OpenSyllableIntro.route, true),
    PhonicsLevelItem(12, "Magic E",
        "cap→cape · hop→hope · bit→bite · cub→cube · pet→Pete",
        "✨", Color(0xFF880E4F), "Age 6-7", RouteNavigation.MagicEIntro.route, true),
    PhonicsLevelItem(13, "Vowel Teams",
        "ai/ay rain, day · ee/ea feet, read · oa/ow boat, snow",
        "🤝", Color(0xFFEF6C00), "Age 6-7", RouteNavigation.VowelTeamsIntro.route, true),
    PhonicsLevelItem(14, "Diphthongs",
        "oi/oy coin, boy · ou/ow cloud, cow · au/aw pause, saw",
        "🔄", Color(0xFFE65100), "Age 6-7", RouteNavigation.DiphthongsIntro.route, true),
    PhonicsLevelItem(15, "R-Controlled Vowels",
        "ar car · or fork · er her · ir bird · ur burn",
        "🌀", Color(0xFF2E7D32), "Age 6-7", RouteNavigation.RControlledIntro.route, true),
    PhonicsLevelItem(16, "igh & gh Patterns",
        "igh night, light, fight · gh=/f/: enough, laugh",
        "🌙", Color(0xFFF57F17), "Age 6-7", "", false),

    // ── Advanced Patterns (Age 7+) ────────────────────────────────────────────
    PhonicsLevelItem(17, "Y as a Vowel",
        "fly, sky = /i/ · happy, baby = /e/ · gym, myth = /i/",
        "🦋", Color(0xFF0097A7), "Age 7+", "", false),
    PhonicsLevelItem(18, "3-Letter Blends",
        "str strong · spl splash · spr spring · thr three · scr scream",
        "💪", Color(0xFFF9A825), "Age 7+", "", false),
    PhonicsLevelItem(19, "Soft C & Soft G",
        "c+e/i/y = /s/ city, ice · g+e/i/y = /j/ gem, giraffe, gym",
        "🎭", Color(0xFFBF360C), "Age 7+", "", false),
    PhonicsLevelItem(20, "Silent Letters",
        "kn knife, know · wr write, wrist · mb lamb, climb · gn gnat, sign",
        "🤫", Color(0xFF37474F), "Age 7+", "", false),

    // ── Word Building (Age 7+) ────────────────────────────────────────────────
    PhonicsLevelItem(21, "Word Endings",
        "-ing/-ed/-er/-est · double: run→running · drop-e: make→making",
        "🔧", Color(0xFF283593), "Age 7+", "", false),
    PhonicsLevelItem(22, "Prefixes",
        "un- unhappy · re- redo · pre- preview · dis- disagree · mis- mistake",
        "⬅️", Color(0xFF00838F), "Age 7+", "", false),
    PhonicsLevelItem(23, "Suffixes",
        "-ful helpful · -less careless · -ness kindness · -tion/-sion nation",
        "➡️", Color(0xFF33691E), "Age 7+", "", false),
    PhonicsLevelItem(24, "Contractions",
        "do+not=don't · can+not=can't · I+am=I'm · it+is=it's",
        "🤏", Color(0xFF6D4C41), "Age 7+", "", false),
    PhonicsLevelItem(25, "Consonant + -le",
        "ap-ple, lit-tle, ta-ble, pur-ple, bub-ble · final -e is silent",
        "🍏", Color(0xFFAD1457), "Age 7+", "", false),
    PhonicsLevelItem(26, "Compound Words",
        "sun+flower · rain+bow · bed+room · butter+fly",
        "🌈", Color(0xFF455A64), "Age 7+", "", false),
    PhonicsLevelItem(27, "Syllable Division",
        "VCCV rab-bit · VCV pi-lot · V/CV ti-ger · VC/V cam-el",
        "✂️", Color(0xFFE65100), "Age 7+", "", false),

    // ── Sight Words (All Ages) ────────────────────────────────────────────────
    PhonicsLevelItem(28, "Sight Words",
        "the, was, said, have, they, once, who, your, because, friend",
        "👁️", Color(0xFF5D4037), "Age 3+", "", false),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun PhonicsReadingLevelsPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.mintLime, shape = KidsFloatingShape.musicNotes)

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

                Card(
                    modifier = Modifier.padding(horizontal = Dimens16),
                    shape = RoundedCornerShape(Dimens24),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = Dimens8)
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
                                text = "15 / 28",
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
                columns = GridCells.Fixed(if (isTablet) 3 else 2),
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
private fun PhonicLevelCard(level: PhonicsLevelItem, onClick: () -> Unit) {
    val bgColor = if (level.isAvailable) level.color else level.color.copy(alpha = 0.15f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = if (level.isAvailable) Dimens4 else 0.dp, shape = RoundedCornerShape(Dimens20))
            .background(bgColor, RoundedCornerShape(Dimens20))
            .border(Dimens2, level.color, RoundedCornerShape(Dimens20))
            .clickable(onClick = onClick)
            .padding(Dimens12)
    ) {
        Column {
            // Top row: "Level X" badge + emoji
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Level ${level.number}",
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    fontWeight = FontWeight.Bold,
                    color = level.color,
                    modifier = Modifier
                        .background(
                            if (level.isAvailable) Color.White.copy(alpha = 0.85f)
                            else level.color.copy(alpha = 0.15f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = Dimens8, vertical = Dimens3)
                )

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
                color = if (level.isAvailable) Color.White else Color(0xFF212121),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(Dimens2))

            // Subtitle
            Text(
                text = level.subtitle,
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = if (level.isAvailable) Color.White.copy(alpha = 0.85f) else Color.Black.copy(alpha = 0.5f),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(Dimens8))

            // Age tag + status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = level.ageTag,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = if (level.isAvailable) Color.White.copy(alpha = 0.85f) else level.color,
                    modifier = Modifier
                        .background(
                            if (level.isAvailable) Color.White.copy(alpha = 0.20f)
                            else level.color.copy(alpha = 0.12f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = Dimens6, vertical = Dimens2)
                )

                if (level.isAvailable) {
                    Text(
                        text = "✓ Done",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(0xFF1B5E20), RoundedCornerShape(50))
                            .padding(horizontal = Dimens8, vertical = Dimens3)
                    )
                } else {
                    Text(
                        text = "Coming Soon",
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF424242),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(50))
                            .padding(horizontal = Dimens8, vertical = Dimens3)
                    )
                }
            }
        }
    }
}
