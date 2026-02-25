package com.example.travelsafe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.SearchModel
//DEbugged
class SearchViewModel : ViewModel() {

    var searchState by mutableStateOf(SearchModel())
        private set

    fun updateFrom(value: String) {
        searchState = searchState.copy(from = value)
    }

    fun updateTo(value: String) {
        searchState = searchState.copy(to = value)
    }

    fun incrementPassengers() {
        searchState = searchState.copy(passengers = searchState.passengers + 1)
    }

    fun decrementPassengers() {
        if (searchState.passengers > 1) {
            searchState = searchState.copy(passengers = searchState.passengers - 1)
        }
    }

    fun updateDepartureDate(value: String) {
        searchState = searchState.copy(departureDate = value)
    }

    fun updateReturnDate(value: String) {
        searchState = searchState.copy(returnDate = value)
    }
}