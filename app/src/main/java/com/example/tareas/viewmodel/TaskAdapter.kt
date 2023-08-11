package com.example.tareas.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.tareas.R
import com.example.tareas.databinding.ItemTaskBinding
import com.example.tareas.model.TaskData
import com.example.tareas.view.OnClickListenerTask
import com.example.tareas.view.TaskFragment

class TaskAdapter (
    private var tasks : MutableList<TaskData>,
    private var listener: OnClickListenerTask,
    private var context: Context,
    private var viewModel: ViewModelTask,
    private var fragment: TaskFragment
): RecyclerView.Adapter<TaskAdapter.ViewHolder>()

{
    private lateinit var mContext: Context

    inner class ViewHolder(view : View):RecyclerView.ViewHolder(view){
        val binding = ItemTaskBinding.bind(view)

        fun setListener(taskData: TaskData){
            with(binding.root){
                setOnClickListener { listener.onClick(taskData.id) }

                setOnLongClickListener {
                    showOptionDialog(taskData)
                    true
                }

            }
        }
    }

    private fun showOptionDialog(taskData: TaskData){
        listener.onLongClick(taskData)
    }

    fun getCount(): Int {
        return tasks.size
    }

    fun updateData(newTask: List<TaskData>){
        tasks.clear()
        tasks.addAll(newTask)
        notifyDataSetChanged()

        val taskCount = getCount()
        val message = if(taskCount == 1){
            "Tienes 1 Tarea Registrada"
        }else{
            "Tienes $taskCount Tareas Registradas"
        }
        fragment.updateCount(message)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_task,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks.get(position)

        with(holder){
            setListener(task)
            binding.tvTitle.text = task.title.uppercase()
            binding.tvDescriptionTask.text=task.description
        }
    }
}