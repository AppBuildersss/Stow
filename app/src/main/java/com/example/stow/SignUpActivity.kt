package com.example.stow

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stow.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // No user is signed in
        }
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.username.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        // Initialize Cloud Firestore
                        val db = Firebase.firestore

                        // Create a new user with a first and last name
                        val user = hashMapOf(
                            "first" to "",
                            "second" to "",
                            "third" to "",
                            "fourth" to "",
                            "fifth" to ""
                        )

                        // Add a new document with a generated ID
                        db.collection("users")
                            .document(firebaseAuth.uid.toString())
                            .set(user)
                            //.add(user)
                            //.addOnSuccessListener { documentReference ->
                            //    Log.d(TAG,"DocumentSnapshot added with ID: ${documentReference.id}")
                           // }
                           // .addOnFailureListener { e ->
                            //    Log.w(TAG, "Error adding document", e)
                            //}


                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}