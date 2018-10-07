package com.danxionglei.fulldisplaybackgesture.back

import android.graphics.*
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawable.Direction.FROM_LEFT_TO_RIGHT
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawable.Direction.FROM_RIGHT_TO_LEFT

/**
 * @author damonlei
 */
internal fun putCurve(path: Path, state: BackGestureDrawable, config: BackGestureDrawableConfig) {
    path.reset()
    path.moveTo(state.pivot.x, state.pivot.y - config.curveHeight)

    val revertWidth = state.currentWidth * when (state.direction) {
        FROM_LEFT_TO_RIGHT -> 1
        FROM_RIGHT_TO_LEFT -> -1
    }

    path.rCubicTo(0f, config.ctrl1, revertWidth, config.curveHeight - config.ctrl2, revertWidth, config.curveHeight)
    path.rCubicTo(0f, config.ctrl2, -revertWidth, config.curveHeight - config.ctrl1, -revertWidth, config.curveHeight)

    path.close()
}

internal fun putIndicator(rect: RectF, indicator: Bitmap, state: BackGestureDrawable, @Suppress("UNUSED_PARAMETER") config: BackGestureDrawableConfig) {
    rect.top = state.pivot.y - indicator.width / 2f
    rect.bottom = rect.top + indicator.height

    if (state.direction == FROM_LEFT_TO_RIGHT) {
        rect.left = state.pivot.x + (state.currentWidth - indicator.width) / 2f
        rect.right = rect.left + indicator.width
    } else {
        rect.right = state.pivot.x - (state.currentWidth - indicator.width) / 2f
        rect.left = rect.right - indicator.width
    }
}

internal fun createIndicatorBitmap(config: BackGestureDrawableConfig): Bitmap {

    val indicatorWidthPx = config.indicatorWidth.toPx().value
    val indicatorHeightPx = config.indicatorHeight.toPx().value

    val indicator = Bitmap.createBitmap(
            config.indicatorWidth.toPx().toInt() + (config.indicatorStrokeWidth.toPx().toInt() + 1) * 2,
            config.indicatorHeight.toPx().toInt() + (config.indicatorStrokeWidth.toPx().toInt() + 1) * 2,
            Bitmap.Config.ARGB_8888)

    val canvas = Canvas(indicator)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.strokeWidth = config.indicatorStrokeWidth.toPx().value
    paint.style = Paint.Style.STROKE
    paint.color = Color.WHITE
    paint.strokeJoin = Paint.Join.ROUND
    paint.strokeCap = Paint.Cap.ROUND

    val path = Path()
    path.moveTo((indicator.width + indicatorWidthPx) / 2f, (indicator.height - indicatorHeightPx) / 2f)
    path.lineTo((indicator.width - indicatorWidthPx) / 2f, indicator.height / 2f)
    path.lineTo((indicator.width + indicatorWidthPx) / 2f, (indicator.height + indicatorHeightPx) / 2f)
    path.offset(config.indicatorOffset.toPx().value, 0f)

    canvas.drawPath(path, paint)
    return indicator
}


