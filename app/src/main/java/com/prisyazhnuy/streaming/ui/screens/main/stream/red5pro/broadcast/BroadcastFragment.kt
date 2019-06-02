package com.prisyazhnuy.streaming.ui.screens.main.stream.red5pro.broadcast

import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.VSApp
import com.prisyazhnuy.streaming.ui.base.BaseFragment
import com.prisyazhnuy.streaming.utils.SimpleSurfaceCallback
import com.red5pro.streaming.R5Connection
import com.red5pro.streaming.R5Stream
import com.red5pro.streaming.R5Stream.RecordType
import com.red5pro.streaming.R5StreamProtocol
import com.red5pro.streaming.config.R5Configuration
import com.red5pro.streaming.source.R5Camera
import com.red5pro.streaming.source.R5Microphone
import kotlinx.android.synthetic.main.fragment_red5pro_broadcast.*


class BroadcastFragment : BaseFragment<BroadcastVM>(),
        View.OnClickListener {

    companion object {
        private val NAME = MiscellaneousUtils.getExtra("NAME", BroadcastFragment::class.java)

        fun newInstance(streamName: String) = BroadcastFragment().apply {
            arguments = Bundle().apply {
                putString(NAME, streamName)
            }
        }
    }

    override val containerId = R.id.container
    override val layoutId = R.layout.fragment_red5pro_broadcast
    override val viewModelClass = BroadcastVM::class.java

    private var isBroadcasting = false

    private val config by lazy {
        R5Configuration(R5StreamProtocol.RTSP,
                "192.168.42.56",
                5080,
                "live",
                1.0f).apply {
            licenseKey = "MOJ4-ORCR-S4WC-KKVF"
            bundleID = VSApp.instance.packageName
        }
    }

    private lateinit var camera: Camera
    private lateinit var stream: R5Stream
    private val surfaceCallback by lazy {
        object : SimpleSurfaceCallback() {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                camera.apply {
                    setPreviewDisplay(holder)
                    startPreview()
                }
            }
        }
    }

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnBroadcast)
    }

    override fun onResume() {
        super.onResume()
        preview()
    }

    override fun onPause() {
        super.onPause()
        if (isBroadcasting) onBroadcastToggle()
    }

    override fun observeLiveData(viewModel: BroadcastVM) = Unit

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBroadcast -> onBroadcastToggle()
        }
    }

    private fun preview() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
        surfaceView?.holder?.addCallback(surfaceCallback)
    }

    private fun onBroadcastToggle() {
        if (isBroadcasting) {
            stopBroadcast()
        } else {
            startBroadcast()
        }
        isBroadcasting = isBroadcasting.not()
        btnBroadcast.setText(if (isBroadcasting) R.string.stop else R.string.broadcast)
    }

    private fun startBroadcast() {
        camera.stopPreview()
        stream = R5Stream(R5Connection(config)).apply {
            setView(surfaceView)
            attachCamera(R5Camera(camera, surfaceView.width, surfaceView.height))
            attachMic(R5Microphone())
            publish(arguments?.getString(NAME).orEmpty(), RecordType.Live)
        }
        camera.startPreview()
    }

    private fun stopBroadcast() {
        stream.stop()
        camera.startPreview()
    }
}