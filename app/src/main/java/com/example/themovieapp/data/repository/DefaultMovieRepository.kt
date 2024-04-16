package com.example.themovieapp.data.repository

import android.util.Log
import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movielist.GenreDto
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.ExtraMovieDetails
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
) : MovieRepository {

    override fun addExtraMovieDetails(id: Int): Flow<Resource<MovieDetailsAndExtraDetails>> = flow {

        val hasExtraMovieDetails = movieEntityDao.getMovieDetailsAndExtraById(id)?.movieId
        if (hasExtraMovieDetails == null) {
            try {
                fetchAndInsertExtraMovieDetails(moviesApiService, movieEntityDao, id)

            } catch (e: HttpException) {
                emit(Resource.Error("Oops Something went wrong! Try again later."))
                return@flow
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
                return@flow
            }
        }
        emit(
            Resource.Success(
                movieEntityDao.getMovieDetailsAndExtraById(id)?.toMovieDetailsAndExtraDetails()
            )
        )
        return@flow
    }
    override fun getMovieReviewStream(id: Int): Flow<Resource<List<Review>>> = flow {

        val hasReviewLocal = movieEntityDao.getMovieReviewById(id)
        if (hasReviewLocal.isEmpty()) {
            try {
                getMovieReview(moviesApiService, movieEntityDao, id)

            } catch (e: HttpException) {
                emit(Resource.Error("Oops Something went wrong! Try again later."))
                return@flow
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
                return@flow
            }
        }
        emit(
            Resource.Success(movieEntityDao.getMovieReviewById(id).map { it.toReview() })
        )
        return@flow

    }

    override fun getMovieAndExtraDetails(id: Int): Flow<Resource<MovieDetailsAndExtraDetails?>>  = flow{
        emit(Resource.Loading())
        try {
            val getMovieById = movieEntityDao.getMovieDetailsAndExtraById(id)?.toMovieDetailsAndExtraDetails()
            emit(Resource.Success(getMovieById))
            return@flow
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow
        }


    }

    override fun getCastAndCrewStream(id: Int): Flow<Resource<List<CastAndCrew>>> = flow {

        val hasCastAndCrewLocal = movieEntityDao.getCastByMovieId(id)
        if (hasCastAndCrewLocal.isEmpty()) {
            try {
                getCastAndCrew(moviesApiService, movieEntityDao, id)

            } catch (e: HttpException) {
                emit(Resource.Error("Oops Something went wrong! Try again later."))
                return@flow
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
                return@flow
            }
        }
        emit(
            Resource.Success(movieEntityDao.getCastByMovieId(id).map { it.toCastAndCrew() })
        )
        return@flow

    }

    override fun getMoviesByCategoryStream(
        category: String, forceFetchFromRemote: Boolean, page: Int
    ): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading(true))
        val localMovieList = movieEntityDao.getMovieListByCategory(category)
        val shouldLoadLoadMovies = localMovieList.isNotEmpty() && !forceFetchFromRemote

        if (shouldLoadLoadMovies) {
            emit(Resource.Success(getMovieListFromDb(movieEntityDao, category)))
            return@flow
        }
        try {
            fetchAndInsertMovieList(
                movieEntityDao, moviesApiService, category, page
            )
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow
        }
        emit(Resource.Success(getMovieListFromDb(movieEntityDao, category)))
        return@flow

    }
    override fun getMovieByIdStream(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        try {
            val getMovieById = movieEntityDao.getMovieById(id)
            emit(Resource.Success(getMovieById?.toMovie(getMovieById.category ?: "")))
            return@flow
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }
    }



    override fun getAllMoviesStream(): Flow<List<Movie>> = flow {
        emit(movieEntityDao.getAllMovies().map { it.toMovie(it.category) })
        return@flow

    }

    override fun getFavouriteMoviesStream(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading(true))
        try {
            emit(
                Resource.Success(movieEntityDao.getFavouriteMovies()
                    .map { it.toMovie(it.category) })
            )
            return@flow

        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }
    }

    override fun addMovieToFavouriteStream(movie: FavouriteBody): Flow<Resource<MovieDetailsAndExtraDetails>> = flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    addMovieToFavouriteRemote(
                        moviesApiService, movieEntityDao, movie
                    )
                )
            )
            return@flow

        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }
    }
}


private suspend fun getMovieReview(
    moviesApiService: MoviesApi,
    movieEntityDao: MovieEntityDao,
    movieId: Int
): List<Review> {
    val isReviewLocal = movieEntityDao.getMovieReviewById(movieId)
    if (isReviewLocal.isEmpty()){
        val review = moviesApiService.getReviewsById(movieId)
        review.results
        Log.d("review " , review.results.toString())
        movieEntityDao.insertMovieReview(review.results.map { it.toReviewEntity(movieId) })
    }
    return movieEntityDao.getMovieReviewById(movieId).map { it.toReview() }
}

private suspend fun addMovieToFavouriteRemote(
    moviesApiService: MoviesApi, movieEntityDao: MovieEntityDao, movie: FavouriteBody
): MovieDetailsAndExtraDetails? {
    val response = moviesApiService.addMovieToFavourite(RAW_BODY = movie)
    val getMovie = movieEntityDao.getMovieById(movie.media_id)
    if (response.isSuccessful) {
        val updateMovie = getMovie?.copy(isFavourite = !getMovie.isFavourite)
        val movieEntityStatus = updateMovie.let {
            if (it != null) {
                movieEntityDao.updateMovie(it)
            }
        }
    }
    return movieEntityDao.getMovieDetailsAndExtraById(movie.media_id)?.toMovieDetailsAndExtraDetails()
}

private suspend fun fetchAndInsertMovieList(
    movieEntityDao: MovieEntityDao,
    moviesApiService: MoviesApi,
    category: String,
    page: Int,
) {
    val remoteMovieList = moviesApiService.getMovieList(category, page)
    val genreList = getGenreList(moviesApiService)
    val favouriteMovieList = moviesApiService.getFavouriteMovies().results
    val movieEntities = remoteMovieList.results.map { movieDto ->
        val genreNames =
            genreList.filter { it.id in (movieDto.genreIds ?: emptyList()) }.map { it.name }
        val isFavourite = favouriteMovieList.any { it.id == movieDto.id }
        movieDto.toMovieEntity(category, genreNames.joinToString(","), isFavourite)
    }
    movieEntityDao.insertMovieList(movieEntities)
}

private suspend fun getGenreList(
    moviesApiService: MoviesApi,

    ): List<GenreDto> {
    return moviesApiService.getGenreList().genres
}


private suspend fun getMovieListFromDb(
    movieEntityDao: MovieEntityDao, category: String
): List<Movie> {
    return movieEntityDao.getMovieListByCategory(category).map { it.toMovie(category) }
}

private suspend fun fetchAndInsertExtraMovieDetails(
    moviesApiService: MoviesApi,
    movieEntityDao: MovieEntityDao,
    movieId: Int,
) {
    val getExtraMovieDetailsRemote = moviesApiService.getExtraMovieDetailsById(movieId)
    val getRecommendationList = moviesApiService.getRecommendationList(movieId)
    val recommendedMovieListIds = mutableListOf<Int>()
    if (getRecommendationList.results.isNotEmpty()) {
        getRecommendationList.results.map { recommendedMovieListIds.add(it.id ?: -1) }
        recommendedMovieListIds.forEach {
            fetchAndInsertMovie(moviesApiService, movieEntityDao, it)
        }
    }
    val getImageList = moviesApiService.getImages(movieId)
    val postersList = getImageList.posters.map { it.filePath }
    val backdropsList = getImageList.backdrops.map { it.filePath }
    val logosList = getImageList.logos.map { it.filePath }
    val imageList = postersList + backdropsList + logosList
    val getVideoPath = moviesApiService.getVideo(movieId)
    val videoPath = getVideoPath.results.find { it.type == "Trailer" }?.key ?: ""
    val upsertExtraMovieDetailsTable = movieEntityDao.upsertExtraMovieDetails(
        getExtraMovieDetailsRemote.toExtraMovieDetailsEntity(
            recommendedMovieListIds,
            imageList.toString(),
            videoPath,
        )
    )
}

private suspend fun fetchAndInsertMovie(
    moviesApiService: MoviesApi,
    movieEntityDao: MovieEntityDao,
    movieId: Int,
) {
    val movie = moviesApiService.getExtraMovieDetailsById(movieId)

    val genreNames = movie.genres?.map { it.name }?.toList() ?: emptyList()
    val genreIds = movie.genres?.map { it.id }?.toList() ?: emptyList()
    val movieEntity = movie.toMovieEntity(
        "",
        genreNames.toString(),
        genreIds.toString(),
        movie.isFavourite,
    )
    movieEntityDao.upsertMovie(movieEntity)
}

private suspend fun getCastAndCrew(
    moviesApiService: MoviesApi, movieEntityDao: MovieEntityDao, movieId: Int
) {
    val getCreditsRemote = moviesApiService.getCastAndCrew(movieId)
    val castMembers = getCreditsRemote.cast
    castMembers.forEach { cast ->
        movieEntityDao.insertCastAndCrew(cast.toCastAndCrewEntity(movieId))
    }
    val crewMembers = getCreditsRemote.crew
    crewMembers.forEach { crew ->
        movieEntityDao.insertCastAndCrew(crew.toCastAndCrewEntity(movieId))
    }

}