package com.example.tareas.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : TaskData)

    @Update
    suspend fun updateTask(task : TaskData)

    @Delete
    suspend fun deleteTask(task : TaskData)

    @Query("SELECT * FROM TASK_DATA ORDER BY title")
    fun getAllTask():LiveData<List<TaskData>>

    @Query("SELECT * FROM TASK_DATA WHERE id = :taskId")
    suspend fun getOneTask(taskId: Int): TaskData?

    @Query("DELETE  FROM  TASK_DATA")
    suspend fun deleteAll()



}