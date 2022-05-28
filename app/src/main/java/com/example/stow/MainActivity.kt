package com.example.stow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stow.databinding.ActivityMainBinding

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPython()
        binding.pythonTextview.text = getPythonScript()
    }

    private fun initPython() {
        if (!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }
    }

    private fun getPythonScript(): String {
        val python = Python.getInstance()
        val pythonFile = python.getModule("script")
        return pythonFile.callAttr("run").toString()
    }

}