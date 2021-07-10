package com.ijiki16.messengerapp

import android.app.Activity
import android.os.Bundle
import com.ijiki16.messengerapp.databinding.ActivityLauncherBinding

class LauncherActivity: Activity() {

    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}