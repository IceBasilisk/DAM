package a47514.masterplanner.data

// Firestore-friendly flat representation of one row in the editor
// type = "task" or "duration"
data class RoadmapItemEntry(
    val type: String = "task",   // "task" | "duration"
    val taskId: String = "",     // only for type="task"
    val taskName: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    val durationLabel: String = "" // only for type="duration", e.g. "3H 20M"
)