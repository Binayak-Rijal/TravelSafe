package com.example.travelsafe.repository

interface AuthRepo {

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    )

    fun register(
        email: String,
        password: String,
        fullName: String,
        callback: (Boolean, String) -> Unit
    )

    fun updateDisplayName(
        newName: String,
        callback: (Boolean, String) -> Unit
    )

    fun sendPasswordReset(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun logout()

    fun isLoggedIn(): Boolean

    fun getCurrentUserName(): String?

    fun getCurrentUserEmail(): String?
}