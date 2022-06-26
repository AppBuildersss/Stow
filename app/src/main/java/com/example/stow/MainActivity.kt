package com.example.stow

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
        var baton = getPythonScriptResult()

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

                    var numbers = runPythonScript(baton)

                    //binding.tempTextView.text = numbers
                    binding.progressBar2.visibility = View.INVISIBLE

                    binding.word01.text = baton["first"]
                    binding.word01Trend.text = numbers.substringBefore("\n")

                    binding.word02.text = baton["second"]
                    numbers = numbers.substringAfter("\n")
                    binding.word02Trend.text = numbers.substringBefore("\n")

                    binding.word03.text = baton["third"]
                    numbers = numbers.substringAfter("\n")
                    binding.word03Trend.text = numbers.substringBefore("\n")

                    binding.word04.text = baton["fourth"]
                    numbers = numbers.substringAfter("\n")
                    binding.word04Trend.text = numbers.substringBefore("\n")

                    binding.word05.text = baton["fifth"]
                    binding.word05Trend.text = numbers.substringAfter("\n")

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