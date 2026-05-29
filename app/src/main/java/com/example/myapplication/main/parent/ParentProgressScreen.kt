package com.example.myapplication.main.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.access.ModuleID
import com.example.myapplication.data.model.DeviceInfo
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.main.common.buttons.KidsActionButton
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens20
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.Dimens28
import com.example.myapplication.ui.theme.AppDimens.Dimens30
import com.example.myapplication.ui.theme.ButtonType
import com.example.myapplication.utils.extensions.scaled

@Composable
fun ParentProgressScreen(
    navController: NavController,
    viewModel: ParentProgressViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.load() }

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.grayBlue, shape = KidsFloatingShape.dots)

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(Dimens16)
        ) {
            // LEFT PANEL (35%)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.35f),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                BackButtonWithText(
                    title = stringResource(R.string.parent_report),
                    onBackClick = { navController.popBackStack() }
                )
                StreakCard(viewModel)
                WeekDotsCard(viewModel)
            }

            // RIGHT PANEL (65%)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = DeviceInfo.screenTopPadding() + Dimens8, end = Dimens16)
                    .weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                WeekSummaryRow(viewModel)
                ActivitySection(viewModel, navController)
            }
        }

        viewModel.chapterDetail?.let { detail ->
            ChapterDetailSheet(
                detail = detail,
                onClose = { viewModel.closeChapterDetail() },
                onPractice = detail.route?.let { route ->
                    {
                        viewModel.closeChapterDetail()
                        navController.navigate(route)
                    }
                },
                selectedAgeFilter = viewModel.selectedAgeFilter
            )
        }
    }
}

// ── Left panel ───────────────────────────────────────────────────────────────

@Composable
private fun StreakCard(vm: ParentProgressViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens16)
            .shadow(Dimens8, RoundedCornerShape(Dimens16))
            .background(
                Brush.linearGradient(listOf(Color(0xFF9374EF), Color(0xFF5532D2))),
                RoundedCornerShape(Dimens16)
            )
            .padding(Dimens16)
    ) {
        Text("🔥", style = MaterialTheme.typography.displaySmall.scaled())
        Text(
            text = "${vm.currentStreak}",
            style = MaterialTheme.typography.displayMedium.scaled(),
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Text(
            text = "day streak",
            style = MaterialTheme.typography.titleSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.85f)
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = Dimens8),
            color = Color.White.copy(alpha = 0.3f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens4)
        ) {
            Text("🏆", style = MaterialTheme.typography.labelLarge.scaled())
            Text(
                text = "Best: ${vm.bestStreak} days",
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun WeekDotsCard(vm: ParentProgressViewModel) {
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens16)
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
            .background(Color.White, RoundedCornerShape(Dimens12))
            .padding(Dimens12)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { vm.goToPreviousWeek() },
                enabled = vm.canGoBack,
                modifier = Modifier.size(Dimens28)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = if (vm.canGoBack) Color(0xFF5532D2) else Color.Gray.copy(alpha = 0.3f)
                )
            }
            Text(
                text = vm.weekLabel,
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { vm.goToNextWeek() },
                enabled = vm.canGoForward,
                modifier = Modifier.size(Dimens28)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = if (vm.canGoForward) Color(0xFF5532D2) else Color.Gray.copy(alpha = 0.3f)
                )
            }
        }

        Spacer(Modifier.height(Dimens8))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            dayLabels.forEachIndexed { i, label ->
                val active = vm.activeDays.getOrElse(i) { false }
                val selected = vm.selectedDayIndex == i
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { vm.selectDay(i) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens24)
                            .then(
                                if (selected) Modifier.shadow(Dimens4, CircleShape) else Modifier
                            )
                            .background(
                                when {
                                    selected -> Color(0xFF5532D2)
                                    active   -> Color(0xFF9374EF)
                                    else     -> Color(0xFFD5D5D5)
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (active || selected) {
                            Text(
                                "✓",
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        color = if (selected) Color(0xFF5532D2) else Color.Gray,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// ── Right panel ──────────────────────────────────────────────────────────────

@Composable
private fun WeekSummaryRow(vm: ParentProgressViewModel) {
    Row(horizontalArrangement = Arrangement.spacedBy(Dimens12)) {
        StatCard(
            icon = Icons.Filled.PlayCircle,
            value = "${vm.weeklySessionCount}",
            label = "Sessions",
            color = Color(0xFF5532D2),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.AccessTime,
            value = if (vm.weeklyDurationSeconds == 0) "—"
                    else ParentProgressViewModel.formatDuration(vm.weeklyDurationSeconds),
            label = "Time",
            color = Color(0xFF2AA65C),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.Adjust,
            value = if (vm.weeklySessionCount == 0) "—"
                    else "${(vm.weeklyAccuracy * 100).toInt()}%",
            label = "Accuracy",
            color = Color(0xFFE8923A),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
            .background(Color.White, RoundedCornerShape(Dimens12))
            .padding(horizontal = Dimens12, vertical = Dimens8)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Dimens24)
        )
        Spacer(Modifier.width(Dimens8))
        Column {
            Text(
                value,
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium.scaled(),
                color = Color.Gray
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

// ── Activity section ─────────────────────────────────────────────────────────

private val ageFilters = listOf(/*"All",*/ "Age 3-5", "Age 5-7", "Age 6-8")

@Composable
private fun ActivitySection(vm: ParentProgressViewModel, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(Dimens8)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Activity Breakdown",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.75f)
            )
            Spacer(Modifier.weight(1f))
//            if (vm.selectedAgeFilter == "Age 6-8") {
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .border(0.8.dp, Color(0xFFE53935).copy(alpha = 0.5f), RoundedCornerShape(50))
//                        .clickable { vm.clearAge68Data() }
//                        .padding(horizontal = Dimens8, vertical = Dimens4)
//                ) {
//                    Text(
//                        text = "Clear 6-8",
//                        style = MaterialTheme.typography.labelSmall.scaled(),
//                        fontWeight = FontWeight.Normal,
//                        color = Color(0xFFE53935)
//                    )
//                }
//                Spacer(Modifier.width(Dimens6))
//            }
            ageFilters.forEach { filter ->
                val isSelected = vm.selectedAgeFilter == filter
                Spacer(Modifier.width(Dimens6))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) Color(0xFF5532D2) else Color.Transparent,
                            RoundedCornerShape(50)
                        )
                        .then(
                            if (!isSelected)
                                Modifier.border(
                                    0.5.dp,
                                    Color.Gray.copy(alpha = 0.3f),
                                    RoundedCornerShape(50)
                                )
                            else Modifier
                        )
                        .clickable { vm.selectAgeFilter(filter) }
                        .padding(horizontal = Dimens8, vertical = Dimens4)
                ) {
                    Text(
                        text = filter,
                        style = MaterialTheme.typography.labelSmall.scaled(),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else Color.Gray
                    )
                }
            }
        }

        val scrollState = rememberScrollState()
        LaunchedEffect(vm.selectedAgeFilter) { scrollState.animateScrollTo(0) }

        if (vm.filteredModuleRows.isEmpty()) {
            EmptyState()
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                vm.filteredModuleRows.forEach { row ->
                    ModuleRowItem(row = row, navController = navController, viewModel = vm)
                }
                if (vm.filteredMasteredLetterRows.isNotEmpty() || vm.filteredMasteredSequenceRows.isNotEmpty()) {
                    MasteredCard(vm.filteredMasteredLetterRows, vm.filteredMasteredSequenceRows)
                }
                if (vm.filteredWeakLetterRows.isNotEmpty() || vm.filteredWeakArrangeRows.isNotEmpty()) {
                    WeakLettersCard(vm.filteredWeakLetterRows, vm.filteredWeakArrangeRows)
                }
                Spacer(modifier = Modifier.height(Dimens8))
            }
        }
    }
}

@Composable
private fun ModuleRowItem(row: ModuleProgressRow, navController: NavController, viewModel: ParentProgressViewModel) {
    val baseModifier = Modifier
        .fillMaxWidth()
        .shadow(2.dp, RoundedCornerShape(Dimens8))
        .background(Color.White, RoundedCornerShape(Dimens8))
        .padding(vertical = Dimens6)
        .padding(start = Dimens12, end = Dimens8)

    val modifier = when {
        row.chapterKey != null -> baseModifier.clickable { viewModel.openChapterDetail(row.chapterKey) }
        row.route != null      -> baseModifier.clickable { navController.navigate(row.route) }
        else                   -> baseModifier
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = modifier
    ) {
        // Age badge — hidden per design request
//        Text(
//            text = row.ageGroupLabel,
//            style = MaterialTheme.typography.labelSmall.scaled(),
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            modifier = Modifier
//                .background(Color(0xFF9374EF), RoundedCornerShape(50))
//                .padding(horizontal = Dimens6, vertical = Dimens4)
//        )

        // Module name + lesson label + divider + subLabel
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = row.displayName,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (row.lessonLabel != null) {
                Text(
                    text = row.lessonLabel,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color(0xFF5532D2),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (row.subLabel != null) {
                Text(
                    text = row.subLabel,
                    style = MaterialTheme.typography.labelSmall.scaled(),
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Rounds — icon + count
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Replay,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(Dimens20)
            )
            Text(
                text = "${row.rounds}",
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }

        // Score (Fill the Blank sub-rows only)
        row.scoreText?.let { score ->
            Text(
                text = score,
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5532D2)
            )
        }

        // Stars
        if (row.hasQuiz) {
            StarsRow(stars = row.avgStars)
        }

        // Accuracy bar — rounded corners, same as iOS
        if (row.hasQuiz) {
            AccuracyBar(accuracy = row.avgAccuracy)
        }

        // Chevron icon (only when tappable)
        if (row.route != null || row.chapterKey != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9374EF).copy(alpha = 0.6f),
                modifier = Modifier.size(Dimens20)
            )
        }
    }
}

@Composable
private fun StarsRow(stars: Double) {
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        repeat(3) { i ->
            val filled = stars >= (i + 1)
            val partial = !filled && stars > i
            Text(
                text = "★",
                style = MaterialTheme.typography.headlineSmall.scaled(),
                color = when {
                    filled  -> Color(0xFFFFD700)
                    partial -> Color(0xFFFFD700).copy(
                        alpha = (stars - i).toFloat().coerceIn(0f, 1f)
                    )
                    else    -> Color(0xFFD5D5D5)
                }
            )
        }
    }
}

@Composable
private fun AccuracyBar(accuracy: Double) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(Dimens8)
    ) {
        // Track
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEEE8FF), RoundedCornerShape(Dimens4))
        )
        // Fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(accuracy.toFloat().coerceIn(0f, 1f))
                .background(Color(0xFF5532D2), RoundedCornerShape(Dimens4))
        )
    }
}

// ── Weak letters card ─────────────────────────────────────────────────────────

@Composable
private fun WeakLettersCard(rows: List<WeakLetterEntry>, arrangeRows: List<WeakArrangeEntry> = emptyList()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
            .background(Color.White, RoundedCornerShape(Dimens12))
            .padding(Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = Color(0xFFE8923A),
                modifier = Modifier.size(Dimens20)
            )
            Text(
                "Needs Practice",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
        rows.forEach { entry ->
            WeakLetterRow(entry.label, entry.subLabel, entry.letters)
        }
        arrangeRows.forEach { entry ->
            val plainLabels = setOf("Opposite Words", "Drag & Drop", "Word Jigsaw", "Singular & Plural", "Match Word & Image", "Listen & Select", "Article Choice", "Sight Word Choice")
            WeakArrangeRow(
                label = entry.label,
                subLabel = entry.subLabel,
                sequences = entry.sequences,
                highlightLetters = entry.label !in plainLabels && entry.label != "Missing Letter" && !entry.label.startsWith("Vocabulary") && entry.label != "Articles A / An" && entry.label != "Sight Words" && entry.label != "Read & Listen",
                isMissingLetter = entry.label == "Missing Letter"
            )
        }
    }
}

@Composable
private fun WeakLetterRow(label: String, subLabel: String? = null, letters: List<Char>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF3D2B9E), RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp).scaled(),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = Dimens4, vertical = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            letters.forEach { letter ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(Dimens28 + Dimens8)
                        .height(Dimens30)
                        .background(Color(0xFFEEE8FF), RoundedCornerShape(Dimens8))
                ) {
                    Text(
                        text = letter.toString(),
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF5532D2)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeakArrangeRow(label: String, subLabel: String?, sequences: List<String>, highlightLetters: Boolean = true, isMissingLetter: Boolean = false) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF3D2B9E), RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp).scaled(),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = Dimens4, vertical = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            sequences.forEach { sequence ->
                if (isMissingLetter) {
                    MissingLetterSequenceBox(sequence)
                } else if (highlightLetters) {
                    WrongSequenceBox(sequence)
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(Color(0xFFEEE8FF), RoundedCornerShape(Dimens8))
                            .height(Dimens30)
                            .padding(horizontal = Dimens8)
                    ) {
                        Text(
                            text = sequence,
                            style = MaterialTheme.typography.titleMedium.scaled(),
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF5532D2)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MissingLetterSequenceBox(sequence: String) {
    val parts = sequence.split(":", limit = 2)
    val word = parts[0]
    val wrongIndices = if (parts.size > 1)
        parts[1].split(",").mapNotNull { it.toIntOrNull() }.toSet()
    else emptySet()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFFEEE8FF), RoundedCornerShape(Dimens8))
            .height(Dimens30)
            .padding(horizontal = Dimens8)
    ) {
        word.forEachIndexed { i, letter ->
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Normal,
                color = if (wrongIndices.contains(i)) Color.Red else Color(0xFF5532D2)
            )
        }
    }
}

@Composable
private fun WrongSequenceBox(sequence: String) {
    val correct = sequence.toList().sorted()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFFEEE8FF), RoundedCornerShape(Dimens8))
            .height(Dimens30)
            .padding(horizontal = Dimens8)
    ) {
        sequence.forEachIndexed { i, letter ->
            val isWrong = i < correct.size && letter != correct[i]
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.titleMedium.scaled(),
                fontWeight = FontWeight.Normal,
                color = if (isWrong) Color.Red else Color(0xFF5532D2)
            )
        }
    }
}

// ── Mastered card ─────────────────────────────────────────────────────────────

@Composable
private fun MasteredCard(rows: List<WeakLetterEntry>, arrangeRows: List<WeakArrangeEntry> = emptyList()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
            .background(Color(0xFFE8F5E9), RoundedCornerShape(Dimens12))
            .padding(Dimens12),
        verticalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            Text(
                "⭐",
                style = MaterialTheme.typography.bodyMedium.scaled()
            )
            Text(
                "What Kids Learned",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
        }
        rows.forEach { entry ->
            MasteredLetterRow(entry.label, entry.subLabel, entry.letters)
        }
        val masteredPlainLabels = setOf(
            "Missing Letter", "Opposite Words", "Drag & Drop", "Word Jigsaw", "Singular & Plural",
            "Match Word & Image", "Listen & Select", "Article Choice", "Sight Word Choice",
            "Articles A / An", "Sight Words", "Read & Listen"
        )
        arrangeRows.forEach { entry ->
            MasteredSequenceRow(
                label = entry.label,
                subLabel = entry.subLabel,
                sequences = entry.sequences,
                highlightLetters = entry.label !in masteredPlainLabels && !entry.label.startsWith("Vocabulary")
            )
        }
    }
}

@Composable
private fun MasteredLetterRow(label: String, subLabel: String? = null, letters: List<Char>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF2E7D32), RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp).scaled(),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = Dimens4, vertical = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens8)
        ) {
            letters.forEach { letter ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(Dimens28 + Dimens8)
                        .height(Dimens30)
                        .background(Color(0xFFA5D6A7), RoundedCornerShape(Dimens8))
                ) {
                    Text(
                        text = letter.toString(),
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

@Composable
private fun MasteredSequenceRow(label: String, subLabel: String?, sequences: List<String>, highlightLetters: Boolean = true) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF2E7D32), RoundedCornerShape(50))
                    .padding(horizontal = Dimens8, vertical = Dimens4)
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp).scaled(),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = Dimens4, vertical = 2.dp)
                )
            }
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Dimens6)
        ) {
            sequences.forEach { sequence ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Color(0xFFA5D6A7), RoundedCornerShape(Dimens8))
                        .height(Dimens30)
                        .padding(horizontal = Dimens8)
                ) {
                    if (highlightLetters) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            sequence.forEach { letter ->
                                Text(
                                    text = letter.toString(),
                                    style = MaterialTheme.typography.titleMedium.scaled(),
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = sequence,
                            style = MaterialTheme.typography.titleMedium.scaled(),
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun EmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(Dimens4, RoundedCornerShape(Dimens12))
            .background(Color.White, RoundedCornerShape(Dimens12))
            .padding(Dimens20)
    ) {
        Text("📚", style = MaterialTheme.typography.displaySmall.scaled())
        Spacer(Modifier.height(Dimens8))
        Text(
            "No activity yet this week.\nStart an activity to see progress here!",
            style = MaterialTheme.typography.bodyLarge.scaled(),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

// ── Chapter Detail Sheet ──────────────────────────────────────────────────────

@Composable
private fun ChapterDetailSheet(
    detail: ChapterDetailData,
    onClose: () -> Unit,
    onPractice: (() -> Unit)? = null,
    selectedAgeFilter: String = "Age 3-5"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth(0.55f)
                .shadow(Dimens24, RoundedCornerShape(Dimens16))
                .background(Color.White, RoundedCornerShape(Dimens16))
                .clickable { /* consume clicks so backdrop doesn't fire */ }
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF9374EF), Color(0xFF5532D2))
                        ),
                        RoundedCornerShape(topStart = Dimens16, topEnd = Dimens16)
                    )
                    .padding(vertical = Dimens12, horizontal = Dimens16),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens4)
                    ) {
                        Text(
                            text = detail.moduleName,
                            style = MaterialTheme.typography.labelMedium.scaled(),
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        if (detail.difficulty.isNotEmpty()) {
                            Text(
                                text = "·",
                                style = MaterialTheme.typography.labelMedium.scaled(),
                                color = Color.White.copy(alpha = 0.4f)
                            )
                            Text(
                                text = detail.difficulty,
                                style = MaterialTheme.typography.labelSmall.scaled(),
                                color = Color.White.copy(alpha = 0.65f)
                            )
                        }
                    }
                    Text(
                        text = detail.chapterTitle,
                        style = MaterialTheme.typography.titleMedium.scaled(),
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    horizontalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    onPractice?.let {
                        KidsActionButton(
                            text = "Practice",
                            type = ButtonType.POSITIVE,
                            isSmall = true,
                            onClick = it
                        )
                    }
                    KidsActionButton(
                        text = "Close",
                        type = ButtonType.DISABLE,
                        isSmall = true,
                        onClick = onClose
                    )
                }
            }

            // Lesson rows
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens12),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                if (detail.lessonGroups.isEmpty()) {
                    Text(
                        "No lessons recorded yet.",
                        style = MaterialTheme.typography.bodyMedium.scaled(),
                        color = Color.Gray,
                        modifier = Modifier.padding(Dimens8)
                    )
                } else {
                    detail.lessonGroups.forEach { group ->
                        if (group.lessonTitle.isNotEmpty()) {
                            Text(
                                text = group.lessonTitle,
                                style = MaterialTheme.typography.labelMedium.scaled(),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5532D2),
                                modifier = Modifier.padding(top = Dimens8)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens6)){
                            group.sessions.forEach { session ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(Dimens2, RoundedCornerShape(Dimens8))
                                        .background(Color(0xFFF8F5FF), RoundedCornerShape(Dimens8))
                                ) {
                                    ChapterSessionRow(session)
                                    SessionLearnedLettersRow(session, selectedAgeFilter)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

private val age35ModuleIds = setOf(
    ModuleID.LETTER_RECOGNITION, ModuleID.ALPHABET_TRACING, ModuleID.ABCD_WITH_IMAGES,
    ModuleID.LETTER_PHONICS, ModuleID.COLORING_ALPHABETS, ModuleID.MATCH_UPPER_LOWER,
    ModuleID.FILL_THE_BLANK_LETTER, ModuleID.ARRANGE_LETTER_SEQUENCE,
    ModuleID.MATCH_LETTER_WITH_IMAGE, ModuleID.MISSING_LETTER
)
private val age57ModuleIds = setOf(
    ModuleID.SIGHT_WORDS, ModuleID.SIGHT_WORD_CHOICE, ModuleID.ARTICLES_A_AN,
    ModuleID.LISTEN_AND_SELECT, ModuleID.ARTICLES_CHOICE, ModuleID.MATCH_WORD_WITH_PICTURE,
    ModuleID.OPPOSITES_WORD, ModuleID.SINGULAR_PLURAL, ModuleID.MISSING_LETTER_57,
    ModuleID.WORD_JIGSAW, ModuleID.VOCABULARY_ANIMALS, ModuleID.VOCABULARY_FRUITS,
    ModuleID.VOCABULARY_BIRDS, ModuleID.VOCABULARY_VEGETABLES, ModuleID.VOCABULARY_COLORS,
    ModuleID.VOCABULARY_SHAPES, ModuleID.VOCABULARY_VEHICLES
)
private val age68ModuleIds = emptySet<String>()

@Composable
private fun SessionLearnedLettersRow(session: SessionEntry, selectedAgeFilter: String = "Age 3-5") {
    val moduleIds = when (selectedAgeFilter) {
        "Age 3-5" -> age35ModuleIds
        "Age 5-7" -> age57ModuleIds
        "Age 6-8" -> age68ModuleIds
        else -> age35ModuleIds + age57ModuleIds + age68ModuleIds
    }
    val correct = session.correctItems
    val wrong = session.wrongItems.map { it.substringBefore(":") }
    if (session.moduleId !in moduleIds || (correct.isEmpty() && wrong.isEmpty())) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens12, end = Dimens12, bottom = Dimens8),
        horizontalArrangement = Arrangement.spacedBy(Dimens4)
    ) {
        correct.forEach { item -> ItemChip(text = item, isWrong = false) }
        wrong.forEach { item -> ItemChip(text = item, isWrong = true) }
    }
}

@Composable
private fun ItemChip(text: String, isWrong: Boolean) {
    val bgColor = if (isWrong) Color(0xFFFFE8EC) else Color(0xFFE6F4EA)
    val textColor = if (isWrong) Color(0xFFD0021B) else Color(0xFF1B7E2A)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .defaultMinSize(minWidth = 26.dp, minHeight = 26.dp)
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = if (text.length > 2) Dimens6 else 0.dp, vertical = 2.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall.scaled(),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun ChapterSessionRow(session: SessionEntry) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens12)
            .padding(top = Dimens8, bottom = Dimens4)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(Icons.Filled.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(Dimens16))
            Text(
                ParentProgressViewModel.formatDuration(session.durationSeconds),
                style = MaterialTheme.typography.labelSmall.scaled(),
                color = Color.Gray
            )
        }

        if (session.totalQuestions > 0) {
            StarsRow(stars = session.stars)
            Text(
                "${session.score}/${session.totalQuestions}",
                style = MaterialTheme.typography.labelMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5532D2)
            )
            AccuracyBar(accuracy = session.accuracy)
        }

        Spacer(Modifier.weight(1f))

        Text(
            formatSessionTime(session.timestampMs),
            style = MaterialTheme.typography.labelSmall.scaled(),
            color = Color.Gray
        )
    }
}

private fun formatSessionTime(timestampMs: Long): String {
    val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    return fmt.format(Date(timestampMs))
}
