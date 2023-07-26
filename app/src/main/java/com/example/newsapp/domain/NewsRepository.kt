package com.example.newsapp.domain

import androidx.lifecycle.LiveData
import com.example.newsapp.domain.modles.Article
import com.example.newsapp.domain.modles.NewsResponse
import retrofit2.Response

interface NewsRepository {

     suspend fun getBreakingNews(countryCode:String, pageNumber:Int) : Response<NewsResponse>
     suspend fun getSearchNews(searchQuery:String,pageNumber: Int) : Response<NewsResponse>
     suspend fun upInsertArticles(article: Article):Long
     suspend fun deleteArticle(article: Article)
      fun getSavedArticles(): LiveData<List<Article>>
}