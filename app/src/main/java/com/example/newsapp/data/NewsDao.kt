package com.example.newsapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.domain.modles.Article

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upInsertArticles(article: Article):Long

    @Query("select * from Articles")
     fun getSavedArticles():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}