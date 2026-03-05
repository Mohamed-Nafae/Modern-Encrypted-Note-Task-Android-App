package com.bm.encryptednoteapp.domian.use_cases.note_usecases

import com.bm.encryptednoteapp.domian.TaskRepository.NoteRepository
import com.bm.encryptednoteapp.domian.models.Note

class UpdateNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note)
    }
}