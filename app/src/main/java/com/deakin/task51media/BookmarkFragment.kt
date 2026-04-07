package com.deakin.task51media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deakin.task51media.adapter.NewsAdapter
import com.deakin.task51media.sportsdata.Dummydata

class BookmarkFragment : Fragment() {

    /**
     * Standard Fragment lifecycle method to inflate the UI layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Step 1: Initialize UI components from the layout
        val recyclerBookmarks = view.findViewById<RecyclerView>(R.id.recyclerBookmarks)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        // Step 2: Configure LayoutManager for the RecyclerView (Vertical List)
        recyclerBookmarks.layoutManager = LinearLayoutManager(requireContext())

        /**
         * Step 3: Setup Adapter with bookmark data.
         * I am reusing the 'NewsAdapter' to maintain UI consistency.
         */
        recyclerBookmarks.adapter = NewsAdapter(Dummydata.bookmarks) { selectedItem ->

            // Step 4: Handle navigation to DetailFragment when a bookmarked item is clicked
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    SportsDetailFragment.newInstance(
                        selectedItem.title,
                        selectedItem.description,
                        selectedItem.category,
                        selectedItem.imageResId,
                        selectedItem.id
                    )
                )
                // addToBackStack ensures the user can return to the bookmark list
                .addToBackStack(null)
                .commit()
        }

        // Step 5: Handle Back Button to return to the previous fragment in the stack
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}