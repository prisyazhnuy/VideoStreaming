package com.prisyazhnuy.streaming.ui.screens.main.stream.twilio.playback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.cleveroad.bootstrap.kotlin_ext.hide
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.bootstrap.kotlin_ext.show
import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.ui.base.BaseAdapterCallback
import com.prisyazhnuy.streaming.ui.base.BaseViewHolder
import com.twilio.video.RemoteParticipant
import com.twilio.video.VideoView

class ParticipantVH(view: View, private val callback: BaseAdapterCallback?) :
        BaseViewHolder<RemoteParticipant>(view),
        View.OnClickListener {

    companion object {
        internal fun newInstance(inflater: LayoutInflater, parent: ViewGroup?, callback: BaseAdapterCallback?) =
                ParticipantVH(inflater.inflate(R.layout.item_video_view, parent, false), callback)
    }

    private val videoView by lazy { itemView.findViewById<VideoView>(R.id.videoView) }
    private val container by lazy { itemView.findViewById<RelativeLayout>(R.id.container) }
    private val ivMic by lazy { itemView.findViewById<ImageView>(R.id.ivMic) }

    override fun bind(item: RemoteParticipant) {
        with(item) {
            remoteVideoTracks.firstOrNull()?.remoteVideoTrack?.addRenderer(videoView)
            if (remoteAudioTracks.firstOrNull()?.remoteAudioTrack?.isEnabled == true) ivMic.hide() else ivMic.show()
            setClickListeners(container)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.container -> {
                callback?.onClicked(adapterPosition)
            }
        }
    }

}