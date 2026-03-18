package edu.nd.pmcburne.firebase.demo.repositories

import edu.nd.pmcburne.firebase.demo.data.Note
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining note storage operations.
 */
interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    fun getCachedNotes(): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun deleteNote(noteId: String)
}
