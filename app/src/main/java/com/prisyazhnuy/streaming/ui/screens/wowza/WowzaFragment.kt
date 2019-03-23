package com.prisyazhnuy.streaming.ui.screens.wowza

import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.text
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.ui.screens.wowza.broadcast.BroadcastFragment
import com.prisyazhnuy.streaming.ui.screens.wowza.playback.PlaybackFragment
import kotlinx.android.synthetic.main.fragment_wowza.*

class WowzaFragment : BaseFragment<WowzaVM>(),
        View.OnClickListener {

    companion object {
        fun newInstance() = WowzaFragment().apply {
            arguments = Bundle()
        }
    }

    override val layoutId = R.layout.fragment_wowza
    override val viewModelClass = WowzaVM::class.java
    override val containerId = R.id.container

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnStream, btnPlayback)
    }

    override fun observeLiveData(viewModel: WowzaVM) = Unit

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStream -> openBroadcast()
            R.id.btnPlayback -> openPlayback()
        }
    }

    private fun openBroadcast() {
        replaceFragment(BroadcastFragment.newInstance(etStreamName.text()))
    }

    private fun openPlayback() {
        replaceFragment(PlaybackFragment.newInstance(etStreamName.text()))
    }

    override fun onBackPressed(): Boolean {
        return with(childFragmentManager) {
            backStackEntryCount.takeUnless { it == 0 }?.let {
                popBackStack()
                true
            } ?: super.onBackPressed()
        }
    }

}