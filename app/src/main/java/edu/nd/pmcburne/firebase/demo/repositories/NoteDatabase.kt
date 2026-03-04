package edu.nd.pmcburne.firebase.demo.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.nd.pmcburne.firebase.demo.data.Note

/**
 * Database class for offline persistence of notes
 */

@Database(entities = [Note::class], version = 1)
@TypeConverters(FirebaseRoomConverters::class) // specifying type converters
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database" // The name of your database file
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
