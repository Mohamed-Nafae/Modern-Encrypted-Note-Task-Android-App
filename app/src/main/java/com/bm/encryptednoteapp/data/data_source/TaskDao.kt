package com.bm.encryptednoteapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.bm.encryptednoteapp.domian.models.Task


/**
 * Data Access Object for the Task entity.
 */

@Dao
interface TaskDao {
    /**
     * Inserts a new task into the database. If a task with the same ID already exists,
     * it will be replaced.
     *
     * @param task The task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /**
     * Updates an existing task in the database.
     *
     * @param task The task to be updated.
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Deletesa task from the database.
     *
     * @param task The task to be deleted.
     */
    @Delete
    suspend fun deleteTask(task: Task)
}