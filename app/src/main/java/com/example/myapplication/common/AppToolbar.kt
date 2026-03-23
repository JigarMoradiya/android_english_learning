package com.example.myapplication.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.myapplication.R
import com.example.myapplication.main.common.ActionButton
import com.example.myapplication.main.common.BackButtonWithText
import com.example.myapplication.main.common.ButtonType
import com.example.myapplication.ui.theme.AppDimens
import com.example.myapplication.ui.theme.AppDimens.Dimens16
import com.example.myapplication.ui.theme.AppDimens.Dimens8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
        Text(
            text = title, style = MaterialTheme.typography.titleLarge, fontSize = AppDimens.FontMedium16
        )
    }, navigationIcon = {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back)
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        titleContentColor = Color.Black, navigationIconContentColor = Color.Black
    )
    )
}


@Composable
fun AppToolbarDropDownOnRight(
    title: String,
    currentSelected: String,
    modes: List<String>,
    onItemChange: (String) -> Unit,
    onBackClick: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // LEFT
        Box(modifier = Modifier.weight(1f)) {
            BackButtonWithText(
                title = title,
                onBackClick = onBackClick
            )
        }

        // RIGHT (FORCE SPACE)
        Box(
            modifier = Modifier
                .weight(1f).padding(horizontal = Dimens16, vertical = Dimens8),
            contentAlignment = Alignment.CenterEnd
        ) {

            Box {
                ActionButton(
                    text = currentSelected,
                    icon = Icons.Default.KeyboardArrowDown,
                    type = ButtonType.BLUE,
                    onClick = { expanded = true },
                    isIconStart = false
                )

                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }, offset = DpOffset(x = 0.dp, y = 8.dp), // ⭐ stabilizes position
                    properties = PopupProperties(
                        clippingEnabled = false // ⭐ IMPORTANT
                    ), containerColor = Color.White, tonalElevation = 0.dp, shadowElevation = 10.dp, shape = RoundedCornerShape(Dimens16)
                ) {
                    modes.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Text(text = item, modifier = Modifier.weight(1f), color = Color.Black)

                                    if (item == currentSelected) {
                                        Spacer(modifier = Modifier.width(Dimens8))
                                        Icon(
                                            imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black
                                        )
                                    }
                                }
                            },
                            onClick = {
                                expanded = false
                                onItemChange(item)
                            }
                        )
                        if (index != modes.lastIndex) {
                            HorizontalDivider(
                                thickness = 0.6.dp, color = Color.LightGray.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}