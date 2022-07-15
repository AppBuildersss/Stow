package com.example.stow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stow.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
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

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            loginEmail(email, password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(context, LoggedInMainActivity::class.java)
                    startActivity(intent)
                    Runtime.getRuntime().exit(0)
                } else {
                    Toast.makeText(context, "Invalid Email/Password", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
        }
    }
}