package com.danxionglei.fulldisplaybackgesture.back

import android.graphics.*
import android.graphics.drawable.Drawable
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawable.Direction.FROM_LEFT_TO_RIGHT

/**
 * @author damonlei
 */
class BackGestureDrawable(val config: BackGestureConfig = BackGestureConfig()) : Drawable() {

    enum class Direction {
        FROM_LEFT_TO_RIGHT, FROM_RIGHT_TO_LEFT
    }

    val pivot = PointF()

    fun setPivot(x: Float, y: Float) {
        pivot.x = x
        pivot.y = y
    }

    var direction = FROM_LEFT_TO_RIGHT

    var currentRatio: Float = 0f
        set(value) {
            field = when {
                value > 1 -> 1f
                value < 0 -> 0f
                else -> value
            }
        }

    internal val currentWidth
        get() = currentRatio * config.maxWidth.toPxValue()

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        backgroundPaint.color = Color.BLACK
        backgroundPaint.alpha = config.backgroundAlpha255
        backgroundPaint.style = Paint.Style.FILL
    }

    private val indicatorPaint = Paint()

    init {
        indicatorPaint.alpha = config.indicatorAlpha255
    }

    private val indicatorBitmap = createIndicatorBitmap(config)

    private val backgroundPath = Path()

    private val cacheRect = RectF()

    override fun draw(canvas: Canvas) {
        putCurve(backgroundPath, this, config)
        canvas.drawPath(backgroundPath, backgroundPaint)

        putIndicator(cacheRect, indicatorBitmap, this, config)
        canvas.drawBitmap(indicatorBitmap, null, cacheRect, indicatorPaint)
    }

    override fun setAlpha(p0: Int) {
        backgroundPaint.alpha = (p0 * config.backgroundAlpha).toInt()
        invalidateSelf()
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(p0: ColorFilter?) {
        backgroundPaint.colorFilter = p0
        invalidateSelf()
    }

}
