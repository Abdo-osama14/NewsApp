package com.example.newsapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.NewsRepository
import com.example.newsapp.domain.modles.Article
import com.example.newsapp.domain.modles.NewsResponse
import com.example.newsapp.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews = MutableLiveData<Resource<NewsResponse>>()
    private var breakingNewsPage = 1

    val searchingNews = MutableLiveData<Resource<NewsResponse>>()
    private var searchingNewsPage = 1

    fun getBreakingNews( countryCode:String)=viewModelScope.launch {
        breakingNews.value=Resource.Loading()
        val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.value= handleBreakingNewsResponse(response)
    }

    fun getSearchNews(searchQuery:String)=viewModelScope.launch {
        searchingNews.value=Resource.Loading()
        val response=newsRepository.getSearchNews(searchQuery,searchingNewsPage)
        searchingNews.value=handleSearchNewsResponse(response)
    }

    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
    fun addToFav(article: Article)=viewModelScope.launch {
        newsRepository.upInsertArticles(article)
    }
    fun getFavArticles()=newsRepository.getSavedArticles()


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
    return if (response.isSuccessful) {
          Resource.Success(response.body()!!)
        }
        else{
           Resource.Error(response.message())
        }
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }
}