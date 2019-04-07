package com.prisyazhnuy.streaming.utils

import android.view.SurfaceHolder

open class SimpleSurfaceCallback : SurfaceHolder.Callback {
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) = Unit

    override fun surfaceDestroyed(holder: SurfaceHolder?) = Unit

    override fun surfaceCreated(holder: SurfaceHolder?) = Unit
}