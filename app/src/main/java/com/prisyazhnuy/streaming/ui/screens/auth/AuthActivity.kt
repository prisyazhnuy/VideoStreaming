package com.prisyazhnuy.streaming.ui.screens.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseActivity
import com.prisyazhnuy.streaming.ui.screens.auth.sign_in.SignInCallback
import com.prisyazhnuy.streaming.ui.screens.auth.sign_in.SignInFragment
import com.prisyazhnuy.streaming.ui.screens.auth.sign_up.SignUpCallback
import com.prisyazhnuy.streaming.ui.screens.auth.sign_up.SignUpFragment

class AuthActivity : BaseActivity<AuthViewModel>(),
        SignUpCallback,
        SignInCallback {

    companion object {

        fun start(context: Context?) {
            context?.apply ctx@{ startActivity(getIntent(this@ctx)) }
        }

        fun getIntent(context: Context?) = Intent(context, AuthActivity::class.java)
    }

    override val viewModelClass = AuthViewModel::class.java

    override val containerId = R.id.container

    override val layoutId = R.layout.activity_auth

    override fun hasProgressBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openSignUp()
    }

    override fun observeLiveData(viewModel: AuthViewModel) {
        // Put your code here....
    }

    override fun openSignIn() {
        replaceFragment(SignInFragment.newInstance(), false)
    }

    override fun openSignUp(){
        replaceFragment(SignUpFragment.newInstance(), false)
    }
}
