package com.prisyazhnuy.streaming.ui.screens.main.stream.twilio.playback

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseRecyclerViewAdapter
import com.prisyazhnuy.streaming.ui.base.BaseAdapterCallback
import com.twilio.video.RemoteParticipant
import java.lang.ref.WeakReference

interface ParticipantAdapterCallback {
    fun onParticipantClicked(participant: RemoteParticipant)
}

class ParticipantAdapter(context: Context, callback: ParticipantAdapterCallback) :
        BaseRecyclerViewAdapter<RemoteParticipant, ParticipantVH>(context),
        BaseAdapterCallback {

    private val weakRefCallback = WeakReference(callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ParticipantVH.newInstance(LayoutInflater.from(parent.context), parent, this)

    override fun onBindViewHolder(holder: ParticipantVH, position: Int) {
        position.takeUnless { it == RecyclerView.NO_POSITION }?.let { holder.bind(getItem(it)) }
    }

    override fun onClicked(position: Int) {
        position.takeUnless { it == RecyclerView.NO_POSITION }?.let {
            weakRefCallback.get()?.onParticipantClicked(getItem(it))
        }
    }
}