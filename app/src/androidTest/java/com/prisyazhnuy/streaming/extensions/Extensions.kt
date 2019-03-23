package com.prisyazhnuy.streaming.extensions

import com.prisyazhnuy.streaming.VSApp
import okhttp3.mockwebserver.MockResponse

fun getJson(path: String) =
        String(VSApp.instance.assets.open(path).readBytes())

fun getMockResponse(responseCode: Int, bodyPath: String): MockResponse =
        MockResponse()
                .setResponseCode(responseCode)
                .setBody(getJson(bodyPath))