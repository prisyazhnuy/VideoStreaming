package com.prisyazhnuy.streaming.network.exceptions

import com.prisyazhnuy.streaming.R
import com.prisyazhnuy.streaming.extensions.getStringApp

class ServerException : ApiException() {

    companion object {
        // TODO change server error message
        private val ERROR_MESSAGE = getStringApp(R.string.server_error)
        private const val STATUS_CODE = 500
    }

    override val message: String = ERROR_MESSAGE
    override var statusCode: Int? = STATUS_CODE
}
