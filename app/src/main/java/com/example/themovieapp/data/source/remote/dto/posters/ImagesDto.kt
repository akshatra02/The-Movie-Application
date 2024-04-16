package com.example.themovieapp.data.source.remote.dto.posters

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class ImagesDto(
    val id: Int,
    val backdrops: List<FilePath>,
    val logos: List<FilePath>,
    val posters: List<FilePath>
)

@Serializable
data class FilePath(
    @SerializedName("file_path")val filePath: String,
)