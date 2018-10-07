package com.danxionglei.fulldisplaybackgesture.util

import android.graphics.PointF
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.ViewConfiguration
import android.view.ViewGroup
import kotlin.math.abs

/**
 * @author damonlei
 */
class EdgeDragHelper(val view: ViewGroup, var callback: Callback?) {

    interface Callback {
        fun onDrag(edge: Edge, x: Float, y: Float, downPoint: PointF)
        fun onDragFinish(edge: Edge, x: Float, y: Float, downPoint: PointF)
        fun onDragCancel(edge: Edge, x: Float, y: Float, downPoint: PointF)
    }

    enum class Edge(internal val bit: Int) {
        LEFT(EDGE_LEFT), RIGHT(EDGE_RIGHT), TOP(EDGE_TOP), BOTTOM(EDGE_BOTTOM);
    }

    class EdgeFlags {
        private var bits: Int = 0
        fun set(edge: Edge, enable: Boolean) {
            if (get(edge) == enable) {
                return
            }
            bits = bits xor edge.bit
        }

        fun get(edge: Edge?) = bits and (edge?.bit ?: 0) != 0

        fun clear() {
            bits = 0
        }

        fun isClear() = (bits == 0)
    }

    private val trackingEdges = EdgeFlags()

    var touchSlop: Int

    var edgeSize: Int

    init {
        val vc = ViewConfiguration.get(view.context)
        touchSlop = vc.scaledTouchSlop
        edgeSize = vc.scaledEdgeSlop
    }

    fun setTrackingEdges(vararg edges: Edge) {
        for (it in edges) {
            trackingEdges.set(it, true)
        }
    }

    private var downPoint: PointF? = null
    private var lastPoint: PointF? = null

    fun shouldInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == ACTION_DOWN && trackingEdges.get(checkEdge(PointF(ev.x, ev.y)))) {
            downPoint = PointF(ev.x, ev.y)
            lastPoint = PointF(ev.x, ev.y)
            return true
        }
        return false
    }

    fun processTouchEvent(ev: MotionEvent) {
        when (ev.action) {
            ACTION_DOWN -> {}
            ACTION_MOVE -> {
                if (lastPoint == null || downPoint == null) {
                    return
                }
                val lastPoint: PointF = this.lastPoint!!
                val downPoint: PointF = this.downPoint!!
                if (!checkTouchSlop(ev.x - lastPoint.x, ev.y - lastPoint.y)) {
                    return
                }
                callback?.onDrag(checkEdge(downPoint)!!, ev.x, ev.y, downPoint)
            }
            ACTION_UP -> {
                if (downPoint == null) {
                    return
                }
                val downPoint: PointF = this.downPoint!!
                callback?.onDragFinish(checkEdge(downPoint)!!, ev.x, ev.y, downPoint)
                this.downPoint = null
                this.lastPoint = null
            }
            ACTION_CANCEL -> {
                if (downPoint == null) {
                    return
                }
                val downPoint: PointF = this.downPoint!!
                callback?.onDragCancel(checkEdge(downPoint)!!, ev.x, ev.y, downPoint)
                this.downPoint = null
                this.lastPoint = null
            }
            else -> {
            }
        }
    }

    private fun calculateDelta(ev: MotionEvent, downPoint: PointF): Px {
        return when (checkEdge(downPoint)!!) {
            Edge.LEFT, Edge.RIGHT -> Px(abs(ev.x - downPoint.x))
            Edge.TOP, Edge.BOTTOM -> Px(abs(ev.y - downPoint.y))
        }
    }

    private fun checkTouchSlop(dx: Float, dy: Float) = abs(dx) > touchSlop || abs(dy) > touchSlop

    private fun checkEdge(p: PointF): Edge? {
        return when {
            p.x < view.left + edgeSize -> Edge.LEFT
            p.y < view.top + edgeSize -> Edge.TOP
            p.x > view.right - edgeSize -> Edge.RIGHT
            p.y > view.bottom + edgeSize -> Edge.BOTTOM
            else -> null
        }
    }
}