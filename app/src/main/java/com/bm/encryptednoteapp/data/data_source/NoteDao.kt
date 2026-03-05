package com.bm.encryptednoteapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    /**
     * Inserts a new note into the database. If a note with the same ID already exists,* it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    /**
     * Updates an existing note in the database.
     */
    @Update
    suspend fun updateNote(note: Note)

    /**
     * Deletes a note from the database. Because of the CASCADE foreign key,
     * all tasks associated with this note will also be deleted automatically.
     */
    @Delete
    suspend fun deleteNote(note: Note)

    /**
     * get all notes with there tasks
     */
    @Transaction
    @Query("SELECT * FROM Note")
    fun getNotesWithTasks(): Flow<List<NoteWithTasks>>

    /**
     * get note with its tasks
     */
    @Transaction
    @Query("SELECT * FROM Note WHERE noteId = :id")
    suspend fun getNoteWithTasks(id: Int): NoteWithTasks
}