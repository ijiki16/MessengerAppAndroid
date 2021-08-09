package com.ijiki16.messengerapp.main.home

import com.ijiki16.messengerapp.main.home.model.HomeMessageEntity

interface HomeContract {

    interface Presenter {
        fun loadMore(term: String, from: Int)
    }

    interface View {
        fun showError(error: String)
        fun moreLoaded(data: List<HomeMessageEntity>)
    }

}