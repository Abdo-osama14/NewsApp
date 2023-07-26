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
     var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null


    val searchingNews = MutableLiveData<Resource<NewsResponse>>()
     var searchingNewsPage = 1
    var searchingNewsResponse : NewsResponse? = null


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
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> { if (response.isSuccessful){
        response.body()?.let { resultResponse ->
            searchingNewsPage++
            if (searchingNewsResponse == null){
                searchingNewsResponse = resultResponse
            } else {
                val oldArticles = searchingNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            return Resource.Success(searchingNewsResponse ?: resultResponse)
        }
    }
        return Resource.Error(response.message())
    }
}