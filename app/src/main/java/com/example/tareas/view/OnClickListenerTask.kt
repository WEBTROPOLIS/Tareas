package com.example.tareas.view

import com.example.tareas.model.TaskData

interface OnClickListenerTask {

    fun onClick (taskId : Int)

    fun onLongClick (taskData : TaskData)

    fun onDeleteTable (taskData : TaskData)



}