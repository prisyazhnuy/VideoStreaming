package com.prisyazhnuy.streaming.ui.screens.main

import android.content.Context
import android.content.Intent
import android.view.View
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseActivity
import com.prisyazhnuy.streaming.ui.screens.wowza.WowzaFragment
import kotlinx.android.synthetic.main.activity_splash.*

class MainActivity : BaseActivity<MainVM>(),
        View.OnClickListener {

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
        setClickListeners(btnWowza)
    }

    override fun observeLiveData(viewModel: MainVM) = Unit

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnWowza -> openWowzaStream()
        }
    }

    private fun openWowzaStream() {
        replaceFragment(WowzaFragment.newInstance())
    }
}