package com.example.myapplication.data.repositories

import com.example.myapplication.data.api.ContentApi
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val contentApi: ContentApi
) {
//    suspend fun fetchContents(): List<ContentItem> {
//        return contentApi.getContents()
//    }
}