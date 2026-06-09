package a47514.masterplanner.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Roadmap(
    val id: String = "",
    val title: String = "",
    val iconName: String = "",
    val colorHex: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null
)