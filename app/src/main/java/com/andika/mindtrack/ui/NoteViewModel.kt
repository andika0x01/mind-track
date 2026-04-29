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
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _currentNoteContent = MutableStateFlow("")
    val currentNoteContent: StateFlow<String> = _currentNoteContent.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var saveJob: Job? = null

    val allNotes: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadNote(date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getNoteByDate(date).take(1).collect { note ->
                _currentNoteContent.value = note?.content ?: ""
                _isLoading.value = false
            }
        }
    }

    fun updateContent(date: String, content: String) {
        if (_currentNoteContent.value == content) return
        _currentNoteContent.value = content
        
        // Debounce logic
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(1000) // Reduced delay for better feel
            repository.upsertNote(Note(date, content))
        }
    }

    fun setInitialContent(content: String) {
        _currentNoteContent.value = content
    }
}
