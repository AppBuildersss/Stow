package com.example.stow

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.stow.databinding.LoggedInFragmentSecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoggedInSecondFragment : Fragment() {

    private var _binding: LoggedInFragmentSecondBinding? = null
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

        // Go back to login page if user has logged out
        if (firebaseAuth.currentUser == null) {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            Runtime.getRuntime().exit(0)
        }

        _binding = LoggedInFragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayWords()

        binding.buttonConfirm.setOnClickListener {
            storeWords()
            findNavController().navigate(R.id.action_LoggedInSecondFragment_to_LoggedInFirstFragment)
            Toast.makeText(context, "Keywords Updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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