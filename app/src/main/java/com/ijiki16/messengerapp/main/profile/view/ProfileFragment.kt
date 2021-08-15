package com.ijiki16.messengerapp.main.profile.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.databinding.FragmentProfileBinding
import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.infrastructure.GlideApp
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
            AppPreferences.getInstance(requireActivity().getPreferences(Context.MODE_PRIVATE))
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
            openGalleryForImage()
        }
        binding.updateBtn.setOnClickListener {
            toggleLoading(true)
            val username = binding.nicknameEt.text.toString()
            val about = binding.aboutEt.text.toString()
            presenter.updateProfile(username, about)
        }
        binding.signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            presenter.logout()
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Uri? = result.data?.data
            if (data != null) {

                binding.profilePictureIv.visibility = View.GONE
                binding.profilePicturePb.visibility = View.VISIBLE

                val storageRef = Firebase.storage.reference
                val uid = Firebase.auth.currentUser?.uid ?: ""
                val profileImageUrl = "public/$uid/${data.lastPathSegment}"
                val riversRef = storageRef.child(profileImageUrl)
                val uploadTask = riversRef.putFile(data)

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener {
                    showError(it.message ?: "Could not upload an image")
                }.addOnSuccessListener { _ ->
                    presenter.updateImageUrl(profileImageUrl, data)
                }
            }
        }
    }

    override fun profileImageUpdated(data: Uri) {
        binding.profilePictureIv.visibility = View.VISIBLE
        binding.profilePicturePb.visibility = View.GONE
        binding.profilePictureIv.setImageURI(data)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.loadingPb.visibility = if(isLoading) View.VISIBLE else View.GONE

        binding.signOutBtn.isEnabled = !isLoading
        binding.updateBtn.isEnabled = !isLoading
        binding.aboutEt.isEnabled = !isLoading
        binding.nicknameEt.isEnabled = !isLoading
    }

    override fun showError(error: String) {
        binding.profilePictureIv.visibility = View.VISIBLE
        binding.profilePicturePb.visibility = View.GONE
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
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

        if (user.profilePictureUrl.isNotBlank()) {
            val storageReference = Firebase.storage.reference.child(user.profilePictureUrl)
            GlideApp.with(this)
                .load(storageReference)
                .placeholder(R.drawable.ic_baseline_account_circle_96)
                .error(R.drawable.ic_baseline_cancel_96)
                .into(binding.profilePictureIv)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()

    }
}