package com.krishansubudhiapps.smartnews.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDataResponse(
    val status: String,
    val totalResults: Int,
    val results: List<NewsDataArticle>
)

@JsonClass(generateAdapter = true)
data class NewsDataArticle(
    val articleId: String?, // Made articleId nullable
    val title: String?,
    val link: String?,
    val keywords: List<String>?,
    val creator: List<String>?,
    val videoUrl: String?,
    val description: String?,
    val content: String?,
    val pubDate: String?,
    @Json(name = "image_url") val imageUrl: String?,
    val sourceId: String?,
    val sourceName: String?,
    val sourceIcon: String?,
    val sourceUrl: String?,
    val language: String?,
    val country: List<String>?,
    val category: List<String>?,
    val aiPowered: AIPowered?,
    val sentiment: String?,
    val sentimentStats: SentimentStats?
)

@JsonClass(generateAdapter = true)
data class AIPowered(
    val summary: String?
)

@JsonClass(generateAdapter = true)
data class SentimentStats(
    val positive: Double?,
    val negative: Double?,
    val neutral: Double?
)
