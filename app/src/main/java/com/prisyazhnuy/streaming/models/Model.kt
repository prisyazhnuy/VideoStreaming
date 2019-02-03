package com.prisyazhnuy.streaming.models


interface Model<T> : KParcelable {

    var id: T?
}