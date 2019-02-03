package com.prisyazhnuy.streaming.ui.screens.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseActivity
import com.prisyazhnuy.streaming.ui.screens.auth.AuthActivity

class SplashActivity : BaseActivity<SplashViewModel>() {

    override val viewModelClass = SplashViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_splash

    companion object {

        fun start(context: Context?) {
            context?.apply ctx@{ startActivity(getIntent(this@ctx)) }
        }

        fun getIntent(context: Context?) = Intent(context, SplashActivity::class.java)
    }

    override fun hasProgressBar() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openAuthScreen()
    }

    override fun observeLiveData(viewModel: SplashViewModel) {
        // Put your code here....
    }

    private fun openAuthScreen() {
        AuthActivity.start(this)
        finish()
    }
}
