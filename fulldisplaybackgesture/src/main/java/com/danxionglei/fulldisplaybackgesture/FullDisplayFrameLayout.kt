package com.danxionglei.fulldisplaybackgesture

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawable
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawableConfig
import com.danxionglei.fulldisplaybackgesture.util.Dp
import com.danxionglei.fulldisplaybackgesture.util.EdgeDragHelper
import com.danxionglei.fulldisplaybackgesture.util.dp
import kotlin.math.abs

/**
 * @author damonlei
 */
class FullDisplayFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IFullDisplayLayout {

    override var backGestureEnable: Boolean = true

    override var backGestureCallback: IFullDisplayLayout.BackGestureCallback? = null

    override var backGestureDragVertical: Boolean = true

    override var backGestureDragThreshold: Dp = 100.dp()
        set(value) {
            if (value < 0) {
                throw IllegalArgumentException("backGestureDragThreshold must >= 0")
            }
            field = value
        }

    override var backGestureDrawableConfig: BackGestureDrawableConfig = BackGestureDrawableConfig()
        set(value) {
            post { backGestureDrawableNullable = BackGestureDrawable(value) }
            field = value
        }

    override fun attachToActivity(activity: Activity) {
        val decor = activity.window.decorView as ViewGroup
        val decorChild = decor.getChildAt(0) as ViewGroup
        decor.removeView(decorChild)
        addView(decorChild)
        decor.addView(this)
    }

    private var backGestureDrawableNullable: BackGestureDrawable? = null
    private val backGestureDrawable: BackGestureDrawable
        get() {
            if (backGestureDrawableNullable == null) {
                backGestureDrawableNullable = BackGestureDrawable(backGestureDrawableConfig)
            }
            return backGestureDrawableNullable!!
        }

    private val dragHelper = EdgeDragHelper(this, object : EdgeDragHelper.Callback {
        override fun onDrag(edge: EdgeDragHelper.Edge, x: Float, y: Float, downPoint: PointF) {
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

            val pivotY = if (backGestureDragVertical) y else downPoint.y
            backGestureDrawable.setPivot(pivotX, pivotY)
            backGestureDrawable.currentRatio = abs(x - downPoint.x) / backGestureDragThreshold.toPxValue()
            invalidate()
        }

        override fun onDragFinish(edge: EdgeDragHelper.Edge, x: Float, y: Float, downPoint: PointF) {
            animateReset()
            if (abs(x - downPoint.x) >= backGestureDragThreshold.toPxValue()) {
                backGestureCallback?.onBackGestureDone()
                return
            }
        }

        override fun onDragCancel(edge: EdgeDragHelper.Edge, x: Float, y: Float, downPoint: PointF) {
            animateReset()
        }
    })

    init {
        dragHelper.setTrackingEdges(EdgeDragHelper.Edge.LEFT, EdgeDragHelper.Edge.RIGHT)
    }

    init {
        setWillNotDraw(false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!backGestureEnable) {
            return false
        }
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!backGestureEnable) {
            return false
        }
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (!backGestureEnable) {
            return
        }
        if (backGestureDrawable.currentRatio != 0f) {
            backGestureDrawable.draw(canvas)
        }
    }

    private fun animateReset() {
        backGestureDrawable.currentRatio = 0f
        invalidate()
    }

}