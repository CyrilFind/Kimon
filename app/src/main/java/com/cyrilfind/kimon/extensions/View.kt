package com.cyrilfind.kimon.extensions

import android.view.View
import kotlinx.coroutines.delay

val HIGHLIGHT_INTERVAL: Long
    get() = 700L

suspend inline fun View.flash() {
    highlight()
    delay(HIGHLIGHT_INTERVAL)
    unhighlight()
}

fun View.unhighlight() {
    background?.alpha = 255
}

fun View.highlight() {
    background?.alpha = 50
}