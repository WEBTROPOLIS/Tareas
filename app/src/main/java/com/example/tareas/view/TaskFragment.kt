package com.example.tareas.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tareas.R
import com.example.tareas.viewmodel.TaskAdapter
import com.example.tareas.viewmodel.ViewModelTask
import com.example.tareas.databinding.FragmentTaskBinding
import com.example.tareas.model.TaskData
import kotlinx.coroutines.launch


class TaskFragment : Fragment(), OnClickListenerTask {
    private lateinit var mBinding : FragmentTaskBinding
    private val viewModel : ViewModelTask by viewModels()
    private lateinit var adapter: TaskAdapter
    private var regToDel : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTaskBinding.inflate(inflater,container,false)
        return mBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskAdapter(mutableListOf(),this,
                requireContext(),viewModel,this)

        mBinding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rvTask.adapter = adapter

        viewModel.getAllTask().observe(viewLifecycleOwner) { taskList ->
            adapter.updateData(taskList)
        }

        mBinding.mtb.menu.add("Salir")
            .setOnMenuItemClickListener {
                showExitDialog()
                true
            }
        mBinding.mtb.menu.add("Crear Tarea")
            .setOnMenuItemClickListener {
                newTaskLoadFragment()
                true
            }

        mBinding.mtb.menu.add("Borrar todas las Tareas")
            .setOnMenuItemClickListener {
                deleteAll()
                true
            }

        mBinding.fabAddTask.setOnClickListener { newTaskLoadFragment() }

        mBinding.fabDelAllTask.setOnClickListener { deleteAll() }



    }

    private fun getTotalTask(){
        val tasks = adapter.getCount()
        regToDel = tasks > 0

    }

    private fun deleteAll() {
        getTotalTask()
        if (regToDel) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("CONFIRMA")
            builder.setMessage("¿Estás seguro de que deseas Borrar todas las Tareas?")
            builder.setIcon(R.drawable.ic_warnin_red)
            builder.setPositiveButton("Sí") { dialog, _ ->

                viewModel.deleteAllTask()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->

                dialog.dismiss()
            }
            builder.show()
        }else{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sin Registros")
            builder.setMessage("No Existen Tareas para Eliminar")
            builder.setPositiveButton("OK"){dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun newTaskLoadFragment() {
        val newTaskFragment = AddTaskFragment()
        val bundle = Bundle()
        bundle.putBoolean("newTask",true)
        newTaskFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame,newTaskFragment)
            .addToBackStack(null)
            .commit()

    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("CONFIRMA")
        builder.setMessage("¿Estás seguro de que deseas salir?")
        builder.setPositiveButton("Sí") { dialog, _ ->

            requireActivity().finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->

            dialog.dismiss()
        }
        builder.show()
    }

    fun updateCount(message : String){
        mBinding.tvCountInfo.text = message
    }

    override fun onClick(taskId: Int) {
        val editTaskFragment = AddTaskFragment()
        val bundle = Bundle()
        bundle.putBoolean("newTask", false)
        bundle.putInt("taskId",taskId)
        editTaskFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, editTaskFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onLongClick(taskData: TaskData) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Eliminar ${taskData.title}")
            .setMessage("¿Estas seguro de querer eliminar esta tarea?")
            .setIcon(R.drawable.ic_warnin_red)
            .setPositiveButton("ELIMINAR"){_,_ ->
                lifecycleScope.launch { viewModel.deleteOneTask(taskData) }
            }
            .setNegativeButton("CANCELAR", null)
            .create()
        alertDialog.show()
    }

    override fun onDeleteTable(taskData: TaskData) {
        viewModel.deleteAllTask()
    }


}