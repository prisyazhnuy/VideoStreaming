package com.prisyazhnuy.streaming.network.api.requests

import com.fasterxml.jackson.annotation.JsonProperty


data class RegisterRequest(@field:JsonProperty("email")
                           val email: String,
                           @field:JsonProperty("firstName")
                           val firstName: String,
                           @field:JsonProperty("lastName")
                           val lastName: String,
                           @field:JsonProperty("password")
                           val password: String,
                           @field:JsonProperty("confirmPassword")
                           val confirmPassword: String)