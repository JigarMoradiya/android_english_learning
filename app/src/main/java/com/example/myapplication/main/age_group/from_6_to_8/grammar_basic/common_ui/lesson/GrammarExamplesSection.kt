package com.example.myapplication.main.age_group.from_6_to_8.grammar_basic.common_ui.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.GrammarExampleModel
import com.example.myapplication.ui.theme.AppDimens.Dimens10
import com.example.myapplication.ui.theme.AppDimens.Dimens8
import com.example.myapplication.ui.theme.AppDimens.Dimens12
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens6
import com.example.myapplication.utils.extensions.scaled

@Composable
fun GrammarExamplesSection(
    examples: List<GrammarExampleModel>,
    cardColor: Color,
    size: Dp,
    padding: Dp,
    isSmallText: Boolean = false,
    modifier : Modifier = Modifier,
    onExampleClick: (GrammarExampleModel) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens6)
    ) {
        items(examples) { example ->
            Box(
                modifier = Modifier
                    .padding(Dimens4)
                    .clip(RoundedCornerShape(Dimens16))
                    .clickable {
                        onExampleClick(example)
                    }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens16))
                        .background(cardColor.copy(alpha = 0.30f))
                        .padding(vertical = Dimens8), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(example.media), contentDescription = null, modifier = Modifier
                            .size(size)
                            .padding(horizontal = padding)
                    )

                    Spacer(modifier = Modifier.height(Dimens8))

                    Text(
                        text = example.name, style = if (isSmallText)MaterialTheme.typography.bodyMedium.scaled() else MaterialTheme.typography.titleMedium.scaled(), fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .align(
                            if (padding == 0.dp) {
                                Alignment.TopStart
                            } else {
                                Alignment.TopEnd
                            }
                        )
                        .padding(Dimens8)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                        .padding(Dimens6)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(Dimens16)
                    )
                }


            }

        }
    }
}