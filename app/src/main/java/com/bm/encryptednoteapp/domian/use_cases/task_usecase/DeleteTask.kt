package com.bm.encryptednoteapp.domian.use_cases.task_usecase

import com.bm.encryptednoteapp.domian.TaskRepository.TaskRepository
import com.bm.encryptednoteapp.domian.models.Task

class DeleteTask(
    private val repository: TaskRepository
)
{
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}
