package com.ijiki16.messengerapp.users

import com.ijiki16.messengerapp.users.model.UserItemEntity

interface UsersContract {

    interface Presenter {
        fun loadMore(term: String, lastIndex: Int)
    }

    interface View {
        fun showError(error: String)
        fun moreLoaded(data: List<UserItemEntity>)
        fun dataLoaded(data: List<UserItemEntity>)
    }

}