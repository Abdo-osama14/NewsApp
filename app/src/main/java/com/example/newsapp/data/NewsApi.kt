package com.example.newsapp.data

import com.example.newsapp.domain.modles.NewsResponse
import retrofit2.http.GET

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @retrofit2.http.Query("Country")
        countryCode: String = "us",
        @retrofit2.http.Query("Page")
        pageNumber: Int = 1
    ): retrofit2.Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @retrofit2.http.Query("q")
        searchQuery: String,
        @retrofit2.http.Query("Page")
        pageNumber: Int = 1
    ): retrofit2.Response<NewsResponse>

}