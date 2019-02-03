package com.prisyazhnuy.streaming.network.api.retrofit

import com.prisyazhnuy.streaming.network.V1
import com.prisyazhnuy.streaming.network.api.beans.Response
import com.prisyazhnuy.streaming.network.api.beans.SessionBean
import com.prisyazhnuy.streaming.network.api.beans.UserSessionBean
import com.prisyazhnuy.streaming.network.api.requests.LoginRequest
import com.prisyazhnuy.streaming.network.api.requests.RefreshTokenRequest
import com.prisyazhnuy.streaming.network.api.requests.RegisterRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {
    // TODO change links for methods
    @POST("$V1/account")
    fun register(@Body request: RegisterRequest): Single<Response<UserSessionBean>>

    @POST("$V1/account/login")
    fun login(@Body request: LoginRequest): Single<Response<UserSessionBean>>

    @POST("$V1/account/logout")
    fun logout(): Single<Response<Unit>>

    @POST("$V1/account/refreshToken")
    fun refreshToken(@Body request: RefreshTokenRequest): Single<Response<SessionBean>>
}
