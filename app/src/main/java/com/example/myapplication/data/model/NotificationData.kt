package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class NotificationData(
    @SerializedName("type") var type: String = "",
    @SerializedName("link") var link: String = ""
)