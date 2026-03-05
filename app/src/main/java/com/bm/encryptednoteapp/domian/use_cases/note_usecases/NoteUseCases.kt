package com.bm.encryptednoteapp.domian.use_cases.note_usecases

data class NoteUseCases (
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val updateNote: UpdateNote,
    val getNotesWithTasks: GetNotesWithTasks,
    val getNoteWithTasks: GetNoteWithTasks
)