package com.example.themovieapp.data.repository

import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultMovieDetailsRepository @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
) : MovieDetailsRepository {
    override suspend fun getExtraMovieDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails>> {
        var message: String? = null

        val movieDetails = movieEntityDao.getMovieDetailsAndExtraById(id)?.first()
        val hasExtraMovieDetails = movieDetails?.movieId
        if (hasExtraMovieDetails == null) {
            try {
                fetchAndInsertExtraMovieDetails(moviesApiService, movieEntityDao, id)

            } catch (e: HttpException) {
                message = "Oops Something went wrong! Try again later."

            } catch (e: IOException) {
                message = "Couldn't reach server. Check your internet connection"
            }
        }
        if (message == null) {
            return Result.Success(movieEntityDao.getMovieDetailsAndExtraById(id)
                ?.map { it.toMovieDetailsAndExtraDetails() })
        }
        return Result.Error(message.toString())
    }


    override suspend fun getMovieAndExtraDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails?>> {
        return try {
            val getMovieById = movieEntityDao.getMovieDetailsAndExtraById(id)
            (Result.Success(getMovieById?.map { it.toMovieDetailsAndExtraDetails() }))
        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")


        } catch (e: IOException) {
            Result.Error("Couldn't reach server. Check your internet connection")
        }


    }

    override suspend fun getCastAndCrewStream(id: Int): Result<Flow<List<CastAndCrew>>> {
        var message: String? = null
        val castAndCrewEntityList = movieEntityDao.getCastByMovieId(id).first()
        if (castAndCrewEntityList.isEmpty()) {
            try {
                getCastAndCrew(moviesApiService, movieEntityDao, id)

            } catch (e: HttpException) {
                message = "Oops Something went wrong! Try again later."
            } catch (e: IOException) {

                message = "Couldn't reach server. Check your internet connection"

            }
        }
        return if (message == null) {
            Result.Success(movieEntityDao.getCastByMovieId(id)
                .map { resultCastAndCrewEntityList -> resultCastAndCrewEntityList.map { castAndCrewEntity -> castAndCrewEntity.toCastAndCrew() } })
        } else {
            Result.Error(message.toString())
        }
    }

    override suspend fun getMovieReviewStream(id: Int): Result<Flow<List<Review>>> {
        var message: String? = null

        val reviewListLocal = movieEntityDao.getMovieReviewById(id).first()
        if (reviewListLocal.isEmpty()) {
            try {
                getMovieReview(moviesApiService, movieEntityDao, id)

            } catch (e: HttpException) {
                message = "Oops Something went wrong! Try again later."
            } catch (e: IOException) {
                message = "Couldn't reach server. Check your internet connection"

            }
        }
        if (message == null) {
            return Result.Success(movieEntityDao.getMovieReviewById(id)
                .map { reviewEntityList -> reviewEntityList.map { reviewEntity -> reviewEntity.toReview() } })

        }
        return Result.Error(message.toString())

    }

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

private suspend fun getMovieReview(
    moviesApiService: MoviesApi, movieEntityDao: MovieEntityDao, movieId: Int
) {
    val reviewRemote = moviesApiService.getReviewsById(movieId)
    reviewRemote.results
    movieEntityDao.insertMovieReview(reviewRemote.results.map { it.toReviewEntity(movieId) })

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
    val postersPathList = postersList + logosList
    val getVideoPath = moviesApiService.getVideo(movieId)
    val videoPath = getVideoPath.results.find { it.type == "Trailer" }?.key ?: ""
    val upsertExtraMovieDetailsTable = movieEntityDao.upsertExtraMovieDetails(
        getExtraMovieDetailsRemote.toExtraMovieDetailsEntity(
            recommendedMovieListIds,
            postersPathList = postersPathList.toString(),
            backdropsPathList = backdropsList.toString(),
            videoPath,
        )
    )
}
