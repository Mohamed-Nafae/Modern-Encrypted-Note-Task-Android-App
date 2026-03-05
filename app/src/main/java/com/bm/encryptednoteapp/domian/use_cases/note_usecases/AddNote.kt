package com.bm.encryptednoteapp.domian.use_cases.note_usecases

import com.bm.encryptednoteapp.Common.InvalidNoteException
import com.bm.encryptednoteapp.domian.TaskRepository.NoteRepository
import com.bm.encryptednoteapp.domian.models.Note
import kotlin.jvm.Throws

class AddNote(
    private val reposiory: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note): Int{
        if(note.title.isBlank())
            throw InvalidNoteException("The title of the note can't be empty.")

        if(note.content.isBlank())
            throw InvalidNoteException("The content of the note can't be empty.")
        
        return reposiory.insertNote(note)
    }
}