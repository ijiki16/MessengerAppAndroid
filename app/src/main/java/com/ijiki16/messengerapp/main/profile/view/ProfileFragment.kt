package com.ijiki16.messengerapp.main.profile.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.databinding.FragmentProfileBinding
import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.main.profile.ProfileContract
import com.ijiki16.messengerapp.main.profile.presenter.ProfilePresenterImpl
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class ProfileFragment : ProfileContract.View, Fragment() {

    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        presenter = ProfilePresenterImpl(
            this,
            AppPreferences(requireActivity().getPreferences(Context.MODE_PRIVATE))
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.retrieveUserInfo()
        initViews()
    }

    private fun initViews() {
        binding.profilePictureIv.setOnClickListener {
            // TODO: open gallery here and choose picture
        }
        binding.updateBtn.setOnClickListener {
            toggleLoading(true)
            val username = binding.nicknameEt.text.toString()
            val about = binding.aboutEt.text.toString()
            // TODO: somehow get profile pic too here
            presenter.updateProfile(username, about)
        }
        binding.signOutBtn.setOnClickListener {
            presenter.logout()
        }
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.loadingPb.visibility = if(isLoading) View.VISIBLE else View.GONE

        binding.signOutBtn.isEnabled = !isLoading
        binding.updateBtn.isEnabled = !isLoading
        binding.aboutEt.isEnabled = !isLoading
        binding.nicknameEt.isEnabled = !isLoading
    }

    override fun showError(error: String) {
        // TODO: add feedback for errors.
    }

    override fun loggedOut() {
        requireActivity().finish()
    }

    override fun userInfoRetrieved(user: UserInfo) = updateScreen(user)

    override fun profileUpdated(user: UserInfo) {
        toggleLoading(false)
        updateScreen(user)
    }

    private fun updateScreen(user: UserInfo) {
        binding.nicknameEt.setText(user.username)
        binding.aboutEt.setText(user.about)

        Glide.with(requireContext())
            .load(user.profilePictureUrl)
            .placeholder(R.drawable.ic_baseline_account_circle_96)
            .error(R.drawable.ic_baseline_cancel_96)
            .into(binding.profilePictureIv)
    }

    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()

    }
}