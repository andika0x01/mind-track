package com.andika.mindtrack

import android.app.Application
import com.andika.mindtrack.data.NoteDatabase
import com.andika.mindtrack.data.NoteRepository

class NoteApplication : Application() {
    val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}
