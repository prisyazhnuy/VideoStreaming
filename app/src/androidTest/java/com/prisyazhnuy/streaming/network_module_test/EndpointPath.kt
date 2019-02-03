package com.prisyazhnuy.streaming.network_module_test

import com.prisyazhnuy.streaming.network.V1

enum class EndpointPath(val path: String, val method: String) {
    ACCOUNT("/$V1/account", "POST"),
    LOGIN("/$V1/account/login", "POST"),
    LOGOUT("/$V1/account/logout", "POST"),
    REFRESH_TOKEN("/$V1/account/refreshToken", "POST")
}
