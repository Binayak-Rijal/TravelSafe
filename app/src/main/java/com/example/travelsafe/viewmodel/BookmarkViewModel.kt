package com.example.travelsafe.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.RecommendedPlace
import com.example.travelsafe.repository.BookmarkRepo
import com.example.travelsafe.repository.BookmarkRepoImpl

class BookmarkViewModel : ViewModel() {

    // Using RepoImpl through the interface
    private val bookmarkRepo: BookmarkRepo = BookmarkRepoImpl()

    fun getAllBookmarks(
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        bookmarkRepo.getAllBookmarks(callback)
    }

    fun addBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    ) {
        bookmarkRepo.addBookmark(place, callback)
    }

    fun removeBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    ) {
        bookmarkRepo.removeBookmark(place, callback)
    }

    fun toggleBookmark(
        place: RecommendedPlace,
        callback: (Boolean, String) -> Unit
    ) {
        bookmarkRepo.toggleBookmark(place, callback)
    }

    fun isBookmarked(
        place: RecommendedPlace,
        callback: (Boolean) -> Unit
    ) {
        bookmarkRepo.isBookmarked(place, callback)
    }

    fun getBookmarkCount(): Int {
        return bookmarkRepo.getBookmarkCount()
    }

    fun clearAllBookmarks(
        callback: (Boolean, String) -> Unit
    ) {
        bookmarkRepo.clearAllBookmarks(callback)
    }

    fun searchBookmarks(
        query: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        bookmarkRepo.searchBookmarks(query, callback)
    }
}