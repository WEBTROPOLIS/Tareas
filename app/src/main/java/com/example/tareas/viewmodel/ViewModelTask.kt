package com.example.tareas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tareas.model.TaskData
import com.example.tareas.model.TaskDatabase
import com.example.tareas.model.TaskRepository
import kotlinx.coroutines.launch

class ViewModelTask (application: Application): AndroidViewModel(application) {
    private val taskRepository : TaskRepository = TaskRepository(TaskDatabase.getDataBase
                                                                (application).getTaskDao())

    fun insertTask (task : TaskData) { viewModelScope.launch { taskRepository.insertTask(task) }}

    fun updateTask( task : TaskData) {viewModelScope.launch { taskRepository.updateTask(task) }}

    fun deleteOneTask(task : TaskData) { viewModelScope.launch { taskRepository.deleteTask(task) }}

    fun getAllTask() : LiveData<List<TaskData>>{
        return taskRepository.taskListLiveData
    }

    fun deleteAllTask() { viewModelScope.launch {  taskRepository.deleteAll() } }


    suspend fun getOneTask(id : Int): TaskData?{

        return taskRepository.getOneTask(id)
    }


}