package com.bm.encryptednoteapp.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.domian.models.Task

@Database(
    entities = [Note::class, Task::class],
    version = 3,
    exportSchema = false
)
abstract class NoteDB : RoomDatabase() {

    abstract val noteDao: NoteDao
    abstract val taskDao: TaskDao


    companion object{
        const val DATABASE_NAME = "note_db"
    }
}