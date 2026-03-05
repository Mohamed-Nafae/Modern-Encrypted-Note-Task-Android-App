package com.bm.encryptednoteapp.domian.models

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithTasks(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "noteId"
    )
    val tasks: List<Task>
)
