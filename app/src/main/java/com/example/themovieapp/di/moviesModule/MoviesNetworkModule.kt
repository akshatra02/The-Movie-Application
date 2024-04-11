package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MoviesNetworkModule {

    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient
            .Builder()
            .connectTimeout(60,TimeUnit.SECONDS)
            .addInterceptor(RequestInterceptor())
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitService(okHttpClient: OkHttpClient): MoviesApi{
    return Retrofit.Builder()
         .addConverterFactory(GsonConverterFactory.create())
         .baseUrl(MoviesApi.BASE_URL)
         .client(okHttpClient)
         .build()
         .create(MoviesApi::class.java)

    }
}
