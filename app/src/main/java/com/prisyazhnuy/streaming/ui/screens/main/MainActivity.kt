package com.prisyazhnuy.streaming.ui.screens.main

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseActivity
import com.prisyazhnuy.streaming.ui.screens.main.stream.StreamCallback
import com.prisyazhnuy.streaming.ui.screens.main.stream.StreamFragment
import com.prisyazhnuy.streaming.ui.screens.main.stream.StreamService

class MainActivity : BaseActivity<MainVM>(),
        StreamCallback,
        MainCallback {

    override val viewModelClass = MainVM::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_splash

    companion object {

        fun start(context: Context?) {
            context?.apply ctx@{ startActivity(getIntent(this@ctx)) }
        }

        fun getIntent(context: Context?) = Intent(context, MainActivity::class.java)
    }

    override fun hasProgressBar() = false

    override fun onStart() {
        super.onStart()
        replaceFragment(MainFragment.newInstance(), false)
    }

    override fun observeLiveData(viewModel: MainVM) = Unit

    override fun openFragment(fragment: Fragment) {
        replaceFragment(fragment)
    }

    override fun openWowzaStream() {
        replaceFragment(StreamFragment.newInstance(StreamService.WOWZA))
    }

    override fun openRed5ProStream() {
        replaceFragment(StreamFragment.newInstance(StreamService.RED5PRO))
    }

    override fun openTwilioStream() {
        replaceFragment(StreamFragment.newInstance(StreamService.TWILIO))
    }

    override fun openJitsiStream() {
        replaceFragment(StreamFragment.newInstance(StreamService.JITSI))
    }
}
