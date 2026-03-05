package com.bm.encryptednoteapp.domian.use_cases.task_usecase

import com.bm.encryptednoteapp.Common.InvalidNoteException
import com.bm.encryptednoteapp.Common.InvalidTaskException
import com.bm.encryptednoteapp.domian.TaskRepository.TaskRepository
import com.bm.encryptednoteapp.domian.models.Task

class AddTask(
    private val repository: TaskRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(task: Task) {
        if (task.description.isBlank())
            throw InvalidNoteException("The description of the task can't be empty.")
        repository.insertTask(task)
    }
}
