package com.prisyazhnuy.streaming.extensions

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.prisyazhnuy.streaming.NPApp

fun getStringApp(resId: Int) = NPApp.instance.getString(resId)

fun getAppString(@StringRes stringId: Int, vararg formatArgs: Any) =
        NPApp.instance.getString(stringId, *formatArgs)

fun getStringArray(@ArrayRes id: Int) = NPApp.instance.resources.getStringArray(id)
