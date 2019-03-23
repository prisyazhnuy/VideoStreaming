package com.prisyazhnuy.streaming.ui.screens.wowza.playback

import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.BuildConfig
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.safeSingleObserve
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.ui.screens.wowza.StatusCallback
import com.wowza.gocoder.sdk.api.player.WOWZPlayerConfig
import kotlinx.android.synthetic.main.fragment_wowza_playback.*

class PlaybackFragment : BaseFragment<PlaybackVM>(),
        View.OnClickListener {

    companion object {
        fun newInstance() = PlaybackFragment().apply {
            arguments = Bundle()
        }
    }

    override val layoutId = R.layout.fragment_wowza_playback
    override val viewModelClass = PlaybackVM::class.java
    override val containerId = R.id.container
    private val streamPlayerConfig by lazy {
        WOWZPlayerConfig().apply {
            isPlayback = true
            hostAddress = BuildConfig.WOWZA_HOST_ADDRESS
            portNumber = BuildConfig.WOWZA_PORT
            applicationName = BuildConfig.WOWZA_APP_NAME
            streamName = BuildConfig.WOWZA_STREAM_NAME
            isAudioEnabled = true
        }
    }
    private val statusCallback by lazy { StatusCallback() }

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnPlay, btnStop)
    }

    override fun observeLiveData(viewModel: PlaybackVM) {
        statusCallback.statusLD.safeSingleObserve(this) { showSnackBar(it) }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPlay -> vwStreamPlayer.play(streamPlayerConfig, statusCallback)
            R.id.btnStop -> vwStreamPlayer.stop(statusCallback)
        }
    }
}