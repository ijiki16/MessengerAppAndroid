package com.ijiki16.messengerapp.main.home.presenter

import com.ijiki16.messengerapp.main.home.HomeContract

class HomePresenterImpl(
    private val view: HomeContract.View
): HomeContract.Presenter {

    override fun loadMore() {
        //TODO: do some loading from server
//        view.moreLoaded()
    }

}