package a47514.masterplanner.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Roadmap value.
 */
data class Roadmap(
    val id: String = "",
    val title: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    /**
     * List of roadmap items.
     */
    val itemEntries: List<RoadmapItemEntry> = emptyList(),
    /**
     * Sort order of the roadmap.
     */
    val sortOrder: Int = 0,
    /**
     * Timestamp of when the roadmap was created.
     */
    @ServerTimestamp val timestamp: Timestamp? = null
)