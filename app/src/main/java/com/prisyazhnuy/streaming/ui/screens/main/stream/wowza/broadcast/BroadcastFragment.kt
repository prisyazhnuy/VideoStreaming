package com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.broadcast

import android.Manifest
import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.bootstrap.kotlin_ext.withNotNull
import com.prisyazhnuy.streaming.BuildConfig
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.safeSingleObserve
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.ui.screens.main.stream.wowza.StatusCallback
import com.prisyazhnuy.streaming.utils.LOG
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wowza.gocoder.sdk.api.WowzaGoCoder
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice
import com.wowza.gocoder.sdk.api.devices.WOWZCamera
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_wowza_broadcast.*


class BroadcastFragment : BaseFragment<BroadcastVM>(),
        View.OnClickListener {
    override val layoutId = R.layout.fragment_wowza_broadcast
    override val viewModelClass = BroadcastVM::class.java
    override val containerId = R.id.container

    companion object {
        private val NAME = MiscellaneousUtils.getExtra("NAME", BroadcastFragment::class.java)

        fun newInstance(streamName: String) = BroadcastFragment().apply {
            arguments = Bundle().apply {
                putString(NAME, streamName)
            }
        }
    }

    private val rxPermission by lazy { RxPermissions(this) }
    // Create an audio device instance for capturing and broadcasting audio
    private val goCoderAudioDevice by lazy { WOWZAudioDevice() }
    // Create a broadcaster instance
    private val goCoderBroadcaster by lazy { WOWZBroadcast() }
    private val statusCallback by lazy { StatusCallback() }

    // Create a configuration instance for the broadcaster
    private val goCoderBroadcastConfig by lazy {
        WOWZBroadcastConfig(WOWZMediaConfig.FRAME_SIZE_640x480).apply {
            // Set the connection properties for the target Wowza Streaming Engine server or Wowza Streaming Cloud live stream
            hostAddress = BuildConfig.WOWZA_HOST_ADDRESS
            portNumber = BuildConfig.WOWZA_PORT
            applicationName = BuildConfig.WOWZA_APP_NAME
            streamName = arguments?.getString(NAME).orEmpty()
//            username = "wowzaStream"
//            password = "314159265"
            username = "client40770"
            password = "fd80c179"

// Designate the camera preview as the video source
            videoBroadcaster = cameraPreview

// Designate the audio device as the audio broadcaster
            audioBroadcaster = goCoderAudioDevice
        }
    }

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWowza()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnBroadcast)
    }

    override fun onResume() {
        super.onResume()
        rxPermission
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(Consumer<Boolean> { granted ->
                    btnBroadcast.isEnabled = granted
                    if (granted) showPreview()
                })
    }

    override fun observeLiveData(viewModel: BroadcastVM) {
        statusCallback.statusLD.safeSingleObserve(this) { showSnackBar(it) }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBroadcast -> startBroadcast()
        }
    }

    private fun initWowza() {
        // Initialize the GoCoder SDK
        val sdk = WowzaGoCoder.init(context, BuildConfig.WOWZA_API_KEY)

        // If initialization failed, retrieve the last error and display it
        WowzaGoCoder.getLastError()?.let {
            LOG.e(message = "GoCoder SDK error: ${it.errorDescription}")
        }
    }

    private fun showPreview() {
        withNotNull(cameraPreview) {
            if (isPreviewPaused) onResume() else startPreview(WOWZMediaConfig().apply {
                setCamera(WOWZCamera.DIRECTION_FRONT)
            })
        }
    }

    private fun startBroadcast() {
// Ensure the minimum set of configuration settings have been specified necessary to
        // initiate a broadcast streaming session
        val configValidationError = goCoderBroadcastConfig.validateForBroadcast()

        configValidationError?.let {
            showSnackBar(it.errorDescription)
        } ?: run {
            if (goCoderBroadcaster.status.isRunning) {
                // Stop the broadcast that is currently running
                goCoderBroadcaster.endBroadcast(statusCallback)
            } else {
                // Start streaming
                goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, statusCallback)
            }
        }
    }
}