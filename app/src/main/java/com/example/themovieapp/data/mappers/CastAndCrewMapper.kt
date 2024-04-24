package com.example.themovieapp.data.mappers

import com.example.themovieapp.data.source.local.room.moviedetails.entity.CastAndCrewEntity
import com.example.themovieapp.data.source.remote.dto.castandcrew.CastDto
import com.example.themovieapp.data.source.remote.dto.castandcrew.CrewDto
import com.example.themovieapp.domain.model.CastAndCrew

fun CastAndCrewEntity.toDomainCastAndCrew() : CastAndCrew = CastAndCrew(
    personId = personId,
    movieId = movieId,
    name = name,
    role = role,
    knowForDepartment = knowForDepartment,
    order = order,
    profilePath = profilePath,
    isCast = isCast,
)
fun CastDto.toEntityCastAndCrew(movieId : Int): CastAndCrewEntity =
    CastAndCrewEntity(
        personId = id,
        movieId = movieId,
        name = name,
        role = character ?: "",
        knowForDepartment = knownForDepartment ?: "",
        order = order ?: -1,
        profilePath = profilePath ?: "",
        isCast = true,
    )
fun CrewDto.toEntityCastAndCrew(movieId : Int): CastAndCrewEntity =
    CastAndCrewEntity(
        personId = id,
        movieId = movieId,
        name = name,
        role = job ?: "",
        knowForDepartment = knownForDepartment ?: "",
        order = -1,
        profilePath = profilePath ?: "",
        isCast = false
    )