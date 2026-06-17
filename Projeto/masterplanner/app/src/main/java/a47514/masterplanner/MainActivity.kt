package a47514.masterplanner

import a47514.masterplanner.data.PremiumManager
import a47514.masterplanner.data.RoadmapViewModel
import a47514.masterplanner.data.Utility
import a47514.masterplanner.ui.*
import a47514.masterplanner.ui.theme.MasterPlannerTheme
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

enum class Screen {
    Splash, Login, SignUp, MainMenu, TaskLibrary, RoadMapEditor, CreateTask, Freemium
}

class MainActivity : ComponentActivity() {

    private val roadmapViewModel: RoadmapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .setSizeBytes(50L * 1024 * 1024) // 50MB cache
                    .build()
            )
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings

        MusicManager.start(this)
        PremiumManager.init(this)

        setContent {
            MasterPlannerTheme {
                var currentScreen by remember { mutableStateOf(Screen.Splash) }
                var currentRoadmapId by remember { mutableStateOf("") }
                var previousScreen by remember { mutableStateOf(Screen.MainMenu) }
                val auth = FirebaseAuth.getInstance()

                val navigate: (Screen) -> Unit = { screen ->
                    currentScreen = screen
                }

                when (currentScreen) {
                    Screen.Splash -> {
                        SplashScreen(onTimeout = {
                            currentScreen = if (auth.currentUser != null && auth.currentUser!!.isEmailVerified) {
                                Screen.MainMenu
                            } else {
                                Screen.Login
                            }
                        })
                    }
                    Screen.Login -> {
                        LoginScreen(
                            onLoginClick = { email, password ->
                                loginUser(email, password) { success ->
                                    if (success) currentScreen = Screen.MainMenu
                                }
                            },
                            onSignUpClick = { currentScreen = Screen.SignUp },
                            onResetPasswordClick = {
                                Utility.showToast(this@MainActivity, "Password reset functionality not implemented yet")
                            }
                        )
                    }
                    Screen.SignUp -> {
                        CreateAccountScreen(
                            onCreateAccountClick = { email, password, confirmPassword ->
                                createAccount(email, password, confirmPassword) { success ->
                                    if (success) currentScreen = Screen.Login
                                }
                            },
                            onLoginClick = { currentScreen = Screen.Login }
                        )
                    }
                    Screen.MainMenu -> {
                        LaunchedEffect(Unit) {
                            roadmapViewModel.listenToRoadmaps()
                            roadmapViewModel.startMonitoringNetwork(applicationContext)
                        }

                        MainMenuScreen(
                            roadmapViewModel = roadmapViewModel,
                            onLogoutClick = { auth.signOut(); currentScreen = Screen.Login },
                            onNavigate = { screen, roadmapId ->
                                currentRoadmapId = roadmapId ?: ""
                                navigate(screen)
                            }
                        )
                    }
                    Screen.TaskLibrary -> {
                        TaskLibraryScreen(
                            roadmapViewModel = roadmapViewModel,
                            onCreateTask = {
                                currentRoadmapId = "library"
                                previousScreen = Screen.TaskLibrary
                                currentScreen = Screen.CreateTask
                            },
                            onLogoutClick = { auth.signOut(); currentScreen = Screen.Login },
                            onNavigate = navigate
                        )
                    }
                    Screen.RoadMapEditor -> {
                        DisposableEffect(currentRoadmapId) {
                            roadmapViewModel.listenToTasks(currentRoadmapId)
                            onDispose { roadmapViewModel.stopListeningToTasks() }
                        }

                        RoadMapEditorScreen(
                            roadmapId = currentRoadmapId,
                            roadmapViewModel = roadmapViewModel,
                            onCreateTask = {
                                previousScreen = Screen.RoadMapEditor
                                currentScreen = Screen.CreateTask
                            },
                            onNavigate = navigate,
                            onBack = { currentScreen = Screen.MainMenu },
                            onFreemium = { currentScreen = Screen.Freemium }
                        )
                    }
                    Screen.CreateTask -> {
                        CreateTaskScreen(
                            roadmapId = currentRoadmapId,
                            roadmapViewModel = roadmapViewModel,
                            onBack = { currentScreen = previousScreen },
                            onNavigate = navigate
                        )
                    }

                    Screen.Freemium -> {
                        FreemiumScreen(
                            onDismiss = { currentScreen = Screen.MainMenu },
                            onNavigate = navigate
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resume()
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.release()
    }

    private fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        if (!validateLoginData(email, password)) {
            onComplete(false)
            return
        }

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (auth.currentUser?.isEmailVerified == true) {
                    onComplete(true)
                } else {
                    Utility.showToast(this, "Please verify your email.")
                    auth.signOut()
                    onComplete(false)
                }
            } else {
                Utility.showToast(this, task.exception?.localizedMessage ?: "Login failed")
                onComplete(false)
            }
        }
    }

    private fun createAccount(email: String, password: String, confirmPassword: String, onComplete: (Boolean) -> Unit) {
        if (!validateSignUpData(email, password, confirmPassword)) {
            onComplete(false)
            return
        }

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Utility.showToast(this, "Account created. Check email to verify.")
                auth.currentUser?.sendEmailVerification()
                auth.signOut()
                onComplete(true)
            } else {
                Utility.showToast(this, task.exception?.localizedMessage ?: "Signup failed")
                onComplete(false)
            }
        }
    }

    private fun validateLoginData(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Utility.showToast(this, "Email is invalid")
            return false
        }
        if (password.length < 6) {
            Utility.showToast(this, "Password too short")
            return false
        }
        return true
    }

    private fun validateSignUpData(email: String, password: String, confirmPassword: String): Boolean {
        if (!validateLoginData(email, password)) return false
        if (password != confirmPassword) {
            Utility.showToast(this, "Passwords do not match")
            return false
        }
        return true
    }
}
