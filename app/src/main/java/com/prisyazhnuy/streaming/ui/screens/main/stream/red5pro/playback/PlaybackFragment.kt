package com.prisyazhnuy.streaming.ui.screens.main.stream.red5pro.playback

import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.VSApp
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.red5pro.streaming.R5Connection
import com.red5pro.streaming.R5Stream
import com.red5pro.streaming.R5StreamProtocol
import com.red5pro.streaming.config.R5Configuration
import kotlinx.android.synthetic.main.fragment_red5pro_playback.*


class PlaybackFragment : BaseFragment<PlaybackVM>(),
        View.OnClickListener {

    companion object {
        private val NAME = MiscellaneousUtils.getExtra("NAME", PlaybackFragment::class.java)

        fun newInstance(streamName: String) = PlaybackFragment().apply {
            arguments = Bundle().apply {
                putString(NAME, streamName)
            }
        }
    }

    override val containerId = NO_ID
    override val layoutId = R.layout.fragment_red5pro_playback
    override val viewModelClass = PlaybackVM::class.java

    private val config by lazy {
        R5Configuration(R5StreamProtocol.SRTP,
                "192.168.0.104",
                8554,
                "live",
                3.0f).apply {
            licenseKey = "XX5C-INYZ-HMMW-GNJS"
            bundleID = VSApp.instance.packageName
        }
    }

    private var isPlayback = false
    private lateinit var stream: R5Stream
    private val streamName by lazy { arguments?.getString(NAME).orEmpty() }

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnPlayback)
    }

    override fun observeLiveData(viewModel: PlaybackVM) = Unit

    override fun onPause() {
        super.onPause()
        if (isPlayback) onPlaybackToggle()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPlayback -> onPlaybackToggle()
        }
    }

    private fun onPlaybackToggle() {
        if (isPlayback) stopPlayback() else startPlayback()
        isPlayback = isPlayback.not()
        btnPlayback.setText(if (isPlayback) R.string.stop else R.string.play)
    }

    private fun startPlayback() {
        stream = R5Stream(R5Connection(config))
        subscribeView.attachStream(stream)
        stream.play(streamName)
    }

    private fun stopPlayback() {
        stream.stop()
    }
}