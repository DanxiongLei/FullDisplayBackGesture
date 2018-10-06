package com.danxionglei.fulldisplaybackgesture

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawable
import com.danxionglei.fulldisplaybackgesture.util.EdgeDragHelper
import com.danxionglei.fulldisplaybackgesture.util.Px
import com.danxionglei.fulldisplaybackgesture.util.dp
import kotlin.math.abs

/**
 * @author damonlei
 */
class FullDisplayFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        setWillNotDraw(false)
    }

    private val backGestureDrawable = BackGestureDrawable()

    private val dragHelper = EdgeDragHelper(this, object : EdgeDragHelper.Callback {

        override fun onDrag(delta: Px, edge: EdgeDragHelper.Edge, downPoint: PointF) {
            backGestureDrawable.direction = when (edge) {
                EdgeDragHelper.Edge.LEFT -> BackGestureDrawable.Direction.FROM_LEFT_TO_RIGHT
                EdgeDragHelper.Edge.RIGHT -> BackGestureDrawable.Direction.FROM_RIGHT_TO_LEFT
                else -> return
            }

            val pivotX = when (edge) {
                EdgeDragHelper.Edge.LEFT -> left.toFloat()
                EdgeDragHelper.Edge.RIGHT -> right.toFloat()
                else -> return
            }
            backGestureDrawable.setPivot(pivotX, downPoint.y)
            backGestureDrawable.currentRatio = abs(delta.value) / 100.dp().toPxValue()
            invalidate()
        }

        override fun onDragFinish(delta: Px, edge: EdgeDragHelper.Edge, downPoint: PointF) {
            animateReset()
        }

        override fun onDragCancel(delta: Px, edge: EdgeDragHelper.Edge, downPoint: PointF) {
            animateReset()
        }
    })

    init {
        dragHelper.setTrackingEdges(EdgeDragHelper.Edge.LEFT, EdgeDragHelper.Edge.RIGHT)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
//
//        return when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                backGestureDrawable.setPivot(event.x, event.y)
//                downPointPx = event.x
//                true
//            }
//            else -> {
//                val offset = event.x - downPointPx
//                backGestureDrawable.direction = when {
//                    offset > 0 -> FROM_LEFT_TO_RIGHT
//                    offset < 0 -> FROM_RIGHT_TO_LEFT
//                    else -> FROM_LEFT_TO_RIGHT
//                }
//
//                backGestureDrawable.currentRatio = abs(offset) / backGestureDrawable.config.maxWidth.toPxValue()
//                invalidate()
//                true
//            }
//        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (backGestureDrawable.currentRatio != 0f) {
            backGestureDrawable.draw(canvas)
        }
    }

    fun animateReset() {
        backGestureDrawable.currentRatio = 0f
        invalidate()
    }
}