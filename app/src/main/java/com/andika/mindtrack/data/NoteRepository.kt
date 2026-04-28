package com.andika.mindtrack.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    fun getNoteByDate(date: String): Flow<Note?> = noteDao.getNoteByDate(date)
    
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun upsertNote(note: Note) = noteDao.upsertNote(note)
}
