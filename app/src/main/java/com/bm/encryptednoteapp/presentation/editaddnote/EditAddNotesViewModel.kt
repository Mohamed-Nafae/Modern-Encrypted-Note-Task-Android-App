package com.bm.encryptednoteapp.presentation.editaddnote

import android.content.ContentResolver
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bm.encryptednoteapp.Common.InvalidNoteException
import com.bm.encryptednoteapp.Common.InvalidTaskException
import com.bm.encryptednoteapp.domian.models.Task
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.NoteUseCases
import com.bm.encryptednoteapp.domian.use_cases.task_usecase.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EditAddNotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val taskUseCases: TaskUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(EditAddNotesState())
    val state: State<EditAddNotesState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isClearAllDialogVisible = mutableStateOf(false)
    val isTaskDialogVisible = mutableStateOf(false)
    val saveDialogVisible = mutableStateOf(false)
    val leaveDialogVisible = mutableStateOf(false)

    private var currentNoteId: Int? = null
    private var deletedImagePath: String? = null
    private var maxTaskPosition: Int = 0


    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteWithTasks(noteId).also {
                        currentNoteId = it.note.noteId
                        _state.value = state.value.copy(
                            noteWithTasks = it
                        )
                    }
                    _state.value.noteWithTasks.tasks.forEach {
                        if (it.position > maxTaskPosition) {
                            maxTaskPosition = it.position
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: EditAddNotesEvents) {
        when (event) {
            is EditAddNotesEvents.AddTask -> {
                if (event.task.isBlank()){
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("The task can't be empty."))
                    }
                } else {
                    _state.value = state.value.copy(
                        noteWithTasks = state.value.noteWithTasks.copy(
                            tasks = state.value.noteWithTasks.tasks.plus(
                                Task(
                                    noteId = -1,
                                    description = event.task,
                                    isDone = false,
                                    position = maxTaskPosition + 1
                                )
                            )
                        )
                    )
                    maxTaskPosition++
                    isTaskDialogVisible.value = false
                }
            }

            is EditAddNotesEvents.OnImageSelected -> {
                val internalPath = persistImage(event.uri, event.contentResolver, event.internalDir)
                if (internalPath != null) {
                    _state.value = state.value.copy(
                        noteWithTasks = state.value.noteWithTasks.copy(
                            note = state.value.noteWithTasks.note.copy(
                                imagePath = internalPath // Save the permanent local path
                            )
                        )
                    )
                }
            }

            is EditAddNotesEvents.ChangeColor -> {
                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        note = state.value.noteWithTasks.note.copy(
                            color = event.color
                        )
                    )
                )
            }

            is EditAddNotesEvents.ChangeTag -> {
                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        note = state.value.noteWithTasks.note.copy(
                            tag = event.tag
                        )
                    )
                )
            }

            is EditAddNotesEvents.OnImageDelete ->{
                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        note = state.value.noteWithTasks.note.copy(
                            imagePath = null
                        )
                    )
                )
                deletedImagePath = event.imagePath
            }

            is EditAddNotesEvents.OnClearAllConfirm -> {
                deletedImagePath = _state.value.noteWithTasks.note.imagePath
                _state.value = EditAddNotesState()
                isClearAllDialogVisible.value = false
                maxTaskPosition = 0
            }

            is EditAddNotesEvents.DeleteTask -> {
                val task: Task =
                    state.value.noteWithTasks.tasks.last { it.position == event.position }

                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        tasks = state.value.noteWithTasks.tasks.filter { it != task }
                    )
                )

                if (currentNoteId != null && task.noteId != -1) {
                    viewModelScope.launch {
                        taskUseCases.deleteTask(task)
                    }
                }
            }

            is EditAddNotesEvents.EnteredContent -> {
                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        note = state.value.noteWithTasks.note.copy(
                            content = event.content
                        )
                    )
                )
            }

            is EditAddNotesEvents.EnteredSubtitle -> {
                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        note = state.value.noteWithTasks.note.copy(
                            subtitle = event.subtitle
                        )
                    )
                )
            }

            is EditAddNotesEvents.EnteredTitle -> {
                _state.value = state.value.copy(
                    noteWithTasks = state.value.noteWithTasks.copy(
                        note = state.value.noteWithTasks.note.copy(
                            title = event.title
                        )
                    )
                )
            }

            is EditAddNotesEvents.SaveNote -> {
                viewModelScope.launch {
                    try {
                        if (currentNoteId == null) {
                            currentNoteId = noteUseCases.addNote(state.value.noteWithTasks.note)
                        }
                        else {
                            noteUseCases.updateNote(state.value.noteWithTasks.note)
                        }
                        state.value.noteWithTasks.tasks.forEach {
                            if (it.noteId == -1){
                                taskUseCases.addTask(it.copy(noteId = currentNoteId!!))
                            }
                        }
                        _eventFlow.emit(UiEvent.SaveNote)
                        _state.value = EditAddNotesState()
                        deletedImagePath?.let {
                            deleteImage(it)
                        }
                        deletedImagePath = null
                        isClearAllDialogVisible.value = false
                        saveDialogVisible.value = false
                        leaveDialogVisible.value = false
                        maxTaskPosition = 0
                        currentNoteId = null
                    } catch (e: InvalidNoteException){
                        saveDialogVisible.value = false
                        leaveDialogVisible.value = false
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }

            is EditAddNotesEvents.UpdateTask -> {
                if (event.task.isBlank()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackBar("The task can't be empty."))
                    }
                } else {
                    var uTask = state.value.noteWithTasks.tasks[0]
                    _state.value = state.value.copy(
                        noteWithTasks = state.value.noteWithTasks.copy(
                            tasks = state.value.noteWithTasks.tasks.map { task ->
                                if (task.position == event.position) {
                                    uTask = task.copy(
                                        description = event.task,
                                        isDone = event.isDone ?: task.isDone
                                    )
                                    uTask
                                } else {
                                    task
                                }
                            }
                        )
                    )

                    if (currentNoteId != null && uTask.noteId != -1) {
                        viewModelScope.launch {
                            taskUseCases.updateTask(uTask)
                        }
                    }
                    isTaskDialogVisible.value = false
                }
            }
        }
    }

    private fun persistImage(uri: android.net.Uri, contentResolver: ContentResolver, internalDir: File): String? {
        return try {
            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val destinationFile = File(internalDir, fileName)

            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            destinationFile.absolutePath // This is what we save to DB
        } catch (e: Exception) {
            Log.e("ViewModel", "Failed to save image", e)
            null
        }
    }

    private fun deleteImage(imagePath: String){
        val file = File(imagePath)
        if (file.exists()) {
            file.delete()
        }
    }
    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}

