package com.bm.encryptednoteapp.domian.TaskRepository

import com.bm.encryptednoteapp.domian.models.Task


/**
 * Repository interface for handling all data operations related to Tasks.
 * It abstracts the data source (DAO) from the ViewModels.
 */
interface TaskRepository {

    suspend fun insertTask(task: Task)

    suspend fun updateTask( task: Task)

    suspend fun deleteTask(task: Task)
}