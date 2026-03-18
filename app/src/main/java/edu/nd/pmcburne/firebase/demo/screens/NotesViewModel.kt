package edu.nd.pmcburne.firebase.demo.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.firebase.demo.data.Note
import edu.nd.pmcburne.firebase.demo.repositories.AuthRepository
import edu.nd.pmcburne.firebase.demo.repositories.FirebaseNoteRepository
import edu.nd.pmcburne.firebase.demo.repositories.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(
    private val firebaseNoteRepository: NoteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    val notes: StateFlow<List<Note>> = firebaseNoteRepository.getCachedNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var newNoteText by mutableStateOf("")
    
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            authRepository.currentUserFlow.collectLatest { user ->
                if (user != null) {
                    try {
                        firebaseNoteRepository.getNotes().collect {}
                    } catch (_: Exception) {
                        // Handle potential stream errors here if needed
                    }
                }
            }
        }
    }

    fun onNewNoteTextChange(text: String) {
        newNoteText = text
    }

    val currentUserUid: String?
        get() = authRepository.currentUser?.uid

    fun addNote() {
        if (!authRepository.isUserSignedIn()) return
        val currentUser = authRepository.currentUser!!
        if (newNoteText.isBlank()) return

        val note = Note(
            authorId = currentUser.uid,
            authorName = currentUser.email ?: "Anonymous",
            text = newNoteText
        )

        viewModelScope.launch {
            try {
                firebaseNoteRepository.addNote(note)
                newNoteText = ""
            } catch (e: Exception) {
                errorMessage = "Failed to add note: ${e.localizedMessage}"
            }
        }
    }

    fun deleteNote(note: Note) {
        val currentUser = authRepository.currentUser ?: return
        if (note.authorId == currentUser.uid) {
            viewModelScope.launch {
                try {
                    firebaseNoteRepository.deleteNote(note.id)
                } catch (e: Exception) {
                    errorMessage = "Failed to delete note: ${e.localizedMessage}"
                }
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun signOut() {
        authRepository.signOut()
    }
}
