package com.example.themovieapp.data.mappers

import com.example.themovieapp.data.source.local.room.moviedetails.entity.ExtraMovieDetailsEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.GenreEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieDetailsAndExtraDetailsData
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.example.themovieapp.data.source.remote.dto.extramoviedetails.ExtraMovieDetailsDto
import com.example.themovieapp.data.source.remote.dto.movielist.GenreDto
import com.example.themovieapp.data.source.remote.dto.movielist.MovieDto
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails

fun MovieDetailsAndExtraDetailsData.toDomainMovieAndExtraDetails(): MovieDetailsAndExtraDetails = MovieDetailsAndExtraDetails(
    adult = adult ,
    backdropPath = backdropPath,
    originalLanguage = originalLanguage ,
    originalTitle = originalTitle,
    overview = overview ,
    popularity = popularity ,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    id = id,
    category = category,
    genreIds = try {
        genreIds.split(",").map { it.toInt() }
    }catch (e : Exception){
        null
    },
    genreNames = genreNames.split(","),
    isFavourite = isFavourite,
    movieId =  movieId,
    budget = budget,
    homepage = homepage,
    imdbId = imdbId,
    revenue = revenue,
    runtime = runtime,
    status = status,
    tagline = tagline,
    recommendationMoviesList =
    try {
        recommendationMoviesList?.replace("[","")?.replace("]","")?.split(", ") ?.map { it.toInt() }
    } catch (e: Exception) {
        null
    },
    videoLink = videoLink,
    postersPathList = postersPathList?.replace("[","")?.replace("]","")?.split(", "),
    backdropsPathList = backdropsPathList?.replace("[","")?.replace("]","")?.split(", "),
)

fun MovieEntity.toDomainMovie(
    category: String = ""
): Movie {
    return Movie(
        adult = adult ,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage ,
        originalTitle = originalTitle,
        overview = overview ,
        popularity = popularity ,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        id = id,
        category = category,
        genreIds = try {
            genreIds?.split(",")?.map { it.toInt() }
        }catch (e : Exception){
            null
        },
        genreNames = genreNames.split(","),
        isFavourite = isFavourite,


        )
}

fun MovieDto.toEntityMovie(category: String, genreNames: String, isFavourite: Boolean): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        title = title ?: "",
        video = video ?: false,
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
        id = id ?: 0,
        category = category,
        genreIds = try {
            genreIds.toString()
        } catch (e: Exception) {
            null
        },
        genreNames = genreNames,
        isFavourite = isFavourite,
    )
}

fun ExtraMovieDetailsDto. toEntityExtraMovieDetails(recommendationMoviesList: List<Int>, postersPathList: String, backdropsPathList: String, videoLink: String) = ExtraMovieDetailsEntity(
    movieId = id ?: 0,
    budget = budget ?: 0,
    homepage = homepage ?: "",
    imdbId = imdbId ?: "",
    revenue = revenue ?: 0,
    runtime = runtime ?: 0,
    status = status ?: "",
    tagline = tagline ?: "",
    recommendationMoviesList = recommendationMoviesList.toString(),
    postersPathList = postersPathList,
    backdropsPathList = backdropsPathList,
    videoLink = videoLink,
)
fun ExtraMovieDetailsDto.toEntityMovie(
    category: String,
    genreNames: String,
    genreIds: String,
    isFavourite: Boolean,
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        title = title ?: "",
        video = video ?: false,
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
        id = id ?: 0,
        category = category,
        genreIds = genreIds,
        genreNames = genreNames,
        isFavourite = isFavourite,
    )
}

fun GenreDto.toEntityGenre() = GenreEntity(
     genreId = id,
     genreName = name
)