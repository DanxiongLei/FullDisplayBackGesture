package com.danxionglei.fulldisplaybackgesture

import android.app.Activity
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawableConfig
import com.danxionglei.fulldisplaybackgesture.util.Dp

/**
 * @author damonlei
 */
interface IFullDisplayLayout {

    var backGestureEnable: Boolean

    interface BackGestureCallback {
        fun onBackGestureDone()
    }

    var backGestureCallback: BackGestureCallback?

    var backGestureDragVertical: Boolean

    var backGestureDragThreshold: Dp

    var backGestureDrawableConfig: BackGestureDrawableConfig

    fun attachToActivity(activity: Activity)
}