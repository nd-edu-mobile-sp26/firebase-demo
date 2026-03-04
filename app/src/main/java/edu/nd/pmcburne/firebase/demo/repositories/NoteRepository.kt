package edu.nd.pmcburne.firebase.demo.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.nd.pmcburne.firebase.demo.data.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await

class NoteRepository(
    private val firestore: FirebaseFirestore,
    private val noteDao: NoteDao
) {
    private val notesCollection = firestore.collection("notes")

    /**
     * Returns a subscribe list of notes from Firestore. When new notes are received, they are
     * added to the local Room database for offline access.
     */
    fun getNotes(): Flow<List<Note>> = callbackFlow {
        val subscription = notesCollection
            .orderBy("timestamp", Query.Direction.DESCENDING) // sort by most recent to oldest
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val notes = snapshot.toObjects(Note::class.java)
                    trySend(notes)
                }
            }
        awaitClose { // stay open and keep listening for events until the flow is closed
            subscription.remove() // when the flow is closed, remove the listener
        }
    }.onEach { notes -> // everytime the flow emits a new value, sync it's data with the database
        noteDao.syncNotes(notes)
    }

    /**
     * Returns a cached list of notes from the local Room database for offline mode.
     */
    fun getCachedNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    suspend fun addNote(note: Note) {
        // Generate the ID manually so we can persist it immediately and correctly
        val docRef = notesCollection.document() // this generates the id
        val noteWithId = note.copy(id = docRef.id) // this sets our new note's ID to the generated one
        docRef.set(noteWithId).await()
    }

    /**
     * Delete a note by its ID from Firestore
     */
    suspend fun deleteNote(noteId: String) {
        if (noteId.isEmpty()) return
        // delete from Firestore
        notesCollection.document(noteId).delete().await()

        // deleting from local repository for immediate UI Feedback - while not necessary, this
        // will give much faster feedback than waiting for Firestore to send fresh data
        noteDao.deleteNoteById(noteId)
    }
}
