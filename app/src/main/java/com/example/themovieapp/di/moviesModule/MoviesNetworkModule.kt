package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
