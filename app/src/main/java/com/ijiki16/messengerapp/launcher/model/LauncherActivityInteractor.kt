package com.ijiki16.messengerapp.launcher.model

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.launcher.LauncherActivityContract

public class LauncherActivityInteractor(private val presenter: LauncherActivityContract.Presenter) {

    companion object {
        const val DB_USERS = "users"
        const val DB_PASSWORD_KEY = "password"
        const val DB_ABOUT_KEY = "about"
    }

    fun logIn(username: String, passwordHash: String) {
        val database = Firebase.database
        val usersReference = database.getReference(DB_USERS)
        val userReference = usersReference.child(username)

        userReference.child(DB_PASSWORD_KEY).get().addOnSuccessListener {
            if (it.exists() && it.value == passwordHash) {
                presenter.onLogInResult(LoginResult(true, ""))
                presenter.saveUserLocally(username, passwordHash)
            } else {
                presenter.onLogInResult(LoginResult(false, "Incorrect Username or Password"))
            }
        }.addOnFailureListener{
            presenter.onLogInResult(LoginResult(false, it.toString()))
        }
    }

    fun register(username: String, passwordHash: String, about: String) {
        val database = Firebase.database
        val usersReference = database.getReference(DB_USERS)
        usersReference.child(username).child(DB_PASSWORD_KEY).setValue(passwordHash).addOnCompleteListener { passRes ->
            if (passRes.isSuccessful) {
                usersReference.child(username).child(DB_ABOUT_KEY).setValue(about).addOnCompleteListener { aboutRes ->
                    if(aboutRes.isSuccessful) {
                        presenter.onLogInResult(LoginResult(true, ""))
                    } else {
                        presenter.onLogInResult(LoginResult(false, aboutRes.exception.toString()))
                    }
                }
            } else {
                presenter.onLogInResult(LoginResult(false, passRes.exception.toString()))
            }
        }
    }

}