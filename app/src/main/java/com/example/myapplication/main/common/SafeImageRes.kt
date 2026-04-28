package com.example.myapplication.main.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.myapplication.R
import com.example.myapplication.data.generation.letter.ImageAlphabetMapper
import com.example.myapplication.data.generation.letter.ImageSentenceMapper
import com.example.myapplication.data.generation.letter.ImageWordMapper

@Composable
fun getImageResFromWord(word: String?): Int? {
    if (word.isNullOrBlank()) return null
    return ImageWordMapper.get(word)
}
@Composable
fun getImageResForAlphabet(imgName: String?): Int? {
    if (imgName.isNullOrBlank()) return null
    return ImageAlphabetMapper.get(imgName)
}
@Composable
fun getImageResForSentence(imgName: String?): Int? {
    if (imgName.isNullOrBlank()) return null
    return ImageSentenceMapper.get(imgName)
}
