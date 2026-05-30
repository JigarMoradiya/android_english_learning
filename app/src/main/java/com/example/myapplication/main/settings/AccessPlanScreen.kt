package com.example.myapplication.main.settings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens18
import com.example.myapplication.ui.theme.AppDimens.Dimens3
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.ui.theme.getButtonColors
import com.example.myapplication.utils.extensions.scaled

// ── Data model ───────────────────────────────────────────────────────────────

private sealed class TierAccess {
    object Full : TierAccess()
    object LoginRequired : TierAccess()
    object Premium : TierAccess()
}

private data class PlanRow(
    val icon: ImageVector,
    val title: String,
    val guest: TierAccess,
    val free: TierAccess,
    // premium is always Full
)

private data class PlanSection(
    val icon: ImageVector,
    val title: String,
    val type: ButtonType,
    val rows: List<PlanRow>,
)

private val planSections = listOf(
    PlanSection(
        icon = Icons.Filled.Stars,
        title = "Ages 3-5 · Little Explorers",
        type = ButtonType.ORANGE,
        rows = listOf(
            PlanRow(Icons.Filled.Edit,                       "Alphabet Tracing (A-J)",            TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.EditOff,                    "Alphabet Tracing (K-Z)",            TierAccess.LoginRequired, TierAccess.Full),
            PlanRow(Icons.Filled.Headphones,                 "Letter Phonics Sound (A-J)",        TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.AutoMirrored.Filled.VolumeUp,      "Letter Phonics Sound (K-Z)",        TierAccess.LoginRequired, TierAccess.Full),
            PlanRow(Icons.Filled.TextFormat,                 "Letter Recognition",                TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.TextFields,                 "ABCD with Images",                  TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Palette,                    "Colouring Alphabets",               TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.SwapVert,                   "Match Letter (Uppercase → Lowercase)", TierAccess.Full,       TierAccess.Full),
            PlanRow(Icons.Filled.CropSquare,                 "Fill the Blank Letter",             TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.AutoMirrored.Filled.Sort,          "Arrange Letter Sequence",           TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.Image,                      "Match Letter with Image",           TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.AutoMirrored.Filled.Help,          "Missing Letter",                    TierAccess.Premium,       TierAccess.Premium),
        )
    ),
    PlanSection(
        icon = Icons.AutoMirrored.Filled.MenuBook,
        title = "Ages 5-7 · Word Adventure",
        type = ButtonType.BLUE,
        rows = listOf(
            PlanRow(Icons.Filled.Interests,                  "Vocabulary - Shapes",               TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Palette,                    "Vocabulary - Colors",               TierAccess.LoginRequired, TierAccess.Full),
            PlanRow(Icons.Filled.Pets,                       "Vocabulary - Others",               TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.SwapHoriz,                  "Opposite Words",                    TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.LooksOne,                   "Singular / Plural",                 TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.PhotoAlbum,                 "Match Word with Picture",           TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Headphones,                 "Listen & Select Answer",            TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.AutoMirrored.Filled.HelpOutline,   "Missing Letter",                    TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.Extension,                  "Word Jigsaw",                       TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.AutoMirrored.Filled.Article,       "Articles A / An",                   TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Visibility,                 "Sight Words",                       TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Description,                "Articles Choice",                   TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.RemoveRedEye,               "Sight Word Choice",                 TierAccess.Full,          TierAccess.Full),
        )
    ),
    PlanSection(
        icon = Icons.AutoMirrored.Filled.Assignment,
        title = "Ages 6-8 · Sentence Builder",
        type = ButtonType.GREEN,
        rows = listOf(
            PlanRow(Icons.Filled.Book,                       "Read & Listen (Unit 1)",            TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.AutoMirrored.Filled.Chat,          "One Word Answer (Unit 1)",          TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.BorderColor,                "Fill the Missing Word (Unit 1)",    TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Search,                     "Sentence Check (True/False)",          TierAccess.Premium,    TierAccess.Premium),
            PlanRow(Icons.Filled.Photo,                      "Match the Picture",                 TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.Adjust,                     "Which Sentence is Right?",          TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.Create,                     "Find Correct Writing",              TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.Build,                      "Build the Sentence",                TierAccess.Premium,       TierAccess.Premium),
            PlanRow(Icons.Filled.Spellcheck,                 "Grammar - Nouns",                   TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.Star,                       "Grammar - Verbs, Adjectives, Pronouns", TierAccess.Premium,   TierAccess.Premium),
            PlanRow(Icons.Filled.School,                     "Grammar Challenge (Beginner)",      TierAccess.Full,          TierAccess.Full),
            PlanRow(Icons.Filled.EmojiEvents,                "Grammar Challenge (Medium & Advanced)", TierAccess.Premium,   TierAccess.Premium),
        )
    )
)

// ── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun AccessPlanScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.grayBlue, shape = KidsFloatingShape.dots)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // ── Back button ──────────────────────────────────────────────
            BackButtonWithText(
                title = "Access Plan",
                onBackClick = { navController.popBackStack() }
            )

            // ── Scrollable content ───────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens16)
                    .padding(bottom = Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                Spacer(Modifier.height(Dimens8))
                planSections.forEach { section ->
                    SectionBlock(section)
                }
            }
        }
    }
}

// ── Section block ─────────────────────────────────────────────────────────────

@Composable
private fun SectionBlock(section: PlanSection) {
    val colors = getButtonColors(section.type)
    val tierColWidth = AppDimens.PlanTierColWidth

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
    ) {
        // Header — title left, tier labels right
        // padding(horizontal = Dimens12) so tier labels align with badges below
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = Dimens12, topEnd = Dimens12))
                .background(brush = colors.gradient)
                .padding(horizontal = Dimens12, vertical = Dimens4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = section.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens18)
            )
            Spacer(Modifier.width(Dimens8))
            Text(
                text = section.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge.scaled(),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(Dimens8))
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens4)) {
                TierLabel(icon = Icons.Filled.AccountCircle, text = "Guest",   subtitle = "1 activity/day",   colWidth = tierColWidth)
                TierLabel(icon = Icons.Filled.Person,        text = "Free",    subtitle = "3 activities/day", colWidth = tierColWidth)
                TierLabel(icon = Icons.Filled.Star,          text = "Premium", subtitle = "Unlimited",         colWidth = tierColWidth)
            }
        }

        // Rows — same horizontal = Dimens12 so badges stay aligned with header labels
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = Dimens12, bottomEnd = Dimens12))
                .background(Color.White.copy(alpha = 0.92f))
        ) {
            section.rows.forEachIndexed { idx, row ->
                PlanRowItem(row, isEven = idx % 2 == 0, tierColWidth = tierColWidth)
                if (idx < section.rows.size - 1) {
                    HorizontalDivider(Modifier, thickness = Dimens1, color = Color.Gray.copy(alpha = 0.2f))
                }
            }
        }
    }
}

// ── Tier label (inside section header) ───────────────────────────────────────

@Composable
private fun TierLabel(icon: ImageVector, text: String, subtitle: String, colWidth: androidx.compose.ui.unit.Dp) {
    Column(
        modifier = Modifier
            .width(colWidth)
            .clip(RoundedCornerShape(Dimens8))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(top = Dimens3, bottom = Dimens3, start = Dimens4, end = Dimens4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens1)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(Dimens12)
        )
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall.scaled(),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = subtitle,
            color = Color.White.copy(alpha = 0.75f),
            style = MaterialTheme.typography.labelSmall.scaled(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── Plan row ──────────────────────────────────────────────────────────────────

@Composable
private fun PlanRowItem(row: PlanRow, isEven: Boolean, tierColWidth: androidx.compose.ui.unit.Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isEven) Color.Transparent else Color.Gray.copy(alpha = 0.04f))
            // Same horizontal = Dimens12 as the header → columns align perfectly
            .padding(horizontal = Dimens12, vertical = Dimens6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = row.icon,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.55f),
                modifier = Modifier.size(Dimens16)
            )
            Spacer(Modifier.width(Dimens6))
            Text(
                text = row.title,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.85f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(Dimens4))
        AccessBadge(row.guest,       Modifier.width(tierColWidth))
        Spacer(Modifier.width(Dimens4))
        AccessBadge(row.free,        Modifier.width(tierColWidth))
        Spacer(Modifier.width(Dimens4))
        AccessBadge(TierAccess.Full, Modifier.width(tierColWidth))
    }
}

// ── Access badge (horizontal) ─────────────────────────────────────────────────

@Composable
private fun AccessBadge(access: TierAccess, modifier: Modifier = Modifier) {
    val (icon, label, bg, fg) = when (access) {
        TierAccess.Full          -> Quad(Icons.Filled.CheckCircle, "Full",    Color(0xFFE8F5E9), Color(0xFF2E7D32))
        TierAccess.LoginRequired -> Quad(Icons.Filled.Lock,        "Login",   Color(0xFFE3F2FD), Color(0xFF1565C0))
        TierAccess.Premium       -> Quad(Icons.Filled.Star,        "Premium", Color(0xFFFFF8E1), Color(0xFFF57F17))
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens8))
            .background(bg)
            .padding(horizontal = Dimens4, vertical = Dimens4),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(Dimens12)
        )
        Spacer(Modifier.width(Dimens3))
        Text(
            text = label,
            color = fg,
            style = MaterialTheme.typography.labelMedium.scaled(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
