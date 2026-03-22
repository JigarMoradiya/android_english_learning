package com.example.myapplication.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.myapplication.R
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbarDropDownOnRight(
    title: String, currentSelected: String, modes: List<String>, onItemChange: (String) -> Unit, onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
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
    }, actions = {

        // MODE DROPDOWN (Capsule style)
            Box {
                Row(
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp, shape = RoundedCornerShape(50), clip = false
                        )
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .clickable { expanded = true }
                        .padding(horizontal = Dimens16, vertical = Dimens8), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentSelected, color = Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Black
                    )
                }

                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }, offset = DpOffset(x = 0.dp, y = 8.dp), // ⭐ stabilizes position
                    properties = PopupProperties(
                        clippingEnabled = false // ⭐ IMPORTANT
                    ), containerColor = Color.White, tonalElevation = 0.dp, shadowElevation = 10.dp, shape = RoundedCornerShape(Dimens16)
                ) {
                    modes.forEachIndexed { index, item ->

                        DropdownMenuItem(text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Text(
                                    text = item, modifier = Modifier.weight(1f), color = Color.Black
                                )

                                if (item == currentSelected) {
                                    Spacer(modifier = Modifier.width(Dimens8))
                                    Icon(
                                        imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black
                                    )
                                }
                            }
                        }, onClick = {
                            expanded = false
                            onItemChange(item)
                        })
                        if (index != modes.lastIndex) {
                            HorizontalDivider(
                                thickness = 0.6.dp, color = Color.LightGray.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
        }

        Spacer(modifier = Modifier.width(Dimens16))
    },colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,   // ⭐ IMPORTANT
            scrolledContainerColor = Color.Transparent, // ⭐ IMPORTANT (scroll case)
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
    ))
}