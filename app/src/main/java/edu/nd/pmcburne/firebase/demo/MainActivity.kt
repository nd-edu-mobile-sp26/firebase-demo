package edu.nd.pmcburne.firebase.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import edu.nd.pmcburne.firebase.demo.screens.LogInScreen
import edu.nd.pmcburne.firebase.demo.screens.LogInViewModel
import edu.nd.pmcburne.firebase.demo.screens.NoteListScreen
import edu.nd.pmcburne.firebase.demo.screens.NotesViewModel
import edu.nd.pmcburne.firebase.demo.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val logInViewModel: LogInViewModel = hiltViewModel()
            val notesViewModel: NotesViewModel = hiltViewModel()
            
            val currentUser by logInViewModel.currentUser.collectAsState()

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
