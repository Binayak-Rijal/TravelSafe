package com.example.travelsafe.repository

import com.example.travelsafe.model.RecommendedPlace

interface BookmarkRepo {

    fun getAllBookmarks(
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    )

    fun addBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    )

    fun removeBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    )

    fun toggleBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    )

    fun isBookmarked(
        place: RecommendedPlace,
        callback: (Boolean) -> Unit
    )

    fun getBookmarkCount(): Int

    fun clearAllBookmarks(
        callback: (Boolean, String) -> Unit
    )

    fun searchBookmarks(
        query: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    )
}