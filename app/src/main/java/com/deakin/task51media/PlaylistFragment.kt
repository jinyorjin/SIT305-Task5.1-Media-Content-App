package com.deakin.task51media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.deakin.task51media.data.AppDatabase
import kotlinx.coroutines.launch

class PlaylistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listViewPlaylist = view.findViewById<ListView>(R.id.listViewPlaylist)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val sharedPref = requireActivity().getSharedPreferences("user", 0)
        val username = sharedPref.getString("username", "")

        val db = AppDatabase.getDatabase(requireContext())
        val playlistDao = db.playlistDao()

        lifecycleScope.launch {
            val playlistItems = playlistDao.getUserPlaylist(username ?: "")

            if (playlistItems.isEmpty()) {
                Toast.makeText(requireContext(), "No videos in playlist", Toast.LENGTH_SHORT).show()
            }

            val urlList = playlistItems.map { it.videoUrl }

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                urlList
            )

            listViewPlaylist.adapter = adapter

            listViewPlaylist.setOnItemClickListener { _, _, position, _ ->
                val clickedUrl = urlList[position]

                val fragment = StreamHomeFragment()
                val bundle = Bundle()
                bundle.putString("videoUrl", clickedUrl)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}