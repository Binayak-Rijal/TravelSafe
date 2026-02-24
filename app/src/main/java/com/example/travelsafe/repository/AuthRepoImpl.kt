package com.example.travelsafe.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthRepoImpl : AuthRepo {

    private val auth = FirebaseAuth.getInstance()

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful")
                } else {
                    callback(false, task.exception?.message ?: "Login failed")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        fullName: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save display name after registration
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                callback(true, "Registration successful")
                            } else {
                                callback(false, profileTask.exception?.message ?: "Profile update failed")
                            }
                        }
                } else {
                    callback(false, task.exception?.message ?: "Registration failed")
                }
            }
    }

    override fun updateDisplayName(
        newName: String,
        callback: (Boolean, String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            callback(false, "No user logged in")
            return
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Name updated successfully")
                } else {
                    callback(false, task.exception?.message ?: "Update failed")
                }
            }
    }

    override fun sendPasswordReset(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Reset email sent. Check your inbox.")
                } else {
                    callback(false, task.exception?.message ?: "Failed to send reset email")
                }
            }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserName(): String? {
        return auth.currentUser?.displayName
    }

    override fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}