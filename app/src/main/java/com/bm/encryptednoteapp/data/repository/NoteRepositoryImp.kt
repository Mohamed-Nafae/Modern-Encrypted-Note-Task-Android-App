package com.bm.encryptednoteapp.data.repository

import com.bm.encryptednoteapp.data.data_source.NoteDao
import com.bm.encryptednoteapp.domian.TaskRepository.NoteRepository
import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImp(
    private val noteDao: NoteDao
) : NoteRepository {
    override suspend fun insertNote(note: Note): Int {
        return noteDao.insertNote(note).toInt()
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }


    override fun getNotesWithTasks(): Flow<List<NoteWithTasks>> {
        return noteDao.getNotesWithTasks()
    }

    override suspend fun getNoteWithTasks(id: Int): NoteWithTasks {
        return noteDao.getNoteWithTasks(id)
    }


}