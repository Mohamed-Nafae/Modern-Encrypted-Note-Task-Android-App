package com.bm.encryptednoteapp.presentation.mynotes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.NoteUseCases
import com.bm.encryptednoteapp.domian.use_cases.task_usecase.TaskUseCases
import com.bm.encryptednoteapp.domian.util.CryptoManager
import com.bm.encryptednoteapp.domian.util.NoteOrder
import com.bm.encryptednoteapp.domian.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyNotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    val state = _state
    var recentlyDeletedNote: NoteWithTasks? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: HomeScreenEvents) {
        when (event) {
            is HomeScreenEvents.ToggleEncryption -> {
                viewModelScope.launch {
                    val noteWithTasks = event.noteWithTasks
                    val note = noteWithTasks.note
                    val tasks = noteWithTasks.tasks
                    try {
                        if (note.isEncrypted) {
                            // --- DECRYPT LOGIC ---
                            val iv = note.encryptionIv ?: return@launch // Safety check

                            val decryptedNote = note.copy(
                                title = CryptoManager.decrypt(note.title, iv),
                                subtitle = CryptoManager.decrypt(note.subtitle, iv),
                                content = CryptoManager.decrypt(note.content, iv),
                                isEncrypted = false,
                                encryptionIv = null
                            )

                            val decryptedTasks = tasks.map {
                                it.copy(description = CryptoManager.decrypt(it.description, iv))
                            }

                            // Save to DB
                            noteUseCases.updateNote(decryptedNote)
                            decryptedTasks.forEach { taskUseCases.updateTask(it) }
                        } else {
                            val (encryptedContent, iv) = CryptoManager.encrypt(note.content)

                            // 2. Use that SAME IV for title, subtitle, and tasks
                            val encryptedNote = note.copy(
                                title = CryptoManager.encrypt(note.title, iv),
                                subtitle = CryptoManager.encrypt(note.subtitle, iv),
                                content = encryptedContent,
                                isEncrypted = true,
                                encryptionIv = iv
                            )

                            // Update Note in DB
                            noteUseCases.updateNote(encryptedNote)

                            val encryptedTasks = tasks.map {
                                it.copy(description = CryptoManager.encrypt(it.description, iv))
                            }
                            // Update Tasks in DB using the same IV
                            encryptedTasks.forEach {
                                taskUseCases.updateTask(it)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Optional: Update state to show an error message/snackbar
                    }
                }
            }

            is HomeScreenEvents.DeleteNote -> {
                viewModelScope.launch {
                    _state.value.selectedNote?.let { notewithTasks ->
                        noteUseCases.deleteNote(notewithTasks.note)
                        notewithTasks.tasks.forEach {
                            taskUseCases.deleteTask(it)
                        }
                        recentlyDeletedNote = notewithTasks
                        _state.value = state.value.copy(
                            notes = state.value.notes.filter { it != notewithTasks }
                        )
                        updateSelectedNote()
                    }
                }
            }

            is HomeScreenEvents.Order -> {
                if (state.value.noteOrder.orderType == event.noteOrder.orderType &&
                    state.value.noteOrder::class == event.noteOrder::class
                )
                    return
                getNotes(event.noteOrder)
            }

            is HomeScreenEvents.SelectNote -> {
                updateSelectedNote(event.noteId)
            }

            is HomeScreenEvents.SearchNote -> {
                // Update the search query in the state
                _state.value = state.value.copy(
                    searchQuery = event.query
                )
                // Trigger a re-fetch of notes with the new search query
                getNotes(state.value.noteOrder)
            }

            is HomeScreenEvents.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is HomeScreenEvents.RestoreNote -> {
                if (recentlyDeletedNote != null) {
                    viewModelScope.launch {
                        noteUseCases.addNote(recentlyDeletedNote?.note ?: return@launch)
                        recentlyDeletedNote?.tasks?.forEach {
                            taskUseCases.addTask(it)
                        }
                        recentlyDeletedNote = null
                    }
                }
            }

            is HomeScreenEvents.DeleteImage -> {
                viewModelScope.launch {
                    recentlyDeletedNote?.note?.imagePath?.let {
                        deleteImage(it)
                    }
                    recentlyDeletedNote = null
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesWithTasks(noteOrder).onEach { notes ->
            val filteredNotes = if (state.value.searchQuery.isBlank()) {
                notes
            } else {
                notes.filter {
                    (it.note.title.contains(state.value.searchQuery, ignoreCase = true)
                            || it.note.content.contains(state.value.searchQuery, ignoreCase = true)
                            || it.note.subtitle.contains(
                        state.value.searchQuery,
                        ignoreCase = true
                    )) || it.tasks.any { task ->
                        task.description.contains(state.value.searchQuery, ignoreCase = true)
                    }
                }
            }
            _state.value = state.value.copy(
                notes = filteredNotes,
                noteOrder = noteOrder
            )
            if (state.value.selectedNote == null)
                updateSelectedNote()
            else
                updateSelectedNote(state.value.selectedNote?.note?.noteId)
        }.launchIn(viewModelScope)
    }

    private fun deleteImage(imagePath: String) {
        val file = File(imagePath)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun updateSelectedNote( noteId: Int? = null){
        if (noteId == null){
            _state.value = state.value.copy(
                selectedNote = state.value.notes.firstOrNull()
            )
        }
        else {
            _state.value = state.value.copy(
                selectedNote = state.value.notes.find { it.note.noteId == noteId }
            )
        }
    }

}