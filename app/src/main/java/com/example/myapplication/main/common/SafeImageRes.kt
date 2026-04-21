package com.example.myapplication.main.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.myapplication.R
import com.example.myapplication.data.generation.letter.ImageMapper

//@Composable
//fun getImageResFromWord(word: String?): Int? {
//    if (word.isNullOrBlank()) return null
//
//    val imageName = word
//        .lowercase()
//        .replace(" ", "")
//        .replace("-", "")
//
//    return safeImageRes(imageName) // uses LocalContext
//}

@Composable
fun getImageResFromWord(word: String?): Int? {
    if (word.isNullOrBlank()) return null
    return ImageMapper.get(word)
}

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