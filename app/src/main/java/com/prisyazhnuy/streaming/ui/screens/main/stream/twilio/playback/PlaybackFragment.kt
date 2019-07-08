package com.prisyazhnuy.streaming.ui.screens.main.stream.twilio.playback

import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseListFragment
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.prisyazhnuy.streaming.BuildConfig
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.safeSingleObserve
import com.prisyazhnuy.streaming.ui.screens.main.stream.twilio.StatusCallback
import com.prisyazhnuy.streaming.utils.EMPTY_STRING
import com.prisyazhnuy.streaming.utils.LOG
import com.twilio.video.*
import kotlinx.android.synthetic.main.fragment_twilio_playback.*


class PlaybackFragment : BaseListFragment<PlaybackVM, RemoteParticipant>(),
        View.OnClickListener,
        ParticipantAdapterCallback {


    companion object {
        private val NAME = MiscellaneousUtils.getExtra("NAME", PlaybackFragment::class.java)

        fun newInstance(streamName: String) = PlaybackFragment().apply {
            arguments = Bundle().apply {
                putString(NAME, streamName)
            }
        }
    }

    override val layoutId = R.layout.fragment_twilio_playback
    override val viewModelClass = PlaybackVM::class.java


    private var isPlayback = false
    private val streamName by lazy { arguments?.getString(NAME).orEmpty() }
    private val roomListener by lazy {
        object : StatusCallback() {
            override fun onConnected(room: Room?) {
                room?.remoteParticipants?.forEach { it.setListener(participantListener()) }
            }

            override fun onParticipantConnected(room: Room?, remoteParticipant: RemoteParticipant?) {
                remoteParticipant?.setListener(participantListener())
            }
        }
    }

    private var room: Room? = null

    override fun getScreenTitle() = NO_TITLE

    override fun getToolbarId() = NO_TOOLBAR

    override fun hasToolbar() = false

    override var endpoint = EMPTY_STRING
    override val noResultViewId = NO_ID
    override val recyclerViewId = R.id.recyclerView
    override val refreshLayoutId = NO_ID
    override var versionName = EMPTY_STRING

    private val participantAdapter by lazy { context?.let { ctx -> ParticipantAdapter(ctx, this@PlaybackFragment) } }

    override fun getAdapter() = participantAdapter

    override fun getEndPointTextViewId() = NO_ID

    override fun getVersionsLayoutId() = NO_ID

    override fun getVersionsTextViewId() = NO_ID

    override fun isDebug() = false

    override fun loadInitial() {
    }

    override fun loadMoreData() = Unit

    override fun showBlockBackAlert() = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(btnPlayback)
    }

    override fun observeLiveData(viewModel: PlaybackVM) {
        roomListener.statusLD.safeSingleObserve(this) { LOG.e(message = it) }
    }

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

    // Create an audio track
    private val localAudioTrack by lazy { context?.let { LocalAudioTrack.create(it, true) } }
    // Create a video track
    private val localVideoTrack by lazy {
        context?.let { ctx ->
            LocalVideoTrack.create(ctx,
                    true,
                    CameraCapturer(ctx, CameraCapturer.CameraSource.FRONT_CAMERA))?.apply {
                addRenderer(videoView)
            }
        }
    }

    private fun startPlayback() {
        safeLet(context, localAudioTrack, localVideoTrack) { ctx, audioTrack, videoTrack ->
            val connectOptions = ConnectOptions.Builder(BuildConfig.TWILIO_ACCESS_TOKEN)
                    .roomName(streamName)
                    .audioTracks(listOf(audioTrack))
                    .videoTracks(listOf(videoTrack))
                    .build()
            room = Video.connect(ctx, connectOptions, roomListener)
        }
    }

    override fun onParticipantClicked(participant: RemoteParticipant) {
        showSnackBar("onParticipantClicked")
    }

    private fun stopPlayback() {
        room?.disconnect()
    }

    /* In the Participant listener, we can respond when the Participant adds a Video
Track by rendering it on screen: */
    private fun participantListener(): RemoteParticipant.Listener {
        return object : RemoteParticipant.Listener {
            override fun onDataTrackPublished(remoteParticipant: RemoteParticipant?, remoteDataTrackPublication: RemoteDataTrackPublication?) {}

            override fun onAudioTrackEnabled(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?) {}

            override fun onAudioTrackPublished(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?) {}

            override fun onVideoTrackPublished(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?) {}

            override fun onVideoTrackSubscribed(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?, remoteVideoTrack: RemoteVideoTrack?) {
                remoteParticipant?.let { onDataRangeLoaded(listOf(it)) }
            }

            override fun onVideoTrackEnabled(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?) {
//                remoteVideoTrackPublication?.remoteVideoTrack?.addRenderer(primaryVideoView)
            }

            override fun onVideoTrackDisabled(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?) {}

            override fun onVideoTrackUnsubscribed(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?, remoteVideoTrack: RemoteVideoTrack?) {}

            override fun onDataTrackSubscriptionFailed(remoteParticipant: RemoteParticipant?, remoteDataTrackPublication: RemoteDataTrackPublication?, twilioException: TwilioException?) {}

            override fun onAudioTrackDisabled(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?) {}

            override fun onDataTrackSubscribed(remoteParticipant: RemoteParticipant?, remoteDataTrackPublication: RemoteDataTrackPublication?, remoteDataTrack: RemoteDataTrack?) {}

            override fun onAudioTrackUnsubscribed(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?, remoteAudioTrack: RemoteAudioTrack?) {}

            override fun onAudioTrackSubscribed(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?, remoteAudioTrack: RemoteAudioTrack?) {}

            override fun onVideoTrackSubscriptionFailed(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?, twilioException: TwilioException?) {}

            override fun onAudioTrackSubscriptionFailed(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?, twilioException: TwilioException?) {}

            override fun onAudioTrackUnpublished(remoteParticipant: RemoteParticipant?, remoteAudioTrackPublication: RemoteAudioTrackPublication?) {}

            override fun onVideoTrackUnpublished(remoteParticipant: RemoteParticipant?, remoteVideoTrackPublication: RemoteVideoTrackPublication?) {}

            override fun onDataTrackUnsubscribed(remoteParticipant: RemoteParticipant?, remoteDataTrackPublication: RemoteDataTrackPublication?, remoteDataTrack: RemoteDataTrack?) {}

            override fun onDataTrackUnpublished(remoteParticipant: RemoteParticipant?, remoteDataTrackPublication: RemoteDataTrackPublication?) {}
        }
    }
}