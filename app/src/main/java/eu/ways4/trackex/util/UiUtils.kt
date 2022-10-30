package eu.ways4.trackex.util

import android.os.Handler

fun runOnUiThread(delayMillis: Long = 0, block: () -> Unit) {
    Handler().postDelayed(block, delayMillis)
}