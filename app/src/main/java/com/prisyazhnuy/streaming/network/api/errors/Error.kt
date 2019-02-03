package com.prisyazhnuy.streaming.network.api.errors

import com.fasterxml.jackson.annotation.JsonProperty
import com.prisyazhnuy.streaming.utils.NOTHING

data class Error(@JsonProperty("code")
                 val code: Int? = NOTHING,
                 @JsonProperty("key")
                 var key: String? = NOTHING,
                 @JsonProperty("message")
                 var message: String? = NOTHING)


