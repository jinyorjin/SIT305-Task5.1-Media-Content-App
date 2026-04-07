package com.deakin.task51media.model

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val imageResId: Int,
    var isBookmarked: Boolean = false
)