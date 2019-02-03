package com.prisyazhnuy.streaming.network.api.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshTokenRequest(@JsonProperty("refreshToken")
                               val refreshToken: String?)
