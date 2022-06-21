package com.example.stow

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.stow.databinding.ActivityMainBinding
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Entry point to access Firebase Database
        firebaseAuth = FirebaseAuth.getInstance()

        // Go back to login page if user has logged out
        val user = firebaseAuth.currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Setup
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise python support
        initPython()

        // Hash map of keywords
        val baton = getPythonScriptResult()

        binding.editButton.setOnClickListener {
            val intent = Intent(this, AddKeywordsActivity::class.java)
            startActivity(intent)
        }

        binding.signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.refreshButton.setOnClickListener {
            runPythonScript(baton)
        }
    }

    private fun initPython() {
        if (!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }
    }

    private fun runPythonScript(baton: HashMap<String, String>): String {
        val python = Python.getInstance()
        val pythonFile = python.getModule("script")
        return pythonFile.callAttr("getResult", baton["first"], baton["second"],
            baton["third"], baton["fourth"], baton["fifth"]).toString()
    }

    private fun getPythonScriptResult(): HashMap<String, String> {
        val baton = hashMapOf<String, String>()
        val docRef = Firebase.firestore.collection("users")
        docRef.document(firebaseAuth.uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    baton["first"] = (document.getString("first").toString())
                    baton["second"] = (document.getString("second").toString())
                    baton["third"] = (document.getString("third").toString())
                    baton["fourth"] = (document.getString("fourth").toString())
                    baton["fifth"] = (document.getString("fifth").toString())
                    binding.tempTextView.text = runPythonScript(baton)
                } else {
                    Log.d(ContentValues.TAG, "The document doesn't exist.")
                }
            } else {
                task.exception?.message?.let {
                    Log.d(ContentValues.TAG, it)
                }
            }
        }
        return baton
    }
}