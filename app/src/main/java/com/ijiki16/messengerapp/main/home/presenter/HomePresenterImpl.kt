package com.ijiki16.messengerapp.main.home.presenter

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.launcher.model.LauncherActivityInteractor
import com.ijiki16.messengerapp.main.home.HomeContract
import com.ijiki16.messengerapp.main.home.model.HomeMessageEntity


class HomePresenterImpl(
    private val view: HomeContract.View
) : HomeContract.Presenter {

    // cached data
    private var usersDataSnapshot =
        java.util.Collections.synchronizedList(mutableListOf<HomeMessageEntity>())

    private var term: String = ""

    override fun setTerm(newTerm: String) {
        term = newTerm
        view.rawDataLoaded(usersDataSnapshot.filter { it.userNickname == null ||  it.userNickname.contains(term) })
    }

    override fun loadUsers() {
        val database = Firebase.database
        val messagesReference = database.getReference(DB_MESSAGES)

        val myUserId = Firebase.auth.currentUser?.uid!!

        // Realtime database does not have sql "like" query so we have to load everything at once here :(
        // https://stackoverflow.com/questions/44942917/how-to-implement-sqlite-like-query-in-android-firebase-database
        messagesReference
            .orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value == null) {
                        view.rawDataLoaded(emptyList())
                        return
                    }
                    // k: user1|user2, v: conversationObj
                    val myConversations = (dataSnapshot.value as HashMap<*, *>)
                        .filter { (it.key as String).contains(myUserId) }

                    val data = myConversations
                        .toList()
                        .map {
                            val key = it.first.toString()
                            val userId = if (key.startsWith(myUserId)) {
                                key.split("|")[1]
                            } else {
                                key.split("|")[0]
                            }
                            // k: timestamp, v: messageObj
                            val lastMessageObj = (it.second as HashMap<*, *>)
                                .entries.maxByOrNull { entry ->
                                    entry.key.toString()
                                }!!

                            val lastMessage = (lastMessageObj.value as HashMap<*, *>)
                            val userData =
                                usersDataSnapshot.firstOrNull { user -> user.userId == userId }

                            val result = HomeMessageEntity(
                                userId,
                                lastMessage[DB_TEXT].toString(),
                                userData?.userProfileUrl,
                                lastMessageObj.key.toString().toLong(),
                                userData?.userNickname,
                                userData?.userAbout
                            )
                            val idx = usersDataSnapshot.indexOfFirst { user -> user.userId == result.userId }
                            if (idx != -1) {
                                usersDataSnapshot[idx] = result
                            } else {
                                usersDataSnapshot.add(result)
                            }
                            result
                        }
                    view.rawDataLoaded(data.filter { it.userNickname == null || it.userNickname.contains(term) })
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    view.showError(databaseError.message)
                }
            })
    }

    override fun populateUser(data: HomeMessageEntity) {
        val database = Firebase.database
        val usersReference = database.getReference(LauncherActivityInteractor.DB_USERS)
        val userReference = usersReference.child(data.userId)

        userReference.get().addOnSuccessListener {
            val userInfo = (it.value as HashMap<*, *>)
            val result = HomeMessageEntity(
                data.userId,
                data.lastMessage,
                userInfo[DB_PROFILE_KEY].toString(),
                data.lastMessageDateTimestamp,
                userInfo[DB_USERNAME].toString(),
                userInfo[DB_ABOUT_KEY].toString(),
            )
            val index = usersDataSnapshot.indexOfFirst { user-> user.userId == data.userId }
            usersDataSnapshot[index] = result
            view.userPopulated(result)
        }.addOnFailureListener {
            view.showError(it.message ?: "Connection to database was lost.")
        }
    }

    companion object {
        const val DB_MESSAGES = "messages"
        const val DB_TEXT = "text"
        const val DB_PROFILE_KEY = "profile"
        const val DB_USERNAME = "username"
        const val DB_ABOUT_KEY = "about"
    }

}