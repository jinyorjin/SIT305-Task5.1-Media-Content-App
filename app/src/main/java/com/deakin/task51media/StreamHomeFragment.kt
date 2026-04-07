package com.deakin.task51media

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.deakin.task51media.data.AppDatabase
import com.deakin.task51media.data.Playlist
import kotlinx.coroutines.launch

class StreamHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_stream_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etVideoUrl = view.findViewById<EditText>(R.id.etVideoUrl)
        val btnPlay = view.findViewById<Button>(R.id.btnPlay)
        val btnAdd = view.findViewById<Button>(R.id.btnAddToPlaylist)
        val btnMyPlaylist = view.findViewById<Button>(R.id.btnMyPlaylist)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val txtUser = view.findViewById<TextView>(R.id.txtUser)

        val db = AppDatabase.getDatabase(requireContext())
        val playlistDao = db.playlistDao()

        val sharedPref = requireActivity().getSharedPreferences("user", 0)
        val username = sharedPref.getString("username", "") ?: ""

        txtUser.text = "Logged in as: $username"

        val passedUrl = arguments?.getString("videoUrl")
        if (!passedUrl.isNullOrEmpty()) {
            etVideoUrl.setText(passedUrl)
        }
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnPlay.setOnClickListener {
            val url = etVideoUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a YouTube URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidYoutubeUrl(url)) {
                Toast.makeText(requireContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Cannot open video", Toast.LENGTH_SHORT).show()
            }
        }

        btnAdd.setOnClickListener {
            val url = etVideoUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Enter URL first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidYoutubeUrl(url)) {
                Toast.makeText(requireContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                playlistDao.insert(
                    Playlist(
                        username = username,
                        videoUrl = url
                    )
                )
                Toast.makeText(requireContext(), "Added to playlist", Toast.LENGTH_SHORT).show()
                etVideoUrl.text.clear()
            }
        }

        btnMyPlaylist.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlaylistFragment())
                .addToBackStack(null)
                .commit()
        }

        btnLogout.setOnClickListener {
            sharedPref.edit().remove("username").apply()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }

    private fun isValidYoutubeUrl(url: String): Boolean {
        return url.contains("youtube.com/watch?v=") || url.contains("youtu.be/")
    }
}