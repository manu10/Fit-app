package com.manugarcia010.timeselectorview.extensions

import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText

fun AppCompatEditText.updateText(text: String) {
    val focussed = hasFocus()
    if (focussed) {
        clearFocus()
    }
    setText(text, TextView.BufferType.EDITABLE)
    if (focussed) {
        requestFocus()
    }
}