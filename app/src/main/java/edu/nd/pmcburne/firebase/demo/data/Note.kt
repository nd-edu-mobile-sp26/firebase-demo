package edu.nd.pmcburne.firebase.demo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

@Entity(tableName = "notes") // RoomDB annotation for table name. Not used by FireStore
data class Note(
    @PrimaryKey  // This annotation is not used by Firestore, this is for RoomDB
    @DocumentId // Used by Firestore as the unique id of the document. Annotation not used by room db.
    val id: String = "",

    // The UID of the FirebaseUser who created the note
    val authorId: String = "",

    // The email or display name (optional, but helpful for UI)
    val authorName: String = "",

    val text: String = "",

    // The @ServerTimestamp annotation tells Firestore to generate the time of object creation when
    // the object is uploaded on the server side. This is so all users have consistent timing.
    @ServerTimestamp
    val timestamp: Timestamp? = null
)