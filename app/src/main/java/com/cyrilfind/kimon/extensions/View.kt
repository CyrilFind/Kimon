package com.cyrilfind.kimon.extensions

import android.graphics.drawable.Drawable
import android.view.View
import kotlinx.coroutines.delay

val HIGHLIGHT_INTERVAL: Long
    get() = 700L

var View.nullableBackground: Drawable?
    get() = background
    set(value) { background = value }

suspend inline fun View.flash() {
    highlight()
    delay(HIGHLIGHT_INTERVAL)
    unhighlight()
}

fun View.unhighlight() {
    nullableBackground?.alpha = 255
}

fun View.highlight() {
    nullableBackground?.alpha = 50
}