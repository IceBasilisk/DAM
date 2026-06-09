package a47514.masterplanner.data

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat


object Utility {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val collectionReferenceForRoadmaps: CollectionReference
        get() {
            val uid = checkNotNull(FirebaseAuth.getInstance().currentUser).uid
            return FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("roadmaps")
        }

    fun collectionReferenceForTasks(roadmapId: String): CollectionReference {
        return collectionReferenceForRoadmaps
            .document(roadmapId)
            .collection("tasks")
    }

    val collectionReferenceForTaskLibrary: CollectionReference
        get() {
            val uid = checkNotNull(FirebaseAuth.getInstance().currentUser).uid
            return FirebaseFirestore.getInstance()
                .collection("users").document(uid)
                .collection("task_library")
        }

    fun timestampToString(timestamp: Timestamp): String {
        return SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate())
    }

    fun iconFromName(name: String): ImageVector = when (name) {
        "Waves"   -> Icons.Default.Waves
        "Flag"    -> Icons.Default.Flag
        "Ship"    -> Icons.Default.DirectionsBoat
        "Gem"     -> Icons.Default.Diamond
        "Compass" -> Icons.Default.Explore
        "Anchor"  -> Icons.Default.Anchor
        "Sails"   -> Icons.Default.Sailing
        "Chest"   -> Icons.Default.Inventory
        else      -> Icons.Default.Flag
    }
}