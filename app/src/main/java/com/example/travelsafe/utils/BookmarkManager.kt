package com.example.travelsafe.utils

import androidx.compose.runtime.mutableStateListOf
import com.example.travelsafe.model.RecommendedPlace

object BookmarkManager {
    val bookmarks = mutableStateListOf<RecommendedPlace>()

    fun toggle(place: RecommendedPlace) {
        if (isBookmarked(place)) {
            bookmarks.removeAll { it.name == place.name }
        } else {
            bookmarks.add(place)
        }
    }

    fun isBookmarked(place: RecommendedPlace): Boolean {
        return bookmarks.any { it.name == place.name }
    }
}