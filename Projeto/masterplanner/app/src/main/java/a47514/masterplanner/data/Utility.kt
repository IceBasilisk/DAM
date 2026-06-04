package a47514.masterplanner.data

import android.content.Context
import android.widget.Toast
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

    val collectionReferenceForNotes: CollectionReference
        get() {
            val currentUser: FirebaseUser? =
                checkNotNull(FirebaseAuth.getInstance().getCurrentUser())
            return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser!!.getUid()).collection("my_notes")
        }

    fun timestampToString(timestamp: Timestamp): String {
        return SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate())
    }
}