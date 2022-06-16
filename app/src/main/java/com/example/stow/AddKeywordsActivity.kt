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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKeywordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var temp = ""
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            temp += user.email.toString()
        } else {
            // No user is signed in
            temp += "nullllll"
        }


        val db = Firebase.firestore

        val docRef = db.collection("users")
        docRef.document("34OwnhEZZ4prwfo4fW51").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val first = document.getString("first")
                    //Log.d(TAG,"$email/$pass/$user")
                    Log.d(TAG,"$first")
                    binding.EditText1.setText(first)
                } else {
                    Log.d(TAG, "The document doesn't exist.")
                }
            } else {
                task.exception?.message?.let {
                    Log.d(TAG, it)
                }
            }
        }


        binding.buttonConfirm.setOnClickListener {

            // Create a new user with a first and last name
            val user = hashMapOf(
                "first" to "hello",
                "second" to "",
                "third" to "",
                "fourth" to "",
                "fifth" to "yoyoyoy"
            )

            // Add a new document with a generated ID
            db.collection("users").document("34OwnhEZZ4prwfo4fW51")
                .update("first", binding.EditText1.text.toString())

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }




}

