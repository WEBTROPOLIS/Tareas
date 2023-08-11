package com.example.tareas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_data")
data class TaskData(

    @PrimaryKey(autoGenerate = true)
    val id :Int = 0,
    var title : String,
    var description : String
)

