package com.example.themovieapp.data.source.remote

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movieList.GenreDto
import com.example.themovieapp.data.source.remote.dto.movieList.GenreListDto
import com.example.themovieapp.data.source.remote.dto.movieList.MovieListDto
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
    ): MovieListDto


    @GET("genre/movie/list")
    suspend fun getGenreList(): GenreListDto

    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int
    ): MovieListDto

    @Headers(AUTHENTICATION)
    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavouriteMovies(
        @Path("account_id") accountId: Int = ACCOUNT_ID,
    ):MovieListDto

    @Headers(AUTHENTICATION)
    @POST("account/{account_id}/favorite")
    suspend fun addMovieToFavourite(
        @Path("account_id") accountId: Int = ACCOUNT_ID,
        @Body RAW_BODY : FavouriteBody
    ): retrofit2.Response<ResponseBody>



    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "5905390db2e077b3848f6f28672ae83a"
        private const val AUTHENTICATION = "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OTA1MzkwZGIyZTA3N2IzODQ4ZjZmMjg2NzJhZTgzYSIsInN1YiI6IjY2MDY2YzY4ZDRmZTA0MDE3YzI3NzI0ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.tETeGw_VQHyRqzXFFN7L-KYb5z0F_FMgSXabDUWuON0"
        private const val ACCOUNT_ID = 21152468
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
