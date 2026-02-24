package com.example.travelsafe.repository

import com.example.travelsafe.model.RecommendedPlace
import com.example.travelsafe.utils.BookmarkManager

class BookmarkRepoImpl : BookmarkRepo {

    override fun getAllBookmarks(
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        try {
            val bookmarks = BookmarkManager.bookmarks.toList()
            callback(true, "Done", bookmarks)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load bookmarks", emptyList())
        }
    }

    override fun addBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    ) {
        try {
            if (BookmarkManager.isBookmarked(place)) {
                callback(false, "${place.name} is already bookmarked")
            } else {
                BookmarkManager.toggle(place)
                callback(true, "${place.name} added to bookmarks")
            }
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to add bookmark")
        }
    }

    override fun removeBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    ) {
        try {
            if (!BookmarkManager.isBookmarked(place)) {
                callback(false, "${place.name} is not in bookmarks")
            } else {
                BookmarkManager.toggle(place)
                callback(true, "${place.name} removed from bookmarks")
            }
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to remove bookmark")
        }
    }

    override fun toggleBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    ) {
        try {
            val wasBookmarked = BookmarkManager.isBookmarked(place)
            BookmarkManager.toggle(place)
            if (wasBookmarked) {
                callback(true, "${place.name} removed from bookmarks")
            } else {
                callback(true, "${place.name} added to bookmarks")
            }
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to toggle bookmark")
        }
    }

    override fun isBookmarked(
        place: RecommendedPlace,
        callback: (Boolean) -> Unit
    ) {
        callback(BookmarkManager.isBookmarked(place))
    }

    override fun getBookmarkCount(): Int {
        return BookmarkManager.bookmarks.size
    }

    override fun clearAllBookmarks(
        callback: (Boolean, String) -> Unit
    ) {
        try {
            BookmarkManager.bookmarks.clear()
            callback(true, "All bookmarks cleared")
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to clear bookmarks")
        }
    }

    override fun searchBookmarks(
        query: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        try {
            if (query.isEmpty()) {
                callback(true, "Done", BookmarkManager.bookmarks.toList())
                return
            }
            val result = BookmarkManager.bookmarks.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
            callback(true, "Done", result)
        } catch (e: Exception) {
            callback(false, e.message ?: "Search failed", emptyList())
        }
    }
}