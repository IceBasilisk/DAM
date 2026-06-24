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
import com.google.firebase.firestore.FieldValue

class RoadmapViewModel : ViewModel() {
    /**
     * Private roadmap list
     */
    private val _roadmaps = MutableStateFlow<List<Roadmap>>(emptyList())

    /**
     * Roadmap list
     */
    val roadmaps: StateFlow<List<Roadmap>> = _roadmaps

    /**
     * Private task list
     */
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    /**
     * Task list
     */
    val tasks: StateFlow<List<Task>> = _tasks

    /**
     * Private list of tasks from library
     */
    private val _libraryTasks = MutableStateFlow<List<Task>>(emptyList())

    /**
     * List of tasks from library
     */
    val libraryTasks: StateFlow<List<Task>> = _libraryTasks

    /**
     * Private current roadmap
     */
    private val _currentRoadmap = MutableStateFlow<Roadmap?>(null)

    /**
     * Current roadmap
     */
    val currentRoadmap: StateFlow<Roadmap?> = _currentRoadmap

    /**
     * Private list of suggested task names
     */
    private val _suggestedTaskNames = MutableStateFlow<List<String>>(emptyList())

    /**
     * List of suggested task names
     */
    val suggestedTaskNames: StateFlow<List<String>> = _suggestedTaskNames

    /**
     * Private flag for when task name suggestions are being generated
     */
    private val _isSuggesting = MutableStateFlow(false)

    /**
     * Flag for when task name suggestions are being generated
     */
    val isSuggesting: StateFlow<Boolean> = _isSuggesting

    /**
     * Private flag, attempted name generation
     */
    private val _hasSuggestionBeenAttempted = MutableStateFlow(false)

    /**
     * Flag, attempted name generation
     */
    val hasSuggestionBeenAttempted: StateFlow<Boolean> = _hasSuggestionBeenAttempted

    /**
     * Private flag for network connectivity
     */
    private val _isOnline = MutableStateFlow(true)

    /**
     * Flag for network connectivity
     */
    val isOnline: StateFlow<Boolean> = _isOnline

    // Keep track of the active per-roadmap listeners so we can detach them
    // when switching to a different roadmap (or away from the editor).

    /**
     * Firestore Listener, roadmap data
     */
    private var roadmapDocListener: com.google.firebase.firestore.ListenerRegistration? = null

    /**
     * Firestore Listener, task data
     */
    private var tasksListener: com.google.firebase.firestore.ListenerRegistration? = null

    /**
     * RoadmapId of current roadmap being listened to
     */
    private var listeningToRoadmapId: String? = null

    init {
        listenToTaskLibrary()
    }

    /**
     * Starts Firestore listeners to fetch roadmap data updates.
     * Updates _roadmaps info with snapshot information.
     */
    fun listenToRoadmaps() {
        // Gets Roadmap info from 'users/{uid}/roadmaps' via SnapshotListener
        // Listens forever for database changes (create, alter, delete)
        Utility.collectionReferenceForRoadmaps
            .addSnapshotListener { snapshot, error ->
                // Return only callback execution in case of error or lack of snapshot
                if (error != null || snapshot == null) return@addSnapshotListener
                // Documents contain roadmap data, so convert them to Kotlin objects
                val list = snapshot.documents
                    .mapNotNull { doc -> doc.toObject<Roadmap>()?.copy(id = doc.id) }
                    .sortedWith(compareBy({ it.sortOrder }, { -(it.timestamp?.seconds ?: 0L) }))
                _roadmaps.value = list
            }
    }

    /**
     * List of tasks in a specific roadmap. Same as listenToRoadmaps() but for tasks. Called when opening a roadmap.
     */
    fun listenToTasks(roadmapId: String) {
        // Without this, LaunchedEffect re-running would tear down the listeners
        if (listeningToRoadmapId == roadmapId) return

        // Detach listeners to prevent future updates from triggering
        roadmapDocListener?.remove()
        tasksListener?.remove()
        listeningToRoadmapId = roadmapId

        // New clean state for the new roadmap
        _currentRoadmap.value = null
        _tasks.value = emptyList()

        // Also fetch the roadmap document itself for the title
        roadmapDocListener = Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                _currentRoadmap.value = snapshot.toObject<Roadmap>()?.copy(id = snapshot.id)
            }

        // Fetch tasks from the roadmap's sub-collection, order by timestamp
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

    /**
     * Stops Firestore listeners to roadmap data updates.
     */
    fun stopListeningToTasks() {
        roadmapDocListener?.remove()
        tasksListener?.remove()
        roadmapDocListener = null
        tasksListener = null
        listeningToRoadmapId = null
        _currentRoadmap.value = null
        _tasks.value = emptyList()
    }


    /**
     * Saves a task to the roadmap's sub-collection.
     */
    fun saveTask(roadmapId: String, task: Task, onResult: (Task?) -> Unit) {
        val colRef = Utility.collectionReferenceForTasks(roadmapId)
        val docRef = if (task.id.isEmpty()) colRef.document() else colRef.document(task.id)
        val taskToSave = if (task.id.isEmpty()) task.copy(id = docRef.id) else task
        docRef.set(taskToSave)
            .addOnSuccessListener { onResult(taskToSave) }
            .addOnFailureListener { onResult(null) }
    }

    /**
     * Deletes a task from the roadmap's sub-collection.
     */
    fun deleteTask(roadmapId: String, taskId: String, onResult: (Boolean) -> Unit) {
        Utility.collectionReferenceForTasks(roadmapId)
            .document(taskId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /**
     * Delete all tasks inside a roadmap's sub-collection, then optionally the roadmap itself.
     */
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

    /**
     * Delete a task from the task library.
     */
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

    /**
     * Saves a roadmap to the roadmap collection.
     */
    fun saveRoadmap(roadmap: Roadmap, onResult: (Boolean, String) -> Unit) {
        val colRef = Utility.collectionReferenceForRoadmaps
        val docRef = if (roadmap.id.isEmpty()) colRef.document() else colRef.document(roadmap.id)
        val roadmapToSave = if (roadmap.id.isEmpty()) roadmap.copy(id = docRef.id) else roadmap
        docRef.set(roadmapToSave)
            .addOnSuccessListener { onResult(true, docRef.id) }
            .addOnFailureListener { onResult(false, "") }
    }

    /**
     * Deletes a roadmap from the roadmap collection.
     */
    fun deleteRoadmap(roadmapId: String, onResult: (Boolean) -> Unit) {
        Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /**
     * Listens to the task library for changes.
     */
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

    /**
     * Saves a task to the task library.
     */
    fun saveTaskToLibrary(task: Task, onResult: (Boolean) -> Unit) {
        val colRef = Utility.collectionReferenceForTaskLibrary
        val docRef = if (task.id.isEmpty()) colRef.document() else colRef.document(task.id)
        val taskToSave = if (task.id.isEmpty()) task.copy(id = docRef.id) else task
        docRef.set(taskToSave)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /**
     * Saves a list of roadmap items to the roadmap's sub-collection.
     */
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

    /**
     * Adds new task to the roadmap's persisted item order. Used when a task is created inside a Roadmap ("Forge New Task"). Puts task immediately in order.
     */
    fun addTaskToRoadmapItems(roadmapId: String, task: Task, onResult: (Boolean) -> Unit) {
        val entry = RoadmapItemEntry(
            type = "task",
            taskId = task.id,
            taskName = task.name,
            iconName = task.iconName,
            colorHex = task.colorHex
        )
        Utility.collectionReferenceForRoadmaps
            .document(roadmapId)
            .update("itemEntries", FieldValue.arrayUnion(entry))
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /**
     * Get AI task name suggestions.
     */
    fun fetchTaskSuggestions(roadmapTitle: String) {
        if (roadmapTitle.isBlank()) return
        viewModelScope.launch {
            _isSuggesting.value = true
            _hasSuggestionBeenAttempted.value = false
            _suggestedTaskNames.value = emptyList()
            _suggestedTaskNames.value = GeminiService.suggestTaskNames(roadmapTitle)
            _hasSuggestionBeenAttempted.value = true
            _isSuggesting.value = false
        }
    }

    /**
     * Clears AI task name suggestions.
     */
    fun clearSuggestions() {
        _suggestedTaskNames.value = emptyList()
        _hasSuggestionBeenAttempted.value = false  // ← reset so message disappears after picking
    }

    /**
     * Returns the current network connectivity status.
     */
    fun startMonitoringNetwork(context: Context) {
        viewModelScope.launch {
            NetworkMonitor.observe(context).collect { online ->
                _isOnline.value = online
            }
        }
    }

    /**
     * Reorders the roadmap list.
     */
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

    /**
     * Reorders the task list.
     */
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