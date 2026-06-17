package a47514.masterplanner.data

import a47514.masterplanner.ui.RoadmapItem
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

class RoadmapViewModel : ViewModel() {
    private val _roadmaps = MutableStateFlow<List<Roadmap>>(emptyList())
    val roadmaps: StateFlow<List<Roadmap>> = _roadmaps

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _libraryTasks = MutableStateFlow<List<Task>>(emptyList())
    val libraryTasks: StateFlow<List<Task>> = _libraryTasks

    private val _currentRoadmap = MutableStateFlow<Roadmap?>(null)
    val currentRoadmap: StateFlow<Roadmap?> = _currentRoadmap

    private val _suggestedTaskNames = MutableStateFlow<List<String>>(emptyList())
    val suggestedTaskNames: StateFlow<List<String>> = _suggestedTaskNames

    private val _isSuggesting = MutableStateFlow(false)
    val isSuggesting: StateFlow<Boolean> = _isSuggesting
    private val _hasSuggestionBeenAttempted = MutableStateFlow(false)
    val hasSuggestionBeenAttempted: StateFlow<Boolean> = _hasSuggestionBeenAttempted

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline

    // Keep track of the active per-roadmap listeners so we can detach them
    // when switching to a different roadmap (or away from the editor).
    private var roadmapDocListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var tasksListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var listeningToRoadmapId: String? = null

    init {
        listenToTaskLibrary()
    }
    fun listenToRoadmaps() {
        Utility.collectionReferenceForRoadmaps
            .addSnapshotListener { snapshot, error ->      // ← remove orderBy, sort client-side
                if (error != null || snapshot == null) return@addSnapshotListener
                val list = snapshot.documents
                    .mapNotNull { doc -> doc.toObject<Roadmap>()?.copy(id = doc.id) }
                    .sortedWith(compareBy({ it.sortOrder }, { -(it.timestamp?.seconds ?: 0L) }))
                _roadmaps.value = list
            }
    }

    // Call this when RoadMapEditorScreen opens for a specific roadmap
    // Pattern: same as listenToRoadmaps() but on the tasks sub-collection
    fun listenToTasks(roadmapId: String) {
        // Already listening to this roadmap — nothing to do. Without this guard,
        // recomposition (e.g. LaunchedEffect re-running) would tear down and
        // re-attach listeners unnecessarily.
        if (listeningToRoadmapId == roadmapId) return

        // Detach any listeners from the previously open roadmap so its data
        // can't keep flowing into _tasks / _currentRoadmap after we've moved on.
        roadmapDocListener?.remove()
        tasksListener?.remove()
        listeningToRoadmapId = roadmapId

        // Clear stale state immediately rather than waiting for the new
        // snapshot — otherwise the previous roadmap's tasks (and title) are
        // briefly, or persistently, shown under the new roadmap.
        _currentRoadmap.value = null
        _tasks.value = emptyList()

        // Also fetch the roadmap document itself for the title
        roadmapDocListener = Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                _currentRoadmap.value = snapshot.toObject<Roadmap>()?.copy(id = snapshot.id)
            }

        tasksListener = Utility.collectionReferenceForTasks(roadmapId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val list = snapshot.documents
                    .mapNotNull { doc -> doc.toObject<Task>()?.copy(id = doc.id) }
                    .sortedBy { it.timestamp?.seconds ?: 0L }
                _tasks.value = list
            }
    }

    // Call this when leaving the RoadMapEditor (e.g. on back) so listeners
    // don't keep running, and so the next roadmap opened starts from a clean slate.
    fun stopListeningToTasks() {
        roadmapDocListener?.remove()
        tasksListener?.remove()
        roadmapDocListener = null
        tasksListener = null
        listeningToRoadmapId = null
        _currentRoadmap.value = null
        _tasks.value = emptyList()
    }


    fun saveTask(roadmapId: String, task: Task, onResult: (Boolean) -> Unit) {
        val colRef = Utility.collectionReferenceForTasks(roadmapId)
        val docRef = if (task.id.isEmpty()) colRef.document() else colRef.document(task.id)
        val taskToSave = if (task.id.isEmpty()) task.copy(id = docRef.id) else task
        docRef.set(taskToSave)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun deleteTask(roadmapId: String, taskId: String, onResult: (Boolean) -> Unit) {
        Utility.collectionReferenceForTasks(roadmapId)
            .document(taskId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Delete all tasks inside a roadmap's sub-collection, then optionally the roadmap itself
    fun deleteRoadmapWithTasks(roadmapId: String, deleteTasks: Boolean, onResult: (Boolean) -> Unit) {
        if (!deleteTasks) {
            deleteRoadmap(roadmapId, onResult)
            return
        }
        // Fetch all tasks first, delete them, then delete the roadmap
        Utility.collectionReferenceForTasks(roadmapId)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = com.google.firebase.firestore.FirebaseFirestore.getInstance().batch()
                snapshot.documents.forEach { doc -> batch.delete(doc.reference) }
                batch.commit()
                    .addOnSuccessListener {
                        deleteRoadmap(roadmapId, onResult)
                    }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }

    // Delete a task from the library
    fun deleteLibraryTask(taskId: String, onResult: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Step 1: Delete from task_library
        Utility.collectionReferenceForTaskLibrary
            .document(taskId)
            .delete()
            .addOnFailureListener { onResult(false) }

        // Step 2: Fetch all roadmaps and remove the task from their itemEntries
        Utility.collectionReferenceForRoadmaps
            .get()
            .addOnSuccessListener { roadmapSnapshot ->
                val batch = db.batch()

                for (roadmapDoc in roadmapSnapshot.documents) {
                    val roadmap = roadmapDoc.toObject<Roadmap>() ?: continue
                    val updatedEntries = roadmap.itemEntries.filter { entry ->
                        entry.taskId != taskId
                    }
                    // Only update if something actually changed
                    if (updatedEntries.size != roadmap.itemEntries.size) {
                        batch.update(roadmapDoc.reference, "itemEntries", updatedEntries)
                    }
                }

                batch.commit()
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }

    fun saveRoadmap(roadmap: Roadmap, onResult: (Boolean, String) -> Unit) {
        val colRef = Utility.collectionReferenceForRoadmaps
        val docRef = if (roadmap.id.isEmpty()) colRef.document() else colRef.document(roadmap.id)
        val roadmapToSave = if (roadmap.id.isEmpty()) roadmap.copy(id = docRef.id) else roadmap
        docRef.set(roadmapToSave)
            .addOnSuccessListener { onResult(true, docRef.id) }
            .addOnFailureListener { onResult(false, "") }
    }

    fun deleteRoadmap(roadmapId: String, onResult: (Boolean) -> Unit) {
        Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun listenToTaskLibrary() {
        Utility.collectionReferenceForTaskLibrary
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject<Task>()?.copy(id = doc.id)
                    }
                    ?: emptyList()

                _libraryTasks.value = list
            }
    }

    fun saveTaskToLibrary(task: Task, onResult: (Boolean) -> Unit) {
        val colRef = Utility.collectionReferenceForTaskLibrary
        val docRef = if (task.id.isEmpty()) colRef.document() else colRef.document(task.id)
        val taskToSave = if (task.id.isEmpty()) task.copy(id = docRef.id) else task
        docRef.set(taskToSave)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun saveRoadmapItems(roadmapId: String, items: List<RoadmapItem>, onResult: (Boolean) -> Unit) {
        val entries = items.map { item ->
            when (item) {
                is RoadmapItem.Task -> RoadmapItemEntry(
                    type = "task",
                    taskId = item.taskId,
                    taskName = item.title,
                    iconName = item.iconName,
                    colorHex = item.colorHex
                )
                is RoadmapItem.Duration -> RoadmapItemEntry(
                    type = "duration",
                    durationLabel = item.time
                )
            }
        }
        Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .update("itemEntries", entries)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun fetchTaskSuggestions(roadmapTitle: String) {
        if (roadmapTitle.isBlank()) return
        viewModelScope.launch {
            _isSuggesting.value = true
            _hasSuggestionBeenAttempted.value = false
            _suggestedTaskNames.value = emptyList()
            _suggestedTaskNames.value = GeminiService.suggestTaskNames(roadmapTitle)
            _hasSuggestionBeenAttempted.value = true  // ← mark attempt complete
            _isSuggesting.value = false
        }
    }

    fun clearSuggestions() {
        _suggestedTaskNames.value = emptyList()
        _hasSuggestionBeenAttempted.value = false  // ← reset so message disappears after picking
    }

    fun startMonitoringNetwork(context: Context) {
        viewModelScope.launch {
            NetworkMonitor.observe(context).collect { online ->
                _isOnline.value = online
            }
        }
    }

    fun reorderRoadmaps(reordered: List<Roadmap>) {
        _roadmaps.value = reordered   // update UI immediately (optimistic)
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()
        reordered.forEachIndexed { index, roadmap ->
            val ref = Utility.collectionReferenceForRoadmaps.document(roadmap.id)
            batch.update(ref, "sortOrder", index)
        }
        batch.commit()   // fire-and-forget — Firestore handles retry
    }

    fun reorderLibraryTasks(reordered: List<Task>) {
        _libraryTasks.value = reordered
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()
        reordered.forEachIndexed { index, task ->
            val ref = Utility.collectionReferenceForTaskLibrary.document(task.id)
            batch.update(ref, "sortOrder", index)
        }
        batch.commit()
    }
}