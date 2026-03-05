package com.bm.encryptednoteapp.domian.use_cases.note_usecases

import com.bm.encryptednoteapp.domian.TaskRepository.NoteRepository
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import kotlinx.coroutines.flow.Flow

class GetNoteWithTasks (
    private val noteRepository: NoteRepository
){
    suspend operator fun invoke(noteId: Int): NoteWithTasks {
        return noteRepository.getNoteWithTasks(noteId)
    }
}
