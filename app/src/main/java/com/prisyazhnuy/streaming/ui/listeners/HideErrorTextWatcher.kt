package com.prisyazhnuy.streaming.ui.listeners

import com.google.android.material.textfield.TextInputLayout
import com.prisyazhnuy.streaming.ui.base.BaseTextWatcher
import com.prisyazhnuy.streaming.utils.EMPTY_STRING
import java.lang.ref.WeakReference

class HideErrorTextWatcher(til: TextInputLayout) : BaseTextWatcher() {

    private val ref = WeakReference<TextInputLayout>(til)

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        ref.get()?.run {
            error = EMPTY_STRING
            isErrorEnabled = false
        }
    }
}