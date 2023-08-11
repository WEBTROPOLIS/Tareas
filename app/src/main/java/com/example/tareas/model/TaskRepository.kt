package com.example.tareas.model

import androidx.lifecycle.MutableLiveData

class TaskRepository(private val taskDao: TaskDao ) {


    val taskListLiveData = taskDao.getAllTask()
    val oneTaskLiveData = MutableLiveData<TaskData>()

    suspend fun insertTask(task : TaskData) { taskDao.insertTask(task)}

    suspend fun updateTask(task : TaskData) { taskDao.updateTask(task)}

    suspend fun deleteTask(task : TaskData) { taskDao.deleteTask(task)}

    suspend fun getOneTask(id : Int): TaskData? {
       return taskDao.getOneTask(id)
    }

   suspend fun deleteAll() { taskDao.deleteAll() }


}