package com.example.myapplication.main.common.sheets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens24
import com.example.myapplication.ui.theme.AppDimens.SheetIconCircle
import com.example.myapplication.ui.theme.AppDimens.SheetIconSizeLg
import com.example.myapplication.utils.extensions.scaled
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Parental gate overlay — shown before login or purchase screens.
 * Required by Apple Kids category guidelines.
 *
 * Presents a simple addition question. Wrong answer → shake + regenerate.
 * Correct answer → calls [onPassed].
 */
@Composable
fun ParentalGateDialog(
    onPassed: () -> Unit,
    onCancelled: () -> Unit
) {
    var num1 by remember { mutableStateOf(Random.nextInt(2, 10)) }
    var num2 by remember { mutableStateOf(Random.nextInt(2, 10)) }
    var options by remember { mutableStateOf(generateOptions(num1 + num2)) }
    val shakeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun regenerate() {
        num1 = Random.nextInt(2, 10)
        num2 = Random.nextInt(2, 10)
        options = generateOptions(num1 + num2)
    }

    fun handleAnswer(selected: Int) {
        if (selected == num1 + num2) {
            onPassed()
        } else {
            scope.launch {
                // Shake animation
                repeat(4) { i ->
                    shakeOffset.animateTo(
                        targetValue = if (i % 2 == 0) 12f else -12f,
                        animationSpec = tween(durationMillis = 80)
                    )
                }
                shakeOffset.animateTo(0f, animationSpec = tween(80))
                delay(100)
                regenerate()
            }
        }
    }

    // Scrim + centered card
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        // Card
        Column(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .padding(horizontal = Dimens24)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(Dimens24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            // Lock icon
            Box(
                modifier = Modifier
                    .size(SheetIconCircle)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔒", fontSize = SheetIconSizeLg)
            }

            Text(
                text = "For Parents",
                style = MaterialTheme.typography.titleLarge.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1C)
            )

            Text(
                text = "Solve this to continue",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color(0xFF666666)
            )

            // Question with shake
            Text(
                text = "$num1 + $num2 = ?",
                style = MaterialTheme.typography.headlineMedium.scaled(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0074D5),
                modifier = Modifier.graphicsLayer { translationX = shakeOffset.value }
            )

            // Answer options
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                options.forEach { option ->
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFF6FF))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { handleAnswer(option) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$option",
                            style = MaterialTheme.typography.titleMedium.scaled(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0074D5)
                        )
                    }
                }
            }

            // Cancel
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.bodyMedium.scaled(),
                color = Color(0xFF999999),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCancelled() }
                    .padding(Dimens8)
            )
        }
    }
}

private fun generateOptions(correct: Int): List<Int> {
    val set = mutableSetOf(correct)
    while (set.size < 3) {
        val wrong = correct + Random.nextInt(-5, 6)
        if (wrong > 0 && wrong != correct) set.add(wrong)
    }
    return set.shuffled()
}
