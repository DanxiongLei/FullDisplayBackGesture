package com.danxionglei.fulldisplaybackgesture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

/**
 * @author damonlei
 */
open class FullDisplayActivity : AppCompatActivity() {

    private lateinit var fullDisplayFrameLayout: FullDisplayFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullDisplayFrameLayout = FullDisplayFrameLayout(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val decor = window.decorView as ViewGroup
        val decorChild = decor.getChildAt(0) as ViewGroup
        decor.removeView(decorChild)
        fullDisplayFrameLayout.addView(decorChild)
        decor.addView(fullDisplayFrameLayout)
    }

    fun isBackGestureEnabled() = fullDisplayFrameLayout.isBackGestureEnabled

    fun setBackGestureEnabled(enable: Boolean) {
        fullDisplayFrameLayout.isBackGestureEnabled = enable
    }


}