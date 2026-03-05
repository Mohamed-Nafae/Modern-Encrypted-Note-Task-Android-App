package com.bm.encryptednoteapp.domian.TaskRepository

import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import kotlinx.coroutines.flow.Flow


/**
 * Repository interface for handling all data operations related to Notes.
 * It abstracts the data source (DAO) from the ViewModels.
 */
interface NoteRepository {
    suspend fun insertNote(note: Note): Int

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getNotesWithTasks(): Flow<List<NoteWithTasks>>

    suspend fun getNoteWithTasks(id: Int): NoteWithTasks
}