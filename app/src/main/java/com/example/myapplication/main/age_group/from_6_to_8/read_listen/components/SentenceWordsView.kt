package com.example.myapplication.main.age_group.from_6_to_8.read_listen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppDimens.Dimens1
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.PrimaryBlue

@Composable
fun SentenceWordsView(
    words: List<String>,
    isJoined: Boolean,
    speakingIndex: Int?,
    currentWordIndex: Int
) {

    val containerPaddingH = Dimens8
    val containerPaddingV = Dimens4

    if (isJoined) {

        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PrimaryBlue, PrimaryBlue.copy(alpha = 0.8f))
                    ),
                    shape = RoundedCornerShape(containerPaddingH)
                )
                .padding(horizontal = containerPaddingH, vertical = containerPaddingV),
            horizontalArrangement = Arrangement.Center
        ) {

            words.forEachIndexed { index, word ->

                val isSpeaking = speakingIndex == index

                val scale by animateFloatAsState(
                    targetValue = if (isSpeaking) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = ""
                )

                Text(
                    text = word,
                    modifier = Modifier
                        .padding(horizontal = Dimens2) // 👈 VERY IMPORTANT (was too big before)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .background(
                            color = if (isSpeaking) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(Dimens2)
                        )
                        .padding(horizontal = Dimens2, vertical = 0.dp),
                    color = if (isSpeaking) Color.Black else Color.White,
                    fontWeight = if (isSpeaking) FontWeight.Bold else FontWeight.SemiBold
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            words.forEachIndexed { index, word ->

                val isSpeaking = currentWordIndex == index

                val scale by animateFloatAsState(
                    targetValue = if (isSpeaking) 1.1f else 1f,
                    animationSpec = spring(dampingRatio = 0.6f)
                )

                val rotation by animateFloatAsState(
                    targetValue = if (isSpeaking) 2f else 0f
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = Dimens2)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            rotationZ = rotation
                        }
                        .shadow(
                            elevation = if (isSpeaking) Dimens8 else Dimens1,
                            shape = RoundedCornerShape(containerPaddingH),
                            ambientColor = if (isSpeaking) Color(0xFFFB8C00) else Color.Black
                        )
                        .background(
                            brush = if (isSpeaking) {
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFFB8C00), Color(0xFFFFB300))
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(Color.White, Color.White)
                                )
                            },
                            shape = RoundedCornerShape(containerPaddingH)
                        )
                        .border(
                            width = if(isSpeaking) Dimens2 else Dimens1,
                            color = if (isSpeaking) Color(0xFFF4511E) else Color.Gray.copy(0.3f),
                            shape = RoundedCornerShape(containerPaddingH)
                        )
                        .padding(horizontal = containerPaddingH, vertical = containerPaddingV)
                ) {
                    Text(
                        text = word,
                        color = if (isSpeaking) Color.White else Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}