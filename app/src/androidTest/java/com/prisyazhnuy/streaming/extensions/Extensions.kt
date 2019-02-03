package com.prisyazhnuy.streaming.extensions

import com.prisyazhnuy.streaming.NPApp
import okhttp3.mockwebserver.MockResponse

fun getJson(path: String) =
        String(NPApp.instance.assets.open(path).readBytes())

fun getMockResponse(responseCode: Int, bodyPath: String): MockResponse =
        MockResponse()
                .setResponseCode(responseCode)
                .setBody(getJson(bodyPath))