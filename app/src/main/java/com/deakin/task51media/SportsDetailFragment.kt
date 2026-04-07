package com.deakin.task51media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deakin.task51media.adapter.RelatedAdapter
import com.deakin.task51media.sportsdata.Dummydata

class SportsDetailFragment : Fragment() {

    companion object {
        // Create a new fragment and pass selected item data using Bundle
        fun newInstance(
            title: String,
            description: String,
            category: String,
            imageResId: Int,
            id: Int
        ): SportsDetailFragment {
            val fragment = SportsDetailFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("description", description)
            args.putString("category", category)
            args.putInt("imageResId", imageResId)
            args.putInt("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Load the detail fragment layout
        return inflater.inflate(R.layout.fragment_sports_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get UI components from the layout
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val imgDetail = view.findViewById<ImageView>(R.id.imgDetail)
        val txtDetailTitle = view.findViewById<TextView>(R.id.txtDetailTitle)
        val txtDetailDescription = view.findViewById<TextView>(R.id.txtDetailDescription)
        val btnBookmark = view.findViewById<Button>(R.id.btnBookmark)
        val recyclerRelated = view.findViewById<RecyclerView>(R.id.recyclerRelated)
        val txtRelatedTitle = view.findViewById<TextView>(R.id.txtRelatedTitle)

        // Get selected item data from Bundle
        val title = arguments?.getString("title") ?: ""
        val description = arguments?.getString("description") ?: ""
        val category = arguments?.getString("category") ?: ""
        val imageResId = arguments?.getInt("imageResId") ?: 0
        val id = arguments?.getInt("id") ?: -1

        // Display selected news details
        imgDetail.setImageResource(imageResId)
        txtDetailTitle.text = title
        txtDetailDescription.text = description

        // Go back to the previous fragment
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Combine all sports items into one list
        val allNews = Dummydata.latestNews + Dummydata.featuredMatches

        // Find the currently selected item by id
        val selectedItem = allNews.find { item -> item.id == id }

        // If already bookmarked, disable the button
        if (selectedItem != null && Dummydata.bookmarks.any { it.id == selectedItem.id }) {
            btnBookmark.text = "Bookmarked"
            btnBookmark.isEnabled = false
        }

        // Add current item to bookmarks
        btnBookmark.setOnClickListener {
            if (selectedItem != null && Dummydata.bookmarks.none { item -> item.id == selectedItem.id }) {
                Dummydata.bookmarks.add(selectedItem.copy(isBookmarked = true))
                btnBookmark.text = "Bookmarked"
                btnBookmark.isEnabled = false
                Toast.makeText(requireContext(), "Bookmarked", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Already bookmarked", Toast.LENGTH_SHORT).show()
            }
        }

        // Filter related stories using the same category
        val relatedStories = allNews.filter { item ->
            item.category.equals(category, ignoreCase = true) && item.id != id
        }

        // Hide the related section if no related stories exist
        if (relatedStories.isEmpty()) {
            txtRelatedTitle.visibility = View.GONE
            recyclerRelated.visibility = View.GONE
        } else {
            txtRelatedTitle.visibility = View.VISIBLE
            recyclerRelated.visibility = View.VISIBLE

            // Show related stories in RecyclerView
            recyclerRelated.layoutManager = LinearLayoutManager(requireContext())
            recyclerRelated.adapter = RelatedAdapter(relatedStories) { clickedItem ->

                // Open the clicked related item in a new detail fragment
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        newInstance(
                            clickedItem.title,
                            clickedItem.description,
                            clickedItem.category,
                            clickedItem.imageResId,
                            clickedItem.id
                        )
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}