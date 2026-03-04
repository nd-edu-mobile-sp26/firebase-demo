# Firebase Demo

This project is a simple educational demo for Firebase Authentication and Firestore. 

It was developed for **CSE 40333** at the **University of Notre Dame**.

**WARNING**: When building and running this application, be aware that all notes posted are publicly
visible within the application! Do not post any secrete or inappropriate notes while testing the
application!

## Setup and Running

To run this file, you need the associated google-services.json file. For security reasons, you should
never push your google-services.json file to a public repository. Students can download the google-services.json file at the link provided in the class notes for the day.

1) Download the google-services.json file from the class notes for the day
2) Move that file into your project's /app/ folder (the same folder containing your app level build.gradle.kts file)
3) Build and run!

## Source Code Files of Note

## Firebase related

### FirebaseAuthRepository.kt
Demonstrates use of **FirebaseAuthentication**. This class manages Firebase Authentication operations, including signing in, signing up, and signing out, as well as tracking the active user.

### NoteRepository.kt
Handles data operations for notes, coordinating between the remote Firestore database and the local Room cache for offline support.

## Room DB Related

### NoteDao.kt
The Data Access Object for the Room database, defining methods for local storage and retrieval of notes.

### NoteDatabase.kt
The Room database implementation for caching notes locally.

### FirebaseRoomConverters.kt
Contains type converters for Room to handle Firebase-specific types like `Timestamp`.

## Screens and ViewModels

### LogInScreen.kt
A Compose screen providing the user interface for email/password authentication.

### LogInViewModel.kt
Manages the state and logic for the login screen, interacting with the authentication repository.

### NotesScreen.kt
A Compose screen that displays the list of notes and provides an interface for adding and deleting them.

### NotesViewModel.kt
Manages the state and logic for the notes screen, including fetching notes from the repository and handling add/delete actions.

## Other

### MainActivity.kt
The entry point of the application. It initializes the repositories and ViewModels and handles the high-level navigation logic between the login and notes screens.

### Note.kt
A data class representing a note. It is used both as a Firestore document model and a Room database entity.


