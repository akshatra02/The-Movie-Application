package com.example.themovieapp.data.source.local.room.moviedetails.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_table")
data class SyncEntity (
    @PrimaryKey
    val category:String,
    val page:Int = 0
)