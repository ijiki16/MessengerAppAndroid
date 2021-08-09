package com.ijiki16.messengerapp.main.home.presenter

import com.ijiki16.messengerapp.main.home.HomeContract
import com.ijiki16.messengerapp.main.home.model.HomeMessageEntity

class HomePresenterImpl(
    private val view: HomeContract.View
): HomeContract.Presenter {

    override fun loadMore() {
        //TODO: do some loading from server
        val item1 = HomeMessageEntity(
            "hello tony1",
            "https://pbs.twimg.com/profile_images/1214426391946629121/bb12yAuv_400x400.png",
            System.currentTimeMillis() - 1000*60*60*60*2,
            "mtsar16")
        val item2 = HomeMessageEntity(
            "hello tony2",
            "https://pbs.twimg.com/profile_images/1214426391946629121/bb12yAuv_400x400.png",
            System.currentTimeMillis() - 1000*60*60*2,
            "mtsar17")
        val item3 = HomeMessageEntity(
            "hello tony3",
            "https://pbs.twimg.com/profile_images/1214426391946629121/bb12yAuv_400x400.png",
            System.currentTimeMillis() - 1000*60*2,
            "mtsar18")
        val item4 = HomeMessageEntity(
            "hello tony4",
            "https://pbs.twimg.com/profile_images/1214426391946629121/bb12yAuv_400x400.png",
            System.currentTimeMillis() - 1000*2,
            "mtsar19")
        view.moreLoaded(listOf(item1, item1, item1, item2, item3, item3, item4, item4))
    }

}