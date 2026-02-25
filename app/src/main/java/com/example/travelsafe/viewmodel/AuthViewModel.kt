package com.example.travelsafe.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelsafe.repository.AuthRepo
import com.example.travelsafe.repository.AuthRepoImpl

class AuthViewModel : ViewModel() {

    // Using RepoImpl through the interface
    private val authRepo: AuthRepo = AuthRepoImpl()

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        authRepo.login(email, password, callback)
    }

    fun register(
        email: String,
        password: String,
        fullName: String,
        callback: (Boolean, String) -> Unit
    ) {
        authRepo.register(email, password, fullName, callback)
    }

    fun updateDisplayName(
        newName: String,
        callback: (Boolean, String) -> Unit
    ) {
        authRepo.updateDisplayName(newName, callback)
    }

    fun sendPasswordReset(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        authRepo.sendPasswordReset(email, callback)
    }

    fun logout() {
        authRepo.logout()
    }

    fun isLoggedIn(): Boolean {
        return authRepo.isLoggedIn()
    }

    fun getCurrentUserName(): String? {
        return authRepo.getCurrentUserName()
    }

    fun getCurrentUserEmail(): String? {
        return authRepo.getCurrentUserEmail()
    }
}