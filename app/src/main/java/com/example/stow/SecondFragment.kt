package com.example.stow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.stow.databinding.FragmentSecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Entry point to access Firebase Database
        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextEmailSignUp.text.toString()
            val password = binding.editTextPasswordSignUp.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            signUpEmail(email, password, confirmPassword)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUpEmail(email: String, password: String, confirmPassword: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                when {
                    it.isSuccessful -> {

                        // Initialize Cloud Firestore
                        val db = Firebase.firestore

                        // Hash Map of fields
                        val user = hashMapOf(
                            "first" to "",
                            "second" to "",
                            "third" to "",
                            "fourth" to "",
                            "fifth" to ""
                        )

                        // Add a new document with userID
                        db.collection("users")
                            .document(firebaseAuth.uid.toString())
                            .set(user)

                        // Go back to First Fragment
                        Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        firebaseAuth.signOut()
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Password do not match", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, it.exception.toString().
                        substringAfter(": "), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
        }
    }
}