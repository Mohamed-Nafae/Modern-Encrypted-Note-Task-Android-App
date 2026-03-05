package com.bm.encryptednoteapp.presentation.editaddnote

import android.content.ContentResolver
import com.bm.encryptednoteapp.domian.models.NoteTag
import java.io.File

sealed class EditAddNotesEvents {
    data class EnteredTitle(val title: String) : EditAddNotesEvents()
    data class EnteredSubtitle(val subtitle: String) : EditAddNotesEvents()
    data class EnteredContent(val content: String) : EditAddNotesEvents()
    data class OnImageSelected(val uri: android.net.Uri, val contentResolver: ContentResolver, val internalDir: File): EditAddNotesEvents()
    data class OnImageDelete(val imagePath:String): EditAddNotesEvents()
    data class ChangeTag(val tag: NoteTag) : EditAddNotesEvents()
    data class UpdateTask(val position: Int, val task: String, val isDone: Boolean? = null) : EditAddNotesEvents()
    data class DeleteTask(val position: Int) : EditAddNotesEvents()
    data class AddTask(val task: String) : EditAddNotesEvents()
    data class ChangeColor(val color: Int) : EditAddNotesEvents()
    object OnClearAllConfirm: EditAddNotesEvents()
    object SaveNote : EditAddNotesEvents()

}