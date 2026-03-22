package com.example.myapplication.main.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens2
import com.example.myapplication.ui.theme.AppDimens.Dimens4
import com.example.myapplication.ui.theme.AppDimens.Dimens8

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isIconStart: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = Dimens4,
            pressedElevation = Dimens2
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = Dimens16, vertical = Dimens8)
    ) {

        if (isIconStart) {
            Icon(icon, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(Dimens4))
        }

        Text(
            text = text,
            color = Color.Black
        )

        if (!isIconStart) {
            Spacer(Modifier.width(Dimens4))
            Icon(icon, contentDescription = null, tint = Color.Black)
        }
    }
}