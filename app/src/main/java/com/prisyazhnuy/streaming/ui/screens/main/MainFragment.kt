package com.prisyazhnuy.streaming.ui.screens.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.utils.bindInterfaceOrThrow
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainVM>(),
        View.OnClickListener {

    companion object {
        fun newInstance() = MainFragment().apply { arguments = Bundle() }
    }

    override val containerId = R.id.container
    override val layoutId = R.layout.fragment_main
    override val viewModelClass = MainVM::class.java

    private var callback: MainCallback? = null

    override fun getScreenTitle() = NO_TITLE
    override fun getToolbarId() = NO_TOOLBAR
    override fun hasToolbar() = false
    override fun observeLiveData(viewModel: MainVM) = Unit

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnWowza, btnRed5Pro, btnTwilio, btnJitsi)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnWowza -> callback?.openWowzaStream()
            R.id.btnRed5Pro -> callback?.openRed5ProStream()
            R.id.btnTwilio -> callback?.openTwilioStream()
            R.id.btnJitsi -> callback?.openJitsiStream()
        }
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

}