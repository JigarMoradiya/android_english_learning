package com.example.myapplication.main.age_group.from_6_to_8.progress

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.generation.loader.SentenceProgressSummary
import com.example.myapplication.main.age_group.from_6_to_8.progress.view_model.SentenceProgressViewModel
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.KidsFloatingShape
import com.example.myapplication.main.common.KidsGradient
import com.example.myapplication.main.common.KidsGradientBackground
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SentenceProgressPage(
    navController: NavController,
    viewModel: SentenceProgressViewModel = hiltViewModel()
) {
    val summary: SentenceProgressSummary = viewModel.summary
    val cardBg = Color.White.copy(alpha = 0.9f)
    val accent = Color(0xFF6A5AE0)

    Box(modifier = Modifier.fillMaxSize()) {
        KidsGradientBackground(gradient = KidsGradient.blueIndigo, shape = KidsFloatingShape.sparkles)

        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                BackButtonWithText(
                    title = stringResource(R.string.my_progress),
                    modifier = Modifier.weight(1f),
                    onBackClick = { navController.popBackStack() }
                )
            }

            val dayLabels = remember {
                (0..6).map { i ->
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DAY_OF_YEAR, -(6 - i))
                    SimpleDateFormat("EEEEE", Locale.getDefault()).format(cal.time)
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens16)
            ) {

                // Hero — total activities
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens16))
                        .background(accent.copy(alpha = 0.15f))
                        .padding(horizontal = Dimens16, vertical = Dimens8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🏅", fontSize = 34.sp)
                    Spacer(Modifier.width(Dimens12))
                    Column {
                        Text(
                            "${summary.totalActivities}",
                            color = accent,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp
                        )
                        Text("activities completed", color = Color.Gray, fontSize = 14.sp, lineHeight = 16.sp)
                    }
                }

                // Last 7 days trend
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens16))
                        .background(cardBg)
                        .padding(horizontal = Dimens16, vertical = Dimens8),
                    verticalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    Text("Last 7 days", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    val maxC = (summary.last7DayCounts.maxOrNull() ?: 1).coerceAtLeast(1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens8),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        summary.last7DayCounts.forEachIndexed { i, c ->
                            val isToday = i == 6
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text("$c",
                                    color = if (isToday) accent else Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height((c.toFloat() / maxC * 64f + 4f).dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isToday) accent else accent.copy(alpha = 0.35f))
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(dayLabels[i], color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    }
                }

                // Per-module breakdown
                summary.perModule.forEach { m ->
                    val pct = (m.bestAccuracy * 100).toInt()
                    val pillColor = when {
                        pct >= 80 -> Color(0xFF2E7D32)
                        pct >= 50 -> Color(0xFFFF9800)
                        else -> Color.Gray
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Dimens16))
                            .background(cardBg)
                            .padding(horizontal = Dimens16, vertical = Dimens8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(10.dp)
                                .height(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(accent)
                        )
                        Spacer(Modifier.width(Dimens12))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(m.title, color = Color.Black, fontWeight = FontWeight.SemiBold)
                            Text("${m.sessions} played", color = Color.Gray, fontSize = 12.sp)
                        }
                        Spacer(Modifier.width(Dimens12))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(accent.copy(alpha = 0.15f))
                                .padding(horizontal = Dimens12, vertical = Dimens8)
                        ) {
                            Text("Last ${(m.lastAccuracy * 100).toInt()}%", color = accent,
                                fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                        Spacer(Modifier.width(Dimens8))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(pillColor.copy(alpha = 0.15f))
                                .padding(horizontal = Dimens12, vertical = Dimens8)
                        ) {
                            Text("Best $pct%", color = pillColor,
                                fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }
                }

                if (summary.perModule.isEmpty()) {
                    Text(
                        "Play a sentence activity to see your progress here!",
                        color = Color.Black,
                        modifier = Modifier.padding(Dimens16)
                    )
                }
            }
        }
    }
}
