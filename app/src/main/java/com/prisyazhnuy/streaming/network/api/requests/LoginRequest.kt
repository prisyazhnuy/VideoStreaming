package com.prisyazhnuy.streaming.network.api.requests

import com.fasterxml.jackson.annotation.JsonProperty


data class LoginRequest(@field:JsonProperty("email")
                        val email: String,
                        @field:JsonProperty("password")
                        val password: String,
                        @field:JsonProperty("lifeTime")
                        val lifeTime: Long? = null)
