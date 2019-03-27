package com.cyrilfind.kimon.extensions

import android.graphics.drawable.Drawable
import android.view.View
import kotlinx.coroutines.delay

val HIGHLIGHT_INTERVAL: Long
    get() = 700L

fun View.nullableBackground(): Drawable? = background

suspend inline fun View.flash() {
    highlight()
    delay(HIGHLIGHT_INTERVAL)
    unhighlight()
}

fun View.unhighlight() {
    nullableBackground()?.let {
        it.alpha = 255
    } ?: run {
        visibility = View.VISIBLE
    }
}


fun View.highlight() {
    val nullableBackground = nullableBackground()
    if (nullableBackground != null) {
        nullableBackground.alpha = 50
    } else {
        visibility = View.INVISIBLE
    }
}
