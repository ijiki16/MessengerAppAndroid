package com.ijiki16.messengerapp.launcher.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ijiki16.messengerapp.main.MainActivity
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.databinding.ActivityLauncherBinding
import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.infrastructure.GlideApp
import com.ijiki16.messengerapp.launcher.LauncherActivityContract
import com.ijiki16.messengerapp.launcher.presenter.LauncherActivityPresenterImpl
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class LauncherActivity : LauncherActivityContract.View, Activity() {

    private lateinit var binding: ActivityLauncherBinding
    private lateinit var presenter: LauncherActivityContract.Presenter
    private var isRegistering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Firebase.auth.signInAnonymously()

        presenter = LauncherActivityPresenterImpl(this, AppPreferences.getInstance(getPreferences(Context.MODE_PRIVATE)))

        setViews()
    }

    override fun onResume() {
        super.onResume()
        setLoadingState(false)
        if (presenter.loginWithSavedUser()) { // user was saved. wait for login response.
            setLoadingState(true)
        }
    }

    private fun setViews() {
        binding.signBtn.setOnClickListener {
            val username = binding.nicknameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val about = binding.aboutEt.text.toString()

            if (isRegistering) {
                presenter.registerUser(username, password, about)
            } else {
                presenter.logInUser(username, password)
            }
            setLoadingState(true)
        }

        binding.signUpBtn.setOnClickListener {
            setRegisteringState()
        }

    }

    override fun onUserLoaded(user: UserInfo) {
        val storageReference = Firebase.storage.reference.child(user.profilePictureUrl)

        GlideApp.with(this)
            .load(storageReference)
            .placeholder(R.drawable.ic_baseline_account_circle_96)
            .error(R.drawable.ic_baseline_cancel_96)
            .into(binding.userAvatar)
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

    override fun loggedIn() {
        val myIntent = Intent(this@LauncherActivity, MainActivity::class.java)
        this@LauncherActivity.startActivity(myIntent)
    }

    override fun showError(error: String) {
        setLoadingState(false)
        // TODO: add feedback for errors.
    }

}