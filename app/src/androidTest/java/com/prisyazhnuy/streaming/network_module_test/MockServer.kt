package com.prisyazhnuy.streaming.network_module_test

import com.prisyazhnuy.streaming.BuildConfig
import com.prisyazhnuy.streaming.extensions.getMockResponse
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer


class MockServer {

    fun initMockServer(dispatcher: Dispatcher): MockWebServer =
            initMockServer().apply { setDispatcher(dispatcher) }

    fun initMockServer(responseCode: Int, bodyPath: String): MockWebServer =
            initMockServer().apply { enqueue(getMockResponse(responseCode, bodyPath)) }

    fun initMockServer(): MockWebServer =
            MockWebServer().apply { start(BuildConfig.LOCALHOST_PORT) }
}
