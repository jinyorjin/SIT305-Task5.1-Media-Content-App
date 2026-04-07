package com.deakin.task51media

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deakin.task51media.adapter.FeaturedAdapter
import com.deakin.task51media.adapter.NewsAdapter
import com.deakin.task51media.model.NewsItem
import com.deakin.task51media.sportsdata.Dummydata

class SportsHomeFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sports_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val btnBookmarks = view.findViewById<Button>(R.id.btnBookmarks)
        val recyclerFeatured = view.findViewById<RecyclerView>(R.id.recyclerFeatured)
        val recyclerNews = view.findViewById<RecyclerView>(R.id.recyclerNews)

        // Step 1: Set up Horizontal RecyclerView for Featured Matches
        recyclerFeatured.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Step 2: Set up Vertical RecyclerView for Latest Sports News
        recyclerNews.layoutManager =
            LinearLayoutManager(requireContext())

        // Step 3: Initialize adapters with dummy data and click listeners
        val featuredAdapter = FeaturedAdapter(Dummydata.featuredMatches) { selectedItem ->
            openDetailFragment(selectedItem)
        }

        newsAdapter = NewsAdapter(Dummydata.latestNews) { selectedItem ->
            openDetailFragment(selectedItem)
        }

        recyclerFeatured.adapter = featuredAdapter
        recyclerNews.adapter = newsAdapter

        // Navigate to BookmarkFragment
        btnBookmarks.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookmarkFragment())
                .addToBackStack(null)
                .commit()
        }

        /**
         * Step 4: Real-time search/filter logic.
         * It filters the news list by category as the user types.
         */
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()

                // Filter list based on the search keyword (category)
                val filteredList = if (keyword.isEmpty()) {
                    Dummydata.latestNews
                } else {
                    Dummydata.latestNews.filter { item ->
                        item.category.lowercase().contains(keyword.lowercase())
                    }
                }

                // Update the RecyclerView with the filtered results
                newsAdapter.updateList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * Step 5: Helper method for Fragment navigation and data passing.
     */
    private fun openDetailFragment(item: NewsItem) {
        val fragment = SportsDetailFragment.newInstance(
            item.title,
            item.description,
            item.category,
            item.imageResId,
            item.id
        )

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}