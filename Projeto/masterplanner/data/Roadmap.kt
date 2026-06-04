package a47514.masterplanner.data

data class Roadmap(
    val id: String = "",
    val title: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    val tasks: List<Task> = emptyList()
)
