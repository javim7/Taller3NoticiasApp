package com.example.taller3appnoticias

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author") val author:String,
    @SerializedName("title") val title:String,
    @SerializedName("description") val description:String,
    @SerializedName("urlToImage") val urlToImage:String,

)