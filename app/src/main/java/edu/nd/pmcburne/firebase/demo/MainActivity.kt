package edu.nd.pmcburne.firebase.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import edu.nd.pmcburne.firebase.demo.repositories.FirebaseAuthRepository
import edu.nd.pmcburne.firebase.demo.repositories.NoteDatabase
import edu.nd.pmcburne.firebase.demo.repositories.NoteRepository
import edu.nd.pmcburne.firebase.demo.screens.LogInScreen
import edu.nd.pmcburne.firebase.demo.screens.LogInViewModel
import edu.nd.pmcburne.firebase.demo.screens.NoteListScreen
import edu.nd.pmcburne.firebase.demo.screens.NotesViewModel
import edu.nd.pmcburne.firebase.demo.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect to firebase service
        val auth = Firebase.auth // Authentication for log-in/log-out
        val firestore = Firebase.firestore // Firestore for access to cloud database

        // Connection to Room database
        val db = NoteDatabase.getDatabase(applicationContext)
        val noteDao = db.noteDao()

        // Create repository objects
        val authRepository = FirebaseAuthRepository(auth)
        val noteRepository = NoteRepository(firestore, noteDao)


        // Create view models
        @Suppress("UNCHECKED_CAST")
        val logInViewModel: LogInViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LogInViewModel(authRepository) as T
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        val notesViewModel: NotesViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(noteRepository, authRepository) as T
                }
            }
        }

        // draw screen
        enableEdgeToEdge()
        setContent {
            val currentUser by authRepository.currentUserFlow.collectAsState(initial = authRepository.currentUser)

            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (currentUser != null) {
                        NoteListScreen(
                            notesViewModel = notesViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        LogInScreen(
                            viewModel = logInViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
