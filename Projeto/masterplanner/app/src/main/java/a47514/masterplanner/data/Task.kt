package a47514.masterplanner.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Task value.
 */
data class Task(
    val id: String = "",
    val name: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    val durationMinutes: Int = 0,
    /**
     * Sort order of the task.
     */
    val sortOrder: Int = 0,
    /**
     * Timestamp of when the task was created.
     */
    @ServerTimestamp val timestamp: Timestamp? = null
)