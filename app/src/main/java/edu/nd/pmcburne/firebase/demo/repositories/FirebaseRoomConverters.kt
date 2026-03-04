package edu.nd.pmcburne.firebase.demo.repositories

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.Date

/**
 * This class is used to convert Timestamps in Firestore to Timestamps in RoomDB's SQLite database
 * and vice versa.
 */
class FirebaseRoomConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(Date(it)) }
    }

    @TypeConverter
    fun dateToTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.toDate()?.time
    }
}
