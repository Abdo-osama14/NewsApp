package com.example.newsapp.data

import androidx.lifecycle.LiveData
import com.example.newsapp.data.NewsApi
import com.example.newsapp.domain.NewsRepository
import com.example.newsapp.domain.modles.Article
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val api: NewsApi, private val dao:NewsDao) : NewsRepository {

     override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
         api.getBreakingNews(countryCode,pageNumber)

     override suspend fun getSearchNews(searchQuery: String, pageNumber: Int) =
         api.searchForNews(searchQuery,pageNumber)

    override suspend fun upInsertArticles(article: Article): Long =
        dao.upInsertArticles(article)

    override suspend fun deleteArticle(article: Article) =
        dao.deleteArticle(article)

    override  fun getSavedArticles(): LiveData<List<Article>> =
        dao.getSavedArticles()
}