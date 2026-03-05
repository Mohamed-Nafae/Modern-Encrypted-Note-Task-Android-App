package com.bm.encryptednoteapp.di

import android.app.Application
import androidx.room.Room
import com.bm.encryptednoteapp.data.data_source.NoteDB
import com.bm.encryptednoteapp.data.repository.NoteRepositoryImp
import com.bm.encryptednoteapp.data.repository.TaskRepositoryImp
import com.bm.encryptednoteapp.domian.TaskRepository.NoteRepository
import com.bm.encryptednoteapp.domian.TaskRepository.TaskRepository
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.AddNote
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.DeleteNote
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.GetNoteWithTasks
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.GetNotesWithTasks
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.NoteUseCases
import com.bm.encryptednoteapp.domian.use_cases.note_usecases.UpdateNote
import com.bm.encryptednoteapp.domian.use_cases.task_usecase.AddTask
import com.bm.encryptednoteapp.domian.use_cases.task_usecase.DeleteTask
import com.bm.encryptednoteapp.domian.use_cases.task_usecase.TaskUseCases
import com.bm.encryptednoteapp.domian.use_cases.task_usecase.UpdateTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application):NoteDB{
        return Room.databaseBuilder(
            app,
            NoteDB::class.java,
            NoteDB.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDB): NoteRepository{
        return NoteRepositoryImp(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: NoteDB): TaskRepository {
        return TaskRepositoryImp(db.taskDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesWithTasks = GetNotesWithTasks(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNoteWithTasks = GetNoteWithTasks(repository),
            updateNote = UpdateNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTaskUseCases(repository: TaskRepository): TaskUseCases {
        return TaskUseCases(
            addTask = AddTask(repository),
            deleteTask = DeleteTask(repository),
            updateTask = UpdateTask(repository)
        )
    }

}