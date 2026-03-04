package edu.nd.pmcburne.firebase.demo.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.nd.pmcburne.firebase.demo.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    /**
     * Returns a flow of all notes in the database.
     */
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Inserts a list of notes into the database. If a note with the same ID already exists,
     * it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<Note>)

    /**
     * Deletes a note from the database by its ID.
     */
    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: String)

    /**
     * Deletes all notes from the database.
     */
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    /**
     * Deletes all notes from the database and then inserts the provided list of notes.
     */
    @Transaction
    suspend fun syncNotes(notes: List<Note>) {
        deleteAllNotes()
        insertNotes(notes)
    }
}
