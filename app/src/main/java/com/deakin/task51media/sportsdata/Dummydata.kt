package com.deakin.task51media.sportsdata

import com.deakin.task51media.R
import com.deakin.task51media.model.NewsItem

object Dummydata {

    val featuredMatches = mutableListOf(
        NewsItem(
            1,
            "Football Final Tonight",
            "Big final match this evening.",
            "Football",
            R.drawable.football
        ),
        NewsItem(
            2,
            "Basketball Semi Final",
            "Top two teams meet tonight.",
            "Basketball",
            R.drawable.basketball
        ),
        NewsItem(
            3,
            "Cricket Test Match",
            "Australia prepares for the next test.",
            "Cricket",
            R.drawable.cricket
        )
    )

    val latestNews = mutableListOf(
        NewsItem(
            4,
            "Football Star Injured",
            "A star player will miss the next game.",
            "Football",
            R.drawable.football
        ),
        NewsItem(
            5,
            "Basketball Team Wins",
            "Last-minute shot secures the match.",
            "Basketball",
            R.drawable.basketball
        ),
        NewsItem(
            6,
            "Cricket Captain Speaks",
            "Captain shares thoughts before the game.",
            "Cricket",
            R.drawable.cricket
        ),
        NewsItem(
            7,
            "Football Coach Update",
            "Coach explains the latest tactics.",
            "Football",
            R.drawable.football
        ),
        NewsItem(
            8,
            "Cricket Squad Announced",
            "New names included in the final squad.",
            "Cricket",
            R.drawable.cricket
        )
    )

    val bookmarks = mutableListOf<NewsItem>()
}