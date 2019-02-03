package com.prisyazhnuy.streaming.ui.base

import android.text.Editable
import android.text.TextWatcher

open class BaseTextWatcher: TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        // override to implement
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // override to implement
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // override to implement
    }
}