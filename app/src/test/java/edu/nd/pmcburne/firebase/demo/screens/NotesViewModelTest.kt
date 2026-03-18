package edu.nd.pmcburne.firebase.demo.screens

import com.google.firebase.auth.FirebaseUser
import edu.nd.pmcburne.firebase.demo.repositories.FakeAuthRepository
import edu.nd.pmcburne.firebase.demo.repositories.FakeNoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)


class NotesViewModelTest {
    private lateinit var viewModel: NotesViewModel
    private lateinit var fakeNoteRepo: FakeNoteRepository
    private lateinit var fakeAuthRepo: FakeAuthRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Redirect Coroutine main dispatcher to our test dispatcher
        Dispatchers.setMain(testDispatcher)

        fakeNoteRepo = FakeNoteRepository()

        val mockFirebaseUser = mock(FirebaseUser::class.java)
        whenever(mockFirebaseUser.uid).thenReturn("test_uid_123")
        whenever(mockFirebaseUser.email).thenReturn("test@example.com")
        fakeAuthRepo = FakeAuthRepository(mockFirebaseUser)

        viewModel = NotesViewModel(fakeNoteRepo, fakeAuthRepo)
    }

    @Test
    fun `addNote should call repository when user is signed in`() = runTest {
        viewModel.newNoteText = "Hello Test Note"
        viewModel.addNote()
        advanceUntilIdle()
        assertEquals(1, fakeNoteRepo.notes.size)
        assertEquals("Hello Test Note", fakeNoteRepo.notes[0].text)
        assertEquals("", viewModel.newNoteText) // Ensure text was cleared
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}