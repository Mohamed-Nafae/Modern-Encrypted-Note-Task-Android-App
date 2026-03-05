package com.bm.encryptednoteapp.domian.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bm.encryptednoteapp.ui.theme.colorPrimaryDark

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Int=0,
    val title: String,
    val subtitle: String,
    val content: String,
    val tag: NoteTag,
    val color: Int,
    val imagePath: String? = null,
    val createdAt: Long,
    val isEncrypted: Boolean = false,
    val encryptionIv: String? = null
){
    companion object {
        val noteColors = listOf(colorPrimaryDark, Color.Blue, Color.Red, Color.Green, Color.Yellow)
    }
}


