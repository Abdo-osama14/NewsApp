package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.NewsApi
import com.example.newsapp.data.NewsDao
import com.example.newsapp.data.NewsDataBase
import com.example.newsapp.data.NewsRepositoryImpl
import com.example.newsapp.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
         val API_KEY = "6ff47a3122dd406298e8954423774d1b"
         val retrofit by lazy {
            val apiKeyInterceptor = Interceptor() {
                val original = it.request()
                val newUrl = original.url.newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
                    .build()
                it.proceed(original.newBuilder().url(newUrl).build())

            }
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(apiKeyInterceptor)
                .build()
            Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }
        return api
    }

    @Provides
    @Singleton
    fun provideRepository(
         api: NewsApi,
         dao: NewsDao
    ) : NewsRepository{
        return NewsRepositoryImpl(api,dao)
    }

    @Provides
    @Singleton
    fun provideRoomDataBaseInstance(@ApplicationContext context: Context):NewsDataBase{
        return Room.databaseBuilder(
            context.applicationContext,
            NewsDataBase::class.java,
            "News_dp_dp"
        ).fallbackToDestructiveMigration().build()
    }
    @Provides
    @Singleton
    fun provideNewsDao(dp:NewsDataBase):NewsDao{
        return dp.getNewsDao()
    }

}