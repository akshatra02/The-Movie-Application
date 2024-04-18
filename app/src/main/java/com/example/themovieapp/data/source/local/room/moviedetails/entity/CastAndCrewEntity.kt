package com.example.themovieapp.data.source.local.room.moviedetails.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.themovieapp.domain.model.CastAndCrew

@Entity(
    tableName = "cast_and_crew_table",
    primaryKeys = ["movie_id", "person_id" ],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"]
        )
    ]
)
data class CastAndCrewEntity(
    @ColumnInfo("person_id") val personId:Int,
    @ColumnInfo("movie_id") val movieId: Int,
    val name: String,
    val role: String,
    val knowForDepartment: String,
    val order: Int,
    @ColumnInfo("is_cast") val isCast: Boolean,
    @ColumnInfo("profile_path") val profilePath: String,
){
    fun toCastAndCrew() : CastAndCrew = CastAndCrew(
        personId = personId,
        movieId = movieId,
        name = name,
        role = role,
        knowForDepartment = knowForDepartment,
        order = order,
        profilePath = profilePath,
        isCast = isCast,
    )
}
