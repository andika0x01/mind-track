package com.andika.mindtrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: Note): Long

    @Query("SELECT * FROM notes WHERE date = :date")
    fun getNoteByDate(date: String): Flow<Note?>

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>
}
