package com.prisyazhnuy.streaming.utils.validation

import com.prisyazhnuy.streaming.utils.EMPTY_STRING

data class ValidationWrapper(val email: String = EMPTY_STRING,
                             val password: String = EMPTY_STRING,
                             val confirmPassword: String = EMPTY_STRING,
                             val firstName: String = EMPTY_STRING,
                             val lastName: String = EMPTY_STRING)