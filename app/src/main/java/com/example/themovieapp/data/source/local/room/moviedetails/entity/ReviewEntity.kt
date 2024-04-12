package com.example.themovieapp.data.source.local.room.moviedetails.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.themovieapp.domain.model.Review

@Entity(tableName = "review_table",
    primaryKeys = ["id","movie_id"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"]
        )
    ]
)
data class ReviewEntity(

    @ColumnInfo("movie_id") val movieId: Int,
    val id: String,
    val author: String,
    val content: String,
    val url: String,
    val rating: Int,
    @ColumnInfo("avatar_path") val avatarPath: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String

){
    fun toReview():Review = Review(
        movieId = movieId,
        id = id,
        author = author,
        content = content,
        url = url,
        updatedAt = updatedAt,
        rating = rating,
        avatarPath = avatarPath,
        createdAt = createdAt,
    )
}
