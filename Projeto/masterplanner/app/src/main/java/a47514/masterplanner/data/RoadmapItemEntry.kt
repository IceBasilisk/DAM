package a47514.masterplanner.data

/**
 * Represents a roadmap item, either a task or a duration.
 */
data class RoadmapItemEntry(
    /**
     * Type of item (task or duration).
     */
    val type: String = "task",
    /**
     * ID of the task (only for type="task").
     */
    val taskId: String = "",
    val taskName: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    /**
     * Duration value (e.g. "3H 20M") (only for type="duration").
     */
    val durationLabel: String = ""
)