package com.danxionglei.fulldisplaybackgesture;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author damonlei
 */
public class FullDisplayFrameLayout extends FrameLayout {

    private static final String TAG = "FullDisplayFrameLayout";


    private BackGestureDrawable backGestureDrawable;

    public FullDisplayFrameLayout(Context context) {
        super(context);
        init();
    }

    public FullDisplayFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public FullDisplayFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backGestureDrawable = new BackGestureDrawable(getContext());
        backGestureDrawable.setRatio(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogDelegate.Log.i(TAG, "FullDisplayFrameLayout.onInterceptTouchEvent x");
        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }
        LogDelegate.Log.i(TAG, "FullDisplayFrameLayout.onInterceptTouchEvent a");
        if (!shouldIntercept(ev)) {
            return false;
        }

        LogDelegate.Log.i(TAG, "FullDisplayFrameLayout.onInterceptTouchEvent [%b]", ev.getAction() == MotionEvent.ACTION_DOWN);
        return true;
    }

    private float downPointPx;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            backGestureDrawable.setPivot((int) ev.getX(), (int) ev.getY());
            downPointPx = ev.getX();
            return true;
        } else {
            LogDelegate.Log.i(TAG, "MOVE");
            backGestureDrawable.setMovement((int) (ev.getX() - downPointPx));
            invalidate();
            return true;
        }
    }

    private boolean shouldIntercept(MotionEvent event) {
        if (event == null) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        backGestureDrawable.draw(canvas);
    }
}
