package com.danxionglei.fulldisplaybackgesture.util

import android.content.res.Resources
import kotlin.math.round
import kotlin.math.roundToInt

/**
 * @author damonlei
 */
class Dp(var value: Float) {

    constructor(value: Number) : this(value.toFloat())

    fun toPx(): Px {
        return Px(round(value * Resources.getSystem().displayMetrics.density))
    }

    fun toPxValue(): Float {
        return toPx().value
    }

    operator fun compareTo(another: Dp): Int {
        return when {
            value == another.value -> 0
            value > another.value -> 1
            value < another.value -> -1
            else -> throw IllegalStateException()
        }
    }

    operator fun compareTo(another: Px): Int {
        return compareTo(another.toDp())
    }

    operator fun compareTo(another: Number): Int {
        return compareTo(Dp(another))
    }

    operator fun unaryMinus() {
        value *= -1
    }

}

fun Number.dp(): Dp {
    return Dp(this)
}

class Px(var value: Float) {

    constructor(value: Number) : this(value.toFloat())

    fun toDp(): Dp {
        return Dp(round(value / Resources.getSystem().displayMetrics.density))
    }

    fun toDpValue(): Float {
        return toDp().value
    }

    operator fun compareTo(another: Px): Int {
        return when {
            value == another.value -> 0
            value > another.value -> 1
            value < another.value -> -1
            else -> throw IllegalStateException()
        }
    }

    operator fun compareTo(another: Dp): Int {
        return compareTo(another.toPx())
    }

    operator fun compareTo(another: Number): Int {
        return compareTo(Px(another))
    }

    fun toInt(): Int {
        return value.roundToInt()
    }

    operator fun unaryMinus() {
        value *= -1
    }
}

fun Number.px(): Px {
    return Px(this)
}

