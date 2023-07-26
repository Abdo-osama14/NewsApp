package com.example.newsapp.domain.modles

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)