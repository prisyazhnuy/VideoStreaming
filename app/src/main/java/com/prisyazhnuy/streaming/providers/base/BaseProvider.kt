package com.prisyazhnuy.streaming.providers.base

import com.prisyazhnuy.streaming.models.Model
import com.prisyazhnuy.streaming.repositories.Repository


abstract class BaseProvider<M : Model<*>, NetworkModule, Repo : Repository<M>>
    : BaseOnlineProvider<M, NetworkModule>() {

    val repository: Repo = this.initRepository()

    protected abstract fun initRepository(): Repo
}