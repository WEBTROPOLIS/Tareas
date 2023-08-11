package com.example.tareas.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tareas.R
import com.example.tareas.viewmodel.ViewModelTask
import com.example.tareas.databinding.FragmentAddTaskBinding
import com.example.tareas.model.TaskData
import kotlinx.coroutines.launch


class AddTaskFragment : Fragment() {

    private lateinit var mBinding : FragmentAddTaskBinding
    private var isNew :Boolean = false
    private var changesNew : Boolean = false
    private var idTask :Int = -1
    private val viewModel: ViewModelTask by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentAddTaskBinding.inflate(inflater, container, false)




        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val args = arguments
        isNew = args?.getBoolean("newTask") ?: true
        idTask = args?.getInt("taskId",-1) ?: -1
        if (isNew) {
            mBinding.mtbAdd.title = "Agregar Tarea"
            mBinding.etTitle.requestFocus()
        } else {
            mBinding.mtbAdd.title = "Editar Tarea"
            lifecycleScope.launch {
                val task = viewModel.getOneTask(idTask)
                task?.let{
                    mBinding.etTitle.setText(it.title)
                    mBinding.etDescription.setText(it.description)
                    changesNew = false
                }
            }


        }

        mBinding.mtbAdd.menu.add(R.string.menu_save)
            .setOnMenuItemClickListener {
                saveData()
                true
            }

        mBinding.mtbAdd.menu.add(R.string.menu_exit)
            .setOnMenuItemClickListener {
                showExit()
                true
            }

        mBinding.etTitle.doAfterTextChanged {
            mBinding.tilTitle.error = null
            changesNew = true
        }

        mBinding.etDescription.doAfterTextChanged {
            mBinding.tilDescription.error = null
            changesNew = true
        }

        mBinding.fabSave.setOnClickListener { saveData() }

        mBinding.fabBack.setOnClickListener { showExit() }


    }

    private fun showExit(){
        hideKeyboard()
        if (changesNew){
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Cambios sin guardar")
                .setMessage("Tienes cambios sin guardar que se perderan. ¿Estás seguro de que deseas salir?")
                .setPositiveButton("Salir") { _, _ ->
                    requireActivity().supportFragmentManager.popBackStack()
                }
                .setNegativeButton("Guardar", null)
                .setOnDismissListener {
                    changesNew = false
                    saveData()
                }
                .show()
        }else{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveData() {
        val mtitle = mBinding.etTitle.text.toString().trim()
        val mdescription = mBinding.etDescription.text.toString().trim()
        val title = if (mtitle.length > 25) {
            mtitle.substring(0, 25)
        } else {
            mtitle
        }
        val description = if (mdescription.length > 100) {
            mdescription.substring(0, 100)
        } else {
            mdescription
        }
        if (description.isEmpty()) {
            mBinding.tilDescription.error = getString(R.string.required)
            mBinding.etDescription.requestFocus()
            return
        }
        if (title.isEmpty()) {
            mBinding.tilTitle.error = getString(R.string.required)
            mBinding.etTitle.requestFocus()
            return
        }

        if (isNew) {
            val task = TaskData(0, title, description)
            viewModel.insertTask(task)
            hideKeyboard()
            mBinding.etDescription.setText("")
            mBinding.etTitle.setText("")
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Éxito")
                .setMessage("Los datos fueron agregados exitosamente.")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                    showAddOrExitDialog()
                }
                .setCancelable(false)
                .create()

            alertDialog.show()
        } else {
            val task = TaskData(idTask, title, description)
            viewModel.updateTask(task)
            hideKeyboard()
            mBinding.etDescription.setText("")
            mBinding.etTitle.setText("")
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Guardado")
                .setMessage("Los datos fueron actualizados exitosamente.")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                    showAddOrExitDialog()
                }
                .setCancelable(false)
                .create()

            alertDialog.show()
        }
        changesNew = false
    }

    private fun showAddOrExitDialog() {
        val builder = AlertDialog.Builder(context)
            .setTitle("Agregar Nuevo")
            .setMessage("Deseas Agregar otra Tarea?")
            .setPositiveButton("Sí") { dialog, _ ->
                mBinding.etTitle.requestFocus()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                hideKeyboard()
                showExit()
                dialog.dismiss()
            }
        builder.show()
    }


    private fun hideKeyboard() {
        val inputMethodManager =
                     mBinding.root.context.
                        getSystemService(Context.INPUT_METHOD_SERVICE)
                             as? InputMethodManager

        val view = requireActivity().currentFocus
        view?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}