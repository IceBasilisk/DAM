package a47514.masterplanner.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RoadmapViewModel : ViewModel() {
    private val _roadmaps = MutableStateFlow<List<Roadmap>>(emptyList())
    val roadmaps: StateFlow<List<Roadmap>> = _roadmaps

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _libraryTasks = MutableStateFlow<List<Task>>(emptyList())
    val libraryTasks: StateFlow<List<Task>> = _libraryTasks

    fun listenToRoadmaps() {
        Utility.collectionReferenceForRoadmaps
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val list = snapshot.documents
                    .mapNotNull { doc ->
                        doc.toObject<Roadmap>()?.copy(id = doc.id)
                    }
                    .sortedByDescending { it.timestamp?.seconds ?: 0L } // fallback for null timestamps

                _roadmaps.value = list
            }
    }

    // Call this when RoadMapEditorScreen opens for a specific roadmap
    // Pattern: same as listenToRoadmaps() but on the tasks sub-collection
    fun listenToTasks(roadmapId: String) {
        Utility.collectionReferenceForTasks(roadmapId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val list = snapshot.documents
                    .mapNotNull { doc ->
                        doc.toObject<Task>()?.copy(id = doc.id)
                    }
                    .sortedBy { it.timestamp?.seconds ?: 0L } // fallback for null timestamps

                _tasks.value = list
            }
    }

    // ── CRUD — Tasks ─────────────────────────────────────────────────────────

    // Pattern: NoteDetailsActivity.saveNoteToFirebase() — create path
    fun saveTask(roadmapId: String, task: Task, onResult: (Boolean) -> Unit) {
        val colRef = Utility.collectionReferenceForTasks(roadmapId)
        val docRef = if (task.id.isEmpty()) colRef.document() else colRef.document(task.id)
        val taskToSave = if (task.id.isEmpty()) task.copy(id = docRef.id) else task
        docRef.set(taskToSave)
            .addOnSuccessListener {
                Log.d("Firestore", "Roadmap saved: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Roadmap save failed", e)
            }
    }

    // Pattern: NoteDetailsActivity.deleteNoteFromFirebase()
    fun deleteTask(roadmapId: String, taskId: String, onResult: (Boolean) -> Unit) {
        Utility.collectionReferenceForTasks(roadmapId)
            .document(taskId)
            .delete()
            .addOnCompleteListener { result -> onResult(result.isSuccessful) }
    }

    // ── CRUD — Roadmaps ───────────────────────────────────────────────────────

    fun saveRoadmap(roadmap: Roadmap, onResult: (Boolean, String) -> Unit) {
        val colRef = Utility.collectionReferenceForRoadmaps
        val docRef = if (roadmap.id.isEmpty()) colRef.document() else colRef.document(roadmap.id)
        
        // Ensure the ID stored in the object matches the Firestore document ID
        val roadmapToSave = if (roadmap.id.isEmpty()) roadmap.copy(id = docRef.id) else roadmap

        docRef.set(roadmapToSave)
            .addOnSuccessListener {
                Log.d("Firestore", "Roadmap saved: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Roadmap save failed", e)
            }
    }

    fun deleteRoadmap(roadmapId: String, onResult: (Boolean) -> Unit) {
        Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .delete()
            .addOnCompleteListener { result -> onResult(result.isSuccessful) }
    }

    fun listenToTaskLibrary() {
        Utility.collectionReferenceForTaskLibrary
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject<Task>()?.copy(id = doc.id)
                    }
                    ?.sortedByDescending { it.timestamp?.seconds ?: 0L } // fallback for null timestamps
                    ?: emptyList()

                _libraryTasks.value = list
            }
    }

    fun saveTaskToLibrary(task: Task, onResult: (Boolean) -> Unit) {
        val colRef = Utility.collectionReferenceForTaskLibrary
        val docRef = if (task.id.isEmpty()) colRef.document() else colRef.document(task.id)
        val taskToSave = if (task.id.isEmpty()) task.copy(id = docRef.id) else task
        docRef.set(taskToSave).addOnCompleteListener { onResult(it.isSuccessful) }
    }
}