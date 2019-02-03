package com.prisyazhnuy.streaming.network_module_test

import com.prisyazhnuy.streaming.extensions.getMockResponse
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection.*


/**
 * @see Dispatcher Handler for mock server requests.
 */
class RequestDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest?) = when (request?.path) {
        EndpointPath.ACCOUNT.path -> getMockResponse(HTTP_OK, "session.json")
        EndpointPath.LOGIN.path -> getMockResponse(HTTP_OK, "session.json")
        EndpointPath.LOGOUT.path -> getMockResponse(HTTP_OK, "unit.json")
        EndpointPath.REFRESH_TOKEN.path -> getMockResponse(HTTP_OK, "unit.json")
        else -> getMockResponse(HTTP_NOT_FOUND, "unit.json")
    }
}

/**
 * @see Dispatcher Handler for mock server requests.
 */
class ErrorDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest?) = getMockResponse(HTTP_BAD_REQUEST, "unit.json")
}
