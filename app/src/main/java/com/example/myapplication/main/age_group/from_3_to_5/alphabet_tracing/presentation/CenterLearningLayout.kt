package com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation

import androidx.compose.foundation.Image
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.GuidelineCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.presentation.canvas.TracingCanvas
import com.example.myapplication.main.age_group.from_3_to_5.alphabet_tracing.view_model.AlphabetTracingViewModel
import com.example.myapplication.ui.theme.AppDimens.AlphabetTracingArrowImageHeight
import com.example.myapplication.ui.theme.AppDimens.AlphabetTracingLetterSize
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens50
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.isLargeTablet
import com.example.myapplication.ui.theme.AppDimens.isTablet
import com.example.myapplication.utils.extensions.scaled

@Composable
fun CenterLearningLayout(
    modifier : Modifier,
    viewModel: AlphabetTracingViewModel,
    imageRes: Int?,
    word: String?,
    arrowImageRes: Int? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        BoxWithConstraints {

            val minusExtraSpace = if(isLargeTablet) 100.dp else if (isTablet) 60.dp else 0.dp
            val size = min(maxWidth - minusExtraSpace, maxHeight - minusExtraSpace)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // LEFT
                LeftLetterView(
                    modifier = Modifier.weight(1f),
                    viewModel,
                    arrowImageRes = arrowImageRes
                )

                // CENTER (fixed size)
                CenterTracingView(size, viewModel)

                // RIGHT
                RightImageWordView(
                    modifier = Modifier.weight(1f),
                    imageRes = imageRes,
                    word = word
                )
            }
        }
    }
}

@Composable
fun LeftLetterView(
    modifier: Modifier = Modifier,
    viewModel: AlphabetTracingViewModel,
    arrowImageRes: Int? = null
) {
    val highlighted = viewModel.isPhonicsHighlighted
    val phonicsColor by animateColorAsState(
        targetValue = if (highlighted) viewModel.getLetterColor() else Color(0xFF9E9E9E),
        animationSpec = tween(200),
        label = "phonicsColor"
    )
    val phonicsScale by animateFloatAsState(
        targetValue = if (highlighted) 1.2f else 1f,
        animationSpec = tween(200),
        label = "phonicsScale"
    )

    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (arrowImageRes != null) {
                Image(
                    painter = painterResource(id = arrowImageRes),
                    contentDescription = viewModel.uiState.mode.displayString(viewModel.uiState.currentIndex),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(AlphabetTracingArrowImageHeight)
                )
            } else {
                Text(
                    text = viewModel.uiState.mode.displayString(viewModel.uiState.currentIndex),
                    color = Color.Black,
                    fontSize = AlphabetTracingLetterSize,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier.height(Dimens50),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.phonicsSound,
                    color = phonicsColor,
                    style = MaterialTheme.typography.bodyLarge.scaled(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.graphicsLayer(scaleX = phonicsScale, scaleY = phonicsScale)
                )
            }
        }
    }
}

@Composable
fun CenterTracingView(
    size: Dp,
    viewModel: AlphabetTracingViewModel
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(Dimens16),
            elevation = CardDefaults.cardElevation(Dimens4),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            GuidelineCanvas(viewModel)
        }

        TracingCanvas(viewModel)
    }
}

@Composable
fun RightImageWordView(
    modifier: Modifier = Modifier,
    imageRes: Int?,
    word: String?
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {

        if (imageRes != null && word != null) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = word,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxHeight(0.4f)
                )

                Spacer(modifier = Modifier.height(Dimens16))

                Text(
                    text = word,
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineLarge.scaled(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}