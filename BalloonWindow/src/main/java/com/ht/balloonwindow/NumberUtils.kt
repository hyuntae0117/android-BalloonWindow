package com.ht.balloonwindow

import android.content.res.Resources
import java.util.*


class NumberUtils {
    companion object {
        private val random = Random(System.currentTimeMillis())

        fun rand(from: Int, to: Int): Int {
            return random.nextInt(to - from) + from
        }
    }
}


fun Int.toDp():Float {
    return this / Resources.getSystem().displayMetrics.density
}

fun Float.toDp(): Float {
    return this / Resources.getSystem().displayMetrics.density
}

fun Int.toPx(): Int {
    return Math.round(this * Resources.getSystem().displayMetrics.density)
}

fun Float.toPx(): Float {
    return this * Resources.getSystem().displayMetrics.density
}

fun Int.spToPx(): Float {
    return this * Resources.getSystem().displayMetrics.scaledDensity
}


