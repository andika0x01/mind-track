package com.andika.mindtrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val date: String, // ISO format yyyy-MM-dd
    val content: String
)
