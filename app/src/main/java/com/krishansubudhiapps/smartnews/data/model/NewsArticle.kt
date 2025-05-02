package com.krishansubudhiapps.smartnews.data.model

data class NewsArticle(
    val title: String?,
    val description: String?,
    val source: Source?,
    val author: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String?
)
