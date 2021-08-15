package com.ijiki16.messengerapp.launcher.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.main.MainActivity
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.databinding.ActivityLauncherBinding
import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.launcher.LauncherActivityContract
import com.ijiki16.messengerapp.launcher.presenter.LauncherActivityPresenterImpl

class LauncherActivity : LauncherActivityContract.View, Activity() {

    private lateinit var binding: ActivityLauncherBinding
    private lateinit var presenter: LauncherActivityContract.Presenter
    private var isRegistering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.database.setPersistenceEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser
        if(currentUser != null){
            loggedIn()
        }

        presenter = LauncherActivityPresenterImpl(this, AppPreferences.getInstance(getPreferences(Context.MODE_PRIVATE)))

        setViews()
    }

    override fun onResume() {
        super.onResume()
        setLoadingState(false)
    }

    private fun setViews() {
        binding.signBtn.setOnClickListener {
            val username = binding.nicknameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val about = binding.aboutEt.text.toString()

            if (isRegistering) {
                register(username, password, about)
            } else {
                logIn(username, password)
            }
            setLoadingState(true)
        }

        binding.signUpBtn.setOnClickListener {
            setRegisteringState()
        }

    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.nicknameEt.isEnabled = !isLoading
        binding.passwordEt.isEnabled = !isLoading
        binding.aboutEt.isEnabled = !isLoading
        binding.signUpBtn.isEnabled = !isLoading
        binding.signBtn.isEnabled = !isLoading

        binding.loadingPb.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun setRegisteringState() {
        isRegistering = true
        binding.signBtn.text = getString(R.string.sign_up)
        binding.aboutEt.visibility = View.VISIBLE
        binding.notRegisteredTv.visibility = View.GONE
        binding.signUpBtn.visibility = View.GONE
    }

    private fun logIn(username: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword("$username@messenger.app", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Firebase.auth.currentUser?.uid?.let {
                        presenter.getUserInfo(it)
                    }
                } else {
                    showError(task.exception?.message ?: "Something went wrong logging in.")
                }
            }
    }

    private fun register(username: String, password: String, about: String) {
        Firebase.auth.createUserWithEmailAndPassword("$username@messenger.app", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Firebase.auth.currentUser?.uid?.let {
                        presenter.saveUserInfo(
                            it,
                            username,
                            about,
                        )
                    } ?: run {
                        showError(task.exception?.message ?: "Corrupted auth data generated.")
                    }
                } else {
                    showError(task.exception?.message ?: "Something went wrong during registration.")
                }
            }
    }

    override fun loggedIn() {
        val myIntent = Intent(this@LauncherActivity, MainActivity::class.java)
        this@LauncherActivity.startActivity(myIntent)
    }

    override fun showError(error: String) {
        setLoadingState(false)
        // TODO: add feedback for errors.
    }

}