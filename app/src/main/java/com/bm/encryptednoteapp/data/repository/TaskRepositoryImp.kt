package com.bm.encryptednoteapp.data.repository

import com.bm.encryptednoteapp.data.data_source.TaskDao
import com.bm.encryptednoteapp.domian.TaskRepository.TaskRepository
import com.bm.encryptednoteapp.domian.models.Task

class TaskRepositoryImp(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }
    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}