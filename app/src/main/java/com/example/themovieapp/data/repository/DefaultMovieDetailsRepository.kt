package com.example.themovieapp.data.repository

import com.example.themovieapp.data.mappers.toDomainCastAndCrew
import com.example.themovieapp.data.mappers.toDomainMovie
import com.example.themovieapp.data.mappers.toDomainMovieAndExtraDetails
import com.example.themovieapp.data.mappers.toDomainReview
import com.example.themovieapp.data.mappers.toEntityCastAndCrew
import com.example.themovieapp.data.mappers.toEntityExtraMovieDetails
import com.example.themovieapp.data.mappers.toEntityMovie
import com.example.themovieapp.data.mappers.toEntityReview
import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultMovieDetailsRepository @Inject constructor(
    private val movieEntityDao: MovieEntityDao,
    private val moviesApiService: MoviesApi,
) : MovieDetailsRepository {
    override suspend fun getMovieAndExtraDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails?>> {
        return try {
            val movieById = movieEntityDao.getMovieDetailsById(id)
            (Result.Success(movieById?.map { it.toDomainMovieAndExtraDetails() }))
        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")
        } catch (e: IOException) {
            Result.Error("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun addExtraMovieDetails(id: Int) {
        val movieDetails = movieEntityDao.getMovieDetailsById(id)?.first()
        if (movieDetails?.movieId == null) {
            try {
                fetchAndInsertExtraMovieDetails(id)
                Result.Success("Successful")
            } catch (e: HttpException) {
                Result.Error("Oops Something went wrong! Try again later.")
            } catch (e: IOException) {
                Result.Error("Couldn't reach server. Check your internet connection")
            }
        }
    }

    override suspend fun getCastAndCrewStream(id: Int): Result<Flow<List<CastAndCrew>>> {
        val castAndCrewEntityList = movieEntityDao.getCastByMovieId(id).first()
        if (castAndCrewEntityList.isEmpty()) {
            return try {
                getCastAndCrewRemote(id)
                Result.Success(movieEntityDao.getCastByMovieId(id)
                    .map { resultCastAndCrewEntityList -> resultCastAndCrewEntityList.map { castAndCrewEntity -> castAndCrewEntity.toDomainCastAndCrew() } })
            } catch (e: HttpException) {
                Result.Error(message = "Oops Something went wrong! Try again later.")
            } catch (e: IOException) {
                Result.Error(message = "Couldn't reach server. Check your internet connection")
            }
        }
        return Result.Success(movieEntityDao.getCastByMovieId(id)
            .map { resultCastAndCrewEntityList -> resultCastAndCrewEntityList.map { castAndCrewEntity -> castAndCrewEntity.toDomainCastAndCrew() } })
    }

    override suspend fun getMovieReviewStream(id: Int): Result<Flow<List<Review>>> {
        val reviewListLocal = movieEntityDao.getMovieReviewById(id).first()
        if (reviewListLocal.isEmpty()) {
            return try {
                getMovieReviewRemote(id)
                Result.Success(movieEntityDao.getMovieReviewById(id)
                    .map { reviewEntityList -> reviewEntityList.map { reviewEntity -> reviewEntity.toDomainReview() } })
            } catch (e: HttpException) {
                Result.Error(message = "Oops Something went wrong! Try again later.")
            } catch (e: IOException) {
                Result.Error(message = "Couldn't reach server. Check your internet connection")
            }
        }
        return Result.Success(movieEntityDao.getMovieReviewById(id)
            .map { reviewEntityList -> reviewEntityList.map { reviewEntity -> reviewEntity.toDomainReview() } })
    }

    override suspend fun getFavouriteMoviesStream(): Result<Flow<List<Movie>>> {
        return try {
            Result.Success(movieEntityDao.getFavouriteMovies().map { movieEntityList ->
                movieEntityList.map { movieEntity ->
                    movieEntity.toDomainMovie(movieEntity.category)
                }
            })
        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")
        } catch (e: IOException) {
            Result.Error("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun updateFavouriteStatus(movie: FavouriteBody): Boolean {
        val response = moviesApiService.addMovieToFavourite(RAW_BODY = movie)
        if (response.isSuccessful) {
            movieEntityDao.updateFavouriteStatus(movie.mediaId)
            return true
        }
        return false
    }

    private suspend fun getCastAndCrewRemote(
        movieId: Int
    ) {
        val creditsRemote = moviesApiService.getCastAndCrew(movieId)
        val castMembers = creditsRemote.cast
        castMembers.forEach { cast ->
            movieEntityDao.insertCastAndCrew(cast.toEntityCastAndCrew(movieId))
        }
        val crewMembers = creditsRemote.crew
        crewMembers.forEach { crew ->
            movieEntityDao.insertCastAndCrew(crew.toEntityCastAndCrew(movieId))
        }
    }

    private suspend fun getMovieReviewRemote(
        movieId: Int
    ) {
        val reviewRemote = moviesApiService.getReviewsById(movieId)
        reviewRemote.results
        movieEntityDao.insertMovieReview(reviewRemote.results.map { it.toEntityReview(movieId) })
    }

    private suspend fun fetchAndInsertExtraMovieDetails(
        movieId: Int,
    ) {
        val extraMovieDetailsRemote = moviesApiService.getExtraMovieDetailsById(movieId)
        val recommendationList = moviesApiService.getRecommendationList(movieId)
        val recommendedMovieListIds = mutableListOf<Int>()
        if (recommendationList.results.isNotEmpty()) {
            recommendationList.results.map { recommendedMovieListIds.add(it.id ?: -1) }
            recommendedMovieListIds.forEach { recommendedMovieId ->
                coroutineScope {
                    launch {
                        fetchAndInsertMovie(recommendedMovieId)
                    }
                }
            }
        }
        val imageList = moviesApiService.getImages(movieId)
        val postersList = imageList.posters.map { it.filePath }
        val backdropsList = imageList.backdrops.map { it.filePath }
        val logosList = imageList.logos.map { it.filePath }
        val postersPathList = postersList + logosList
        val videoPathRemote = moviesApiService.getVideo(movieId)
        val videoPath = videoPathRemote.results.find { it.type == "Trailer" }?.key ?: ""
        movieEntityDao.insertExtraMovieDetails(
            extraMovieDetailsRemote.toEntityExtraMovieDetails(
                recommendationMoviesList = recommendedMovieListIds,
                postersPathList = postersPathList.toString(),
                backdropsPathList = backdropsList.toString(),
                videoLink = videoPath,
            )
        )
    }

    private suspend fun fetchAndInsertMovie(
        movieId: Int,
    ) {
        val movie = moviesApiService.getExtraMovieDetailsById(movieId)
        val genreNames = movie.genres?.map { it.name }?.toList() ?: emptyList()
        val genreIds = movie.genres?.map { it.id }?.toList() ?: emptyList()
        val movieEntity = movie.toEntityMovie(
            "",
            genreNames.toString(),
            genreIds.toString(),
            movie.isFavourite,
        )
        movieEntityDao.upsertMovie(movieEntity)
    }
}