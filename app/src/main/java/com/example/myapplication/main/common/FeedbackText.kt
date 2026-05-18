package com.example.myapplication.main.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.PrimaryGreen
import com.example.myapplication.utils.extensions.scaled

@Composable
fun FeedbackText(
    title: String?,
    subtitle: String?,
    isSuccess: Boolean,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        title?.let {
            Text(
                text = title,
                color = if (isSuccess) PrimaryGreen else Color.Red,
                style = MaterialTheme.typography.titleSmall.scaled(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(if (isVisible) 1f else 0f)
            )
        }

        if (!title.isNullOrEmpty() && !subtitle.isNullOrEmpty()){
            Spacer(modifier = Modifier.height(AppDimens.Dimens2))
        }

        subtitle?.let{
            Text(
                text = AnnotatedString.fromHtml(subtitle),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium.scaled(),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(if (isVisible) 1f else 0f)
            )
        }

        Spacer(modifier = Modifier.height(Dimens16))
    }
}