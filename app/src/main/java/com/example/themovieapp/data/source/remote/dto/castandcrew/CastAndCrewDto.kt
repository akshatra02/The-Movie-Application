package com.example.themovieapp.data.source.remote.dto.castandcrew

import kotlinx.serialization.Serializable

@Serializable
data class CastAndCrewDto(
    val cast: List<CastDto>,
    val crew: List<CrewDto>,
    val id: Int
)