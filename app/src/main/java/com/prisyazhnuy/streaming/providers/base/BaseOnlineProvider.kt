package com.prisyazhnuy.streaming.providers.base

import com.prisyazhnuy.streaming.models.Model


abstract class BaseOnlineProvider<M : Model<*>, NetworkModule> : Provider<M> {

    val networkModule: NetworkModule = this.initNetworkModule()

    protected abstract fun initNetworkModule(): NetworkModule
}