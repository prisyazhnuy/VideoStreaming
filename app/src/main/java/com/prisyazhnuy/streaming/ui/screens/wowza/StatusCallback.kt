package com.prisyazhnuy.streaming.ui.screens.wowza

import androidx.lifecycle.MutableLiveData
import com.wowza.gocoder.sdk.api.status.WOWZState
import com.wowza.gocoder.sdk.api.status.WOWZStatus
import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback

class StatusCallback : WOWZStatusCallback {

    val statusLD by lazy { MutableLiveData<String>() }

    override fun onWZStatus(status: WOWZStatus?) {
        when (status?.state) {
            WOWZState.STARTING -> statusLD.postValue("Broadcast initialization")
            WOWZState.READY -> statusLD.postValue("Ready to begin streaming")
            WOWZState.RUNNING -> statusLD.postValue("Streaming is active")
            WOWZState.STOPPING -> statusLD.postValue("Broadcast shutting down")
            WOWZState.IDLE -> statusLD.postValue("The broadcast is stopped")
        }
    }

    override fun onWZError(status: WOWZStatus?) {
        statusLD.postValue("Streaming error: ${status?.lastError?.errorDescription}")
    }
}