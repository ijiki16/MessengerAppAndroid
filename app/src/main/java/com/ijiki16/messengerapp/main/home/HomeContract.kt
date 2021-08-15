package com.ijiki16.messengerapp.main.home

import com.ijiki16.messengerapp.main.home.model.HomeMessageEntity

interface HomeContract {

    interface Presenter {
        fun setTerm(newTerm: String)
        fun loadUsers()
        fun populateUser(data: HomeMessageEntity)
    }

    interface View {
        fun showError(error: String)
        fun rawDataLoaded(data: List<HomeMessageEntity>)
        fun userPopulated(data: HomeMessageEntity)
    }

}