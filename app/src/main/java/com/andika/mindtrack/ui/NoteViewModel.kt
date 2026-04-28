package com.andika.mindtrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.mindtrack.data.Note
import com.andika.mindtrack.data.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _currentNoteContent = MutableStateFlow("")
    val currentNoteContent: StateFlow<String> = _currentNoteContent.asStateFlow()

    private var saveJob: Job? = null

    val allNotes: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getNoteByDate(date: String) = repository.getNoteByDate(date)

    fun updateContent(date: String, content: String) {
        _currentNoteContent.value = content
        
        // Debounce logic
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(2000)
            repository.upsertNote(Note(date, content))
        }
    }

    fun setInitialContent(content: String) {
        _currentNoteContent.value = content
    }
}
