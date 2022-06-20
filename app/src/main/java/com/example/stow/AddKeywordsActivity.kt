package com.example.stow

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.stow.databinding.ActivityAddKeywordsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddKeywordsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityAddKeywordsBinding
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
        binding = ActivityAddKeywordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayWords()

        binding.buttonConfirm.setOnClickListener {
            storeWords()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayWords() {
        val docRef = Firebase.firestore.collection("users")
        docRef.document(firebaseAuth.uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    binding.editText1.setText(document.getString("first").toString())
                    binding.editText2.setText(document.getString("second").toString())
                    binding.editText3.setText(document.getString("third").toString())
                    binding.editText4.setText(document.getString("fourth").toString())
                    binding.editText5.setText(document.getString("fifth").toString())
                } else {
                    Log.d(TAG, "The document doesn't exist.")
                }
            } else {
                task.exception?.message?.let {
                    Log.d(TAG, it)
                }
            }
        }
    }

    private fun storeWords() {
        Firebase.firestore.collection("users").document(firebaseAuth.uid.toString())
            .update("first", binding.editText1.text.toString(),
                "second", binding.editText2.text.toString(),
                "third", binding.editText3.text.toString(),
                "fourth", binding.editText4.text.toString(),
                "fifth", binding.editText5.text.toString())
    }

}

