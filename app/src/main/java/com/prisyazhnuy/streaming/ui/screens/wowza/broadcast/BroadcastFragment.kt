package com.prisyazhnuy.streaming.ui.screens.wowza.broadcast

import android.Manifest
import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.bootstrap.kotlin_ext.withNotNull
import com.prisyazhnuy.streaming.BuildConfig
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.utils.LOG
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wowza.gocoder.sdk.api.WowzaGoCoder
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice
import com.wowza.gocoder.sdk.api.status.WOWZState
import com.wowza.gocoder.sdk.api.status.WOWZStatus
import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_wowza_broadcast.*


class BroadcastFragment : BaseFragment<BroadcastVM>(),
        View.OnClickListener,
        WOWZStatusCallback {
    override val layoutId = R.layout.fragment_wowza_broadcast
    override val viewModelClass = BroadcastVM::class.java

    companion object {
        fun newInstance() = BroadcastFragment().apply {
            arguments = Bundle()
        }
    }

    private val rxPermission by lazy { RxPermissions(this) }
    // Create an audio device instance for capturing and broadcasting audio
    private val goCoderAudioDevice by lazy { WOWZAudioDevice() }
    // Create a broadcaster instance
    private val goCoderBroadcaster by lazy { WOWZBroadcast() }

    // Create a configuration instance for the broadcaster
    private val goCoderBroadcastConfig by lazy {
        WOWZBroadcastConfig(WOWZMediaConfig.FRAME_SIZE_1920x1080).apply {
            // Set the connection properties for the target Wowza Streaming Engine server or Wowza Streaming Cloud live stream
            hostAddress = BuildConfig.WOWZA_HOST_ADDRESS
            portNumber = BuildConfig.WOWZA_PORT
            applicationName = BuildConfig.WOWZA_APP_NAME
            streamName = BuildConfig.WOWZA_STREAM_NAME

// Designate the camera preview as the video source
            videoBroadcaster = camera_preview

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
        setClickListeners(broadcast_button)
    }

    override fun onResume() {
        super.onResume()
        rxPermission
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(Consumer<Boolean> { granted ->
                    broadcast_button.isEnabled = granted
                    if (granted) showPreview()
                })
    }

    override fun observeLiveData(viewModel: BroadcastVM) {
        with(viewModel) {
            statusEmmiter
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { showSnackBar(it) }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.broadcast_button -> startBroadcast()
        }
    }

    private fun initWowza() {
        // Initialize the GoCoder SDK
        val sdk = WowzaGoCoder.init(context, BuildConfig.WOWZA_API_KEY)

//        if (goCoder == null) {
        // If initialization failed, retrieve the last error and display it
        val goCoderInitError = WowzaGoCoder.getLastError()
        LOG.e(message = "GoCoder SDK error: ${goCoderInitError.errorDescription}")
//        }
    }

    private fun showPreview() {
        withNotNull(camera_preview) {
            if (isPreviewPaused) onResume() else startPreview()
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
                goCoderBroadcaster.endBroadcast(this)
            } else {
                // Start streaming
                goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this)
            }
        }
    }

    private val statusEmmiter by lazy { PublishSubject.create<String>() }

    override fun onWZStatus(status: WOWZStatus?) {
        statusEmmiter.apply {
            when (status?.state) {
                WOWZState.STARTING -> onNext("Broadcast initialization")
                WOWZState.READY -> onNext("Ready to begin streaming")
                WOWZState.RUNNING -> onNext("Streaming is active")
                WOWZState.STOPPING -> onNext("Broadcast shutting down")
                WOWZState.IDLE -> onNext("The broadcast is stopped")
            }
        }
    }

    override fun onWZError(status: WOWZStatus?) {
        statusEmmiter.onNext("Streaming error: ${status?.lastError?.errorDescription}")
    }
}