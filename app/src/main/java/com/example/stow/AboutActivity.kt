package com.example.stow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stow.databinding.ActivityAboutBinding
import com.google.firebase.auth.FirebaseAuth

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Entry point to access Firebase Database
        firebaseAuth = FirebaseAuth.getInstance()

        // Go back to login page if user has logged out
        if (firebaseAuth.currentUser == null) {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
            this.finish()
            Runtime.getRuntime().exit(0)
        }

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}