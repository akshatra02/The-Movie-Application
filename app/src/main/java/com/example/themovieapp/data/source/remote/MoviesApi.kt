package com.example.themovieapp.data.source.remote

import com.example.themovieapp.data.source.remote.dto.movieList.GenreDto
import com.example.themovieapp.data.source.remote.dto.movieList.GenreListDto
import com.example.themovieapp.data.source.remote.dto.movieList.MovieListDto
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int
    ): MovieListDto


    @GET("genre/movie/list")
    suspend fun getGenreList(): GenreListDto

    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int
    ): MovieListDto



    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "5905390db2e077b3848f6f28672ae83a"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"


    }
}

sealed class Resource<T>(val data: T?= null, val message: String? =null){
    class Success<T>(data: T?): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Error<T>( message: String, data: T? = null): Resource<T>(data, message)
}
class RequestInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url
            .newBuilder()
            .addQueryParameter(
                name = "api_key",
                value = "5905390db2e077b3848f6f28672ae83a"
            )
            .build()
        val request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }

}
