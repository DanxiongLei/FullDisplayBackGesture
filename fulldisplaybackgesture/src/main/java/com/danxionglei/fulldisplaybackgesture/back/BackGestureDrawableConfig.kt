package com.danxionglei.fulldisplaybackgesture.back

import com.danxionglei.fulldisplaybackgesture.util.Dp
import com.danxionglei.fulldisplaybackgesture.util.dp
import kotlin.math.roundToInt

/**
 * @author damonlei
 */
class BackGestureDrawableConfig {
    var backgroundAlpha = 0.775f
        set(value) {
            if (value !in 0..1) {
                throw IllegalArgumentException("backgroundAlpha must in [0, 1]")
            }
            field = value
        }

    var maxWidth = 28.5f.dp()
        set(value) {
            if (value < Dp(0f)) {
                throw IllegalArgumentException("maxWidth must >= 0")
            }
            field = value
        }


    var maxHeight = 250.dp()
        set(value) {
            if (value < Dp(0f)) {
                throw IllegalArgumentException("maxHeight must >= 0")
            }
            field = value
        }

    var indicatorAlpha = 0.85f
        set(value) {
            if (value !in 0..1) {
                throw IllegalArgumentException("indicatorAlpha must in [0, 1]")
            }
            field = value
        }


    var indicatorWidth = 4.5.dp()

    var indicatorHeight = 11.dp()

    var indicatorStrokeWidth = 2.3.dp()

    var indicatorOffset = (-1).dp()

    var ctrl1Ratio = 1.7f

    var ctrl2Ratio = 2.95f

    internal val ctrl1 by lazy {
        curveHeight / ctrl1Ratio
    }

    internal val ctrl2 by lazy {
        curveHeight / ctrl2Ratio
    }

    internal val curveHeight by lazy {
        maxHeight.toPxValue() / 2f
    }

    internal val backgroundAlpha255 get() = (backgroundAlpha * 255).roundToInt()

    internal val indicatorAlpha255 get() = (indicatorAlpha * 255).roundToInt()

}