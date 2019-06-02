package com.prisyazhnuy.streaming.ui.screens.main.stream.twilio

import androidx.lifecycle.MutableLiveData
import com.twilio.video.RemoteParticipant
import com.twilio.video.Room
import com.twilio.video.TwilioException

open class StatusCallback : Room.Listener {

    val statusLD by lazy { MutableLiveData<String>() }

    override fun onRecordingStarted(room: Room?) {
        statusLD.postValue("onRecordingStarted")
    }

    override fun onParticipantDisconnected(room: Room?, remoteParticipant: RemoteParticipant?) {
        statusLD.postValue("onParticipantDisconnected")
    }

    override fun onParticipantConnected(room: Room?, remoteParticipant: RemoteParticipant?) {
        statusLD.postValue("onParticipantConnected")
    }

    override fun onConnectFailure(room: Room?, twilioException: TwilioException?) {
        statusLD.postValue("onConnectFailure")
    }

    override fun onConnected(room: Room?) {
        statusLD.postValue("onConnected")
    }

    override fun onDisconnected(room: Room?, twilioException: TwilioException?) {
        statusLD.postValue("onDisconnected")
    }

    override fun onRecordingStopped(room: Room?) {
        statusLD.postValue("onRecordingStopped")
    }

}