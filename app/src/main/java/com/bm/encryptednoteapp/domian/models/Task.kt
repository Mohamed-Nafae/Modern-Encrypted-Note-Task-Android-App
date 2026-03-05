package com.bm.encryptednoteapp.domian.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["noteId"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("noteId"), Index("taskId")]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int=0,
    val noteId: Int,
    val description: String,
    val isDone: Boolean = false,
    val position: Int,
    val createdAt: Long = System.currentTimeMillis()
)