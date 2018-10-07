package com.danxionglei.fulldisplaybackgesture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author damonlei
 */
open class FullDisplayActivity : AppCompatActivity() {

    private lateinit var fullDisplayFrameLayout: FullDisplayFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullDisplayFrameLayout = FullDisplayFrameLayout(applicationContext)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        fullDisplayFrameLayout.attachToActivity(this)
    }

    fun getFullDisplayFrameLayout(): FullDisplayFrameLayout {
        return fullDisplayFrameLayout
    }

}