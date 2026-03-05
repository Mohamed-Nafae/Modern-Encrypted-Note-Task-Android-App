package com.bm.encryptednoteapp.domian.use_cases.task_usecase

import com.bm.encryptednoteapp.Common.InvalidTaskException
import com.bm.encryptednoteapp.domian.TaskRepository.TaskRepository
import com.bm.encryptednoteapp.domian.models.Task

class UpdateTask (
    private val repository: TaskRepository
){
    suspend operator fun invoke(task: Task) {
        repository.updateTask(task)
    }
}
