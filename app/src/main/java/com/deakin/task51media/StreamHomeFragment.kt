package com.deakin.task51media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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

    private var webView: WebView? = null

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
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        webView = view.findViewById(R.id.webViewYoutube)

        setupWebView()

        val db = AppDatabase.getDatabase(requireContext())
        val playlistDao = db.playlistDao()

        val sharedPref = requireActivity().getSharedPreferences("user", 0)
        val username = sharedPref.getString("username", "") ?: ""

        txtUser.text = "Logged in as: $username"

        val passedUrl = arguments?.getString("videoUrl")
        if (!passedUrl.isNullOrEmpty()) {
            etVideoUrl.setText(passedUrl)

            val videoId = extractYoutubeVideoId(passedUrl)
            if (videoId != null) {
                loadYoutubeVideo(videoId)
            }
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnPlay.setOnClickListener {
            val url = etVideoUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a YouTube URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val videoId = extractYoutubeVideoId(url)

            if (videoId == null) {
                Toast.makeText(requireContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadYoutubeVideo(videoId)
        }

        btnAdd.setOnClickListener {
            val url = etVideoUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Enter URL first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (extractYoutubeVideoId(url) == null) {
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

    private fun setupWebView() {
        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.domStorageEnabled = true
        webView?.webViewClient = WebViewClient()
        webView?.webChromeClient = WebChromeClient()
    }

    private fun loadYoutubeVideo(videoId: String) {
        val embedUrl = "https://www.youtube.com/embed/$videoId"
        webView?.loadUrl(embedUrl)
    }

    private fun extractYoutubeVideoId(url: String): String? {
        return when {
            url.contains("youtube.com/watch?v=") -> {
                val videoId = url.substringAfter("watch?v=").substringBefore("&")
                if (videoId.isNotEmpty()) videoId else null
            }
            url.contains("youtu.be/") -> {
                val videoId = url.substringAfter("youtu.be/").substringBefore("?")
                if (videoId.isNotEmpty()) videoId else null
            }
            url.contains("youtube.com/embed/") -> {
                val videoId = url.substringAfter("embed/").substringBefore("?")
                if (videoId.isNotEmpty()) videoId else null
            }
            else -> null
        }
    }

    override fun onDestroyView() {
        webView?.destroy()
        webView = null
        super.onDestroyView()
    }
}