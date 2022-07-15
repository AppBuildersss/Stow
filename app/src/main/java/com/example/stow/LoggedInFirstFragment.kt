package com.example.stow

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.stow.databinding.LoggedInFragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoggedInFirstFragment : Fragment() {

    private var _binding: LoggedInFragmentFirstBinding? = null
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

        // Initialise python support
        initPython()

        _binding = LoggedInFragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomRightClicked()

        // Display keyword tracker texts
        displayWords()

        val mediaPlayer1 = MediaPlayer.create(context, R.raw.background)
        val mediaPlayer2 = MediaPlayer.create(context, R.raw.motivate)
        val mediaPlayer3 = MediaPlayer.create(context, R.raw.passion)

        mediaPlayer1.isLooping = true;
        mediaPlayer2.isLooping = true;
        mediaPlayer3.isLooping = true;

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_LoggedInFirstFragment_to_LoggedInSecondFragment)
        }

        binding.imageButtonBottomRight.setOnClickListener {
            bottomRightClicked()
            if (mediaPlayer1.isPlaying) {
                mediaPlayer1.pause();
            }

            if (mediaPlayer2.isPlaying) {
                mediaPlayer2.pause();
            }

            if (mediaPlayer3.isPlaying) {
                mediaPlayer3.pause()
            }
        }

        binding.imageButtonBottomLeft.setOnClickListener {
            bottomLeftClicked()
            if (mediaPlayer3.isPlaying) {
                mediaPlayer3.pause();
            }

            if (mediaPlayer2.isPlaying) {
                mediaPlayer2.pause();
            }

            if (!mediaPlayer1.isPlaying) {
                mediaPlayer1.start()
            }
        }

        binding.imageButtonTopLeft.setOnClickListener {
            topLeftClicked()
            if (mediaPlayer1.isPlaying) {
                mediaPlayer1.pause();
            }

            if (mediaPlayer3.isPlaying) {
                mediaPlayer3.pause();
            }

            if (!mediaPlayer2.isPlaying) {
                mediaPlayer2.start()
            }
        }

        binding.imageButtonTopRight.setOnClickListener {
            topRightClicked()
            if (mediaPlayer1.isPlaying) {
                mediaPlayer1.pause();
            }

            if (mediaPlayer2.isPlaying) {
                mediaPlayer2.pause();
            }

            if (!mediaPlayer3.isPlaying) {
                mediaPlayer3.start()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bottomRightClicked() {
        binding.imageButtonBottomRight.setImageResource(R.drawable.disc_mute)
        binding.imageButtonBottomLeft.setImageResource(R.drawable.disc_unselected)
        binding.imageButtonTopRight.setImageResource(R.drawable.disc_unselected)
        binding.imageButtonTopLeft.setImageResource(R.drawable.disc_unselected)
    }

    private fun bottomLeftClicked() {
        binding.imageButtonBottomRight.setImageResource(R.drawable.disc_mute_unclick)
        binding.imageButtonBottomLeft.setImageResource(R.drawable.disc_yellow)
        binding.imageButtonTopRight.setImageResource(R.drawable.disc_unselected)
        binding.imageButtonTopLeft.setImageResource(R.drawable.disc_unselected)
    }

    private fun topRightClicked() {
        binding.imageButtonBottomRight.setImageResource(R.drawable.disc_mute_unclick)
        binding.imageButtonBottomLeft.setImageResource(R.drawable.disc_unselected)
        binding.imageButtonTopRight.setImageResource(R.drawable.disc_red)
        binding.imageButtonTopLeft.setImageResource(R.drawable.disc_unselected)
    }

    private fun topLeftClicked() {
        binding.imageButtonBottomRight.setImageResource(R.drawable.disc_mute_unclick)
        binding.imageButtonBottomLeft.setImageResource(R.drawable.disc_unselected)
        binding.imageButtonTopRight.setImageResource(R.drawable.disc_unselected)
        binding.imageButtonTopLeft.setImageResource(R.drawable.disc_blue)
    }

    private fun displayWords() {
        val docRef = Firebase.firestore.collection("users")
        docRef.document(firebaseAuth.uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val baton = hashMapOf(
                        "first" to (document.getString("first").toString()),
                        "second" to (document.getString("second").toString()),
                        "third" to (document.getString("third").toString()),
                        "fourth" to (document.getString("fourth").toString()),
                        "fifth" to (document.getString("fifth").toString())
                    )

                    showPythonResult(baton)
                }
            }
        }
    }

    private fun initPython() {
        if (!Python.isStarted()){
            context?.let { AndroidPlatform(it) }?.let { Python.start(it) }
        }
    }

    private fun testPythonScript(): String {
        val python = Python.getInstance()
        val pythonFile = python.getModule("script")
        return pythonFile.callAttr("test", "Hello World").toString()
    }

    private fun runPythonScript(baton: HashMap<String, String>): String {
        val python = Python.getInstance()
        val pythonFile = python.getModule("script")
        return pythonFile.callAttr("getResult", baton["first"], baton["second"],
            baton["third"], baton["fourth"], baton["fifth"]).toString()
    }

    private fun showPythonResult(baton: HashMap<String, String>) {
        var trendDump = runPythonScript(baton)

        binding.editText1.text = trendDump.substringBefore("\n")
        trendDump = trendDump.substringAfter("\n")
        binding.trend1.text = trendDump.substringBefore("\n")

        trendDump = trendDump.substringAfter("\n")
        binding.editText2.text = trendDump.substringBefore("\n")
        trendDump = trendDump.substringAfter("\n")
        binding.trend2.text = trendDump.substringBefore("\n")

        trendDump = trendDump.substringAfter("\n")
        binding.editText3.text = trendDump.substringBefore("\n")
        trendDump = trendDump.substringAfter("\n")
        binding.trend3.text = trendDump.substringBefore("\n")

        trendDump = trendDump.substringAfter("\n")
        binding.editText4.text = trendDump.substringBefore("\n")
        trendDump = trendDump.substringAfter("\n")
        binding.trend4.text = trendDump.substringBefore("\n")

        trendDump = trendDump.substringAfter("\n")
        binding.editText5.text = trendDump.substringBefore("\n")
        binding.trend5.text = trendDump.substringAfter("\n")

    }
}