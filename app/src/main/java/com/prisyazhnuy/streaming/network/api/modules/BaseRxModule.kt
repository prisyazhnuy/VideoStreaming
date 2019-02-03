package com.prisyazhnuy.streaming.network.api.modules

import com.prisyazhnuy.streaming.models.converters.Converter


abstract class BaseRxModule<T, NetworkModel, M>(val api: T, val converter: Converter<NetworkModel, M>)