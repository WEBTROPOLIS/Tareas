package com.example.tareas.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tareas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        launchFragment()
    }

    private fun launchFragment(){
        supportFragmentManager.beginTransaction()
            .replace(mBinding.frame.id, TaskFragment())
            .addToBackStack(null)
            .commit()
    }
}