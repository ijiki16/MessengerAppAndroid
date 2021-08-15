package com.ijiki16.messengerapp.users.presenter

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.main.profile.presenter.ProfilePresenterImpl
import com.ijiki16.messengerapp.users.UsersContract
import com.ijiki16.messengerapp.users.model.UserItemEntity

class UsersPresenterImpl(private val view: UsersContract.View) : UsersContract.Presenter {

    override fun loadMore(term: String, lastIndex: Int) {
        if (term.length < 3) {
            view.moreLoaded(emptyList())
        } else {
            val database = Firebase.database
            val usersReference = database.getReference(ProfilePresenterImpl.DB_USERS)
            usersReference
                .orderByChild(DB_USERNAME)
                .startAt(term).endAt(term + "z")
                .limitToFirst(MAX_PER_LOAD + lastIndex)
                .get().addOnSuccessListener {
                    if (it.value == null) {
                        view.dataLoaded(emptyList())
                    } else {
                        val users = (it.value as HashMap<*, *>)

                        val usersList =
                            users.entries.map { userEntity ->
                                val userId = userEntity.key
                                val userMap = (userEntity.value as HashMap<*, *>)
                                UserItemEntity(
                                    userId.toString(),
                                    userMap[DB_USERNAME].toString(),
                                    userMap[DB_ABOUT_KEY].toString(),
                                    userMap[DB_PROFILE_KEY].toString(),
                                )
                            }

                        if(lastIndex == 0 ) {
                            view.dataLoaded(usersList)
                        } else {
                            view.moreLoaded(usersList.slice(lastIndex until usersList.size))
                        }
                    }
                }.addOnFailureListener {
                    view.showError(it.message ?: "Could no load user profiles")
                }


        }
    }

    companion object {
        const val DB_ABOUT_KEY = "about"
        const val DB_PROFILE_KEY = "profile"
        const val DB_USERNAME = "username"

        const val MAX_PER_LOAD = 10
    }
}