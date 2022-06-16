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
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()

        //initPython()

        val docRef = db.collection("users")
        docRef.document(firebaseAuth.uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val word_01 = document.getString("first").toString()
                    val word_02 = document.getString("second").toString()
                    val word_03 = document.getString("third").toString()
                    val word_04 = document.getString("fourth").toString()
                    val word_05 = document.getString("fifth").toString()
                    //Log.d(TAG,"$email/$pass/$user")
                    //Log.d(ContentValues.TAG,"$word_01")
                    //binding.EditText1.setText(first)
                    initPython()
                    binding.TextViewTrends.text = getPythonScript(word_01, word_02, word_03, word_04, word_05)

                } else {
                    Log.d(ContentValues.TAG, "The document doesn't exist.")
                }
            } else {
                task.exception?.message?.let {
                    Log.d(ContentValues.TAG, it)
                }
            }
        }

        //binding.TextViewTrends.text = getPythonScript()

        binding.button.setOnClickListener {
            val intent = Intent(this, AddKeywordsActivity::class.java)
            startActivity(intent)
        }


    }

    private fun initPython() {
        if (!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }
    }

    private fun getPythonScript(word_01: String, word_02: String,
    word_03: String, word_04: String, word_05: String): String {
        val python = Python.getInstance()
        val pythonFile = python.getModule("script")
        return pythonFile.callAttr("get_stats", word_01, word_02, word_03, word_04, word_05).toString()
        //return pythonFile.callAttr("get_stats", "word_01", "word_02", "word_03", "word_04", "word_05").toString()
    }

    private fun getPythonScriptTest(word_01: String, word_02: String,
                                    word_03: String, word_04: String, word_05: String): String {
        val python = Python.getInstance()
        val pythonFile = python.getModule("script")
        return pythonFile.callAttr("test", word_01, word_02, word_03, word_04, word_05).toString()
    }

}