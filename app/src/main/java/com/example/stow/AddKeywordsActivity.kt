package com.example.stow

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.stow.databinding.ActivityAddKeywordsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddKeywordsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityAddKeywordsBinding
    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()


        val docRef = db.collection("users")
        docRef.document(firebaseAuth.uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                   // val first = document.getString("first")
                    val word_01 = document.getString("first").toString()
                    val word_02 = document.getString("second").toString()
                    val word_03 = document.getString("third").toString()
                    val word_04 = document.getString("fourth").toString()
                    val word_05 = document.getString("fifth").toString()
                    //Log.d(TAG,"$email/$pass/$user")
                    //Log.d(TAG,"$first")
                    //binding.EditText1.setText(first)
                    binding.EditText1.setText(word_01)
                    binding.EditText2.setText(word_02)
                    binding.EditText3.setText(word_03)
                    binding.EditText4.setText(word_04)
                    binding.EditText5.setText(word_05)


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


            // Add a new document with a generated ID
            db.collection("users").document(firebaseAuth.uid.toString())
                .update("first", binding.EditText1.text.toString(),
                    "second", binding.EditText2.text.toString(),
                    "third", binding.EditText3.text.toString(),
                    "fourth", binding.EditText4.text.toString(),
                    "fifth", binding.EditText5.text.toString())

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }




}

