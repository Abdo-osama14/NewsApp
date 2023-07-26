package com.example.newsapp.domain.modles

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)