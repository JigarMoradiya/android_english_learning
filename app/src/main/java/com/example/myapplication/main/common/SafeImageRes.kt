package com.example.myapplication.main.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.myapplication.R

@Composable
fun safeImageRes(name: String?): Int? {
    name?.let {
        return remember(name) {
            runCatching {
                val field = R.drawable::class.java.getField(name)
                field.getInt(null)
            }.getOrElse {
                null
            }
        }
    }
    return null
}