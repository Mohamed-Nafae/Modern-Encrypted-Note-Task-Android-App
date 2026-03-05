package com.bm.encryptednoteapp.presentation.editaddnote

import androidx.compose.ui.graphics.toArgb
import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.domian.models.NoteTag
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import com.bm.encryptednoteapp.domian.models.Task
import com.bm.encryptednoteapp.ui.theme.colorPrimaryDark

data class EditAddNotesState(
    val noteWithTasks: NoteWithTasks = NoteWithTasks(
        Note(
            title = "",
            subtitle = "",
            content = "",
            tag = NoteTag.NOTE,
            color = colorPrimaryDark.toArgb(),
            createdAt = System.currentTimeMillis()
        ), emptyList()
    )
    // val isEncrypted: Boolean = false,
    // val encryptionIv: String? = null
    // toString().format("dd MMMM yyyy HH:mm")
)
