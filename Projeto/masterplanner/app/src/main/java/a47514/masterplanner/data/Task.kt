package a47514.masterplanner.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Task(
    val id: String = "",
    val name: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    val durationMinutes: Int = 0,
    val sortOrder: Int = 0,
    @ServerTimestamp val timestamp: Timestamp? = null
)