package edu.nd.pmcburne.firebase.demo.repositories

import edu.nd.pmcburne.firebase.demo.data.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNoteRepository : NoteRepository {
    val notes = mutableListOf<Note>()

    override suspend fun addNote(note: Note) { notes.add(note) }
    override suspend fun deleteNote(noteId: String) {
        notes.removeIf { it.id == noteId }
    }
    // Return empty flows for initialization
    override fun getNotes(): Flow<List<Note>> = flowOf(emptyList())
    override fun getCachedNotes(): Flow<List<Note>> = flowOf(emptyList())
}