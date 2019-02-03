package com.prisyazhnuy.streaming.network.exceptions

import com.prisyazhnuy.streaming.utils.NOTHING

/**
 * Error from server.
 */
open class ApiException() : Exception() {

    open var statusCode: Int? = NOTHING
    var mMessage: String? = NOTHING
    var v: String? = NOTHING
    var errors: List<ValidationError>? = NOTHING
    var stacktrace: String? = NOTHING

    constructor(statusCode: Int?,
                v: String?,
                message: String?,
                errors: List<ValidationError>?,
                stacktrace: String? = NOTHING) : this() {
        this.statusCode = statusCode
        this.mMessage = message
        this.v = v
        this.errors = errors
        this.stacktrace = stacktrace
    }

    override val message = mMessage

    override fun toString(): String {
        return "ApiException(statusCode=$statusCode, mMessage=$mMessage, v=$v, errors=$errors, stacktrace=$stacktrace, message=$message)"
    }
}
