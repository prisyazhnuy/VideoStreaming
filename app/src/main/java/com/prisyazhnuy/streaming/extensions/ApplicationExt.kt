package com.prisyazhnuy.streaming.extensions

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.prisyazhnuy.streaming.VSApp

fun getStringApp(resId: Int) = VSApp.instance.getString(resId)

fun getAppString(@StringRes stringId: Int, vararg formatArgs: Any) =
        VSApp.instance.getString(stringId, *formatArgs)

fun getStringArray(@ArrayRes id: Int) = VSApp.instance.resources.getStringArray(id)
