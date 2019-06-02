package com.prisyazhnuy.streaming.ui.screens.main.stream

import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.text
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.broadcast.BroadcastFragment
import kotlinx.android.synthetic.main.fragment_stream.*

class StreamFragment : BaseFragment<StreamVM>(),
        View.OnClickListener {

    companion object {
        private val SERVICE = MiscellaneousUtils.getExtra("SERVICE", StreamFragment::class.java)

        fun newInstance(service: StreamService) = StreamFragment().apply {
            arguments = Bundle().apply {
                putSerializable(SERVICE, service)
            }
        }
    }

    override val layoutId = R.layout.fragment_stream
    override val viewModelClass = StreamVM::class.java
    override val containerId = R.id.container

    private val service by lazy { arguments?.getSerializable(SERVICE) as? StreamService }

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnStream, btnPlayback)
    }

    override fun observeLiveData(viewModel: StreamVM) = Unit

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStream -> openBroadcast()
            R.id.btnPlayback -> openPlayback()
        }
    }

    private fun openBroadcast() {
        replaceFragment(when(service) {
            StreamService.WOWZA -> com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.broadcast.BroadcastFragment.newInstance(etStreamName.text())
            StreamService.RED5PRO -> com.prisyazhnuy.streaming.ui.screens.main.stream.red5pro.broadcast.BroadcastFragment.newInstance(etStreamName.text())
            StreamService.TWILIO -> com.prisyazhnuy.streaming.ui.screens.main.stream.twilio.playback.PlaybackFragment.newInstance(etStreamName.text())
            else -> com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.broadcast.BroadcastFragment.newInstance(etStreamName.text())
        })
    }

    private fun openPlayback() {
        replaceFragment(when(service) {
            StreamService.WOWZA -> com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.playback.PlaybackFragment.newInstance(etStreamName.text())
            StreamService.RED5PRO -> com.prisyazhnuy.streaming.ui.screens.main.stream.red5pro.playback.PlaybackFragment.newInstance(etStreamName.text())
            StreamService.TWILIO -> com.prisyazhnuy.streaming.ui.screens.main.stream.twilio.playback.PlaybackFragment.newInstance(etStreamName.text())
            else -> com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.playback.PlaybackFragment.newInstance(etStreamName.text())
        })
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