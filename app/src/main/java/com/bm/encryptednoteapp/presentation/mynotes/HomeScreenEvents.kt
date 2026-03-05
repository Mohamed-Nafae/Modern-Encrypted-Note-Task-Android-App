package com.bm.encryptednoteapp.presentation.mynotes

import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import com.bm.encryptednoteapp.domian.util.NoteOrder

sealed class HomeScreenEvents{
    data class SearchNote(val query: String): HomeScreenEvents()
    data class SelectNote(val noteId: Int): HomeScreenEvents()
    data class Order(val noteOrder: NoteOrder): HomeScreenEvents()
    data class DeleteNote(val noteId: Int): HomeScreenEvents()
    data class ToggleEncryption(val noteWithTasks: NoteWithTasks) : HomeScreenEvents()
    object ToggleOrderSection: HomeScreenEvents()
    object RestoreNote: HomeScreenEvents()
    object DeleteImage : HomeScreenEvents()
}