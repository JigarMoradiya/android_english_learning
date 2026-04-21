package com.example.myapplication.main.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.myapplication.R
import com.example.myapplication.data.generation.letter.ImageSentenceMapper
import com.example.myapplication.data.generation.letter.ImageWordMapper

@Composable
fun getImageResFromWord(word: String?): Int? {
    if (word.isNullOrBlank()) return null
    return ImageWordMapper.get(word)
}
@Composable
fun getImageResForSentence(imgName: String?): Int? {
    if (imgName.isNullOrBlank()) return null
    return ImageSentenceMapper.get(imgName)
}

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
//@Composable
//fun safeImageRes(name: String?): Int? {
//    name?.let {
//        return remember(name) {
//            runCatching {
//                val field = R.drawable::class.java.getField(name)
//                field.getInt(null)
//            }.getOrElse {
//                null
//            }
//        }
//    }
//    return null
//}