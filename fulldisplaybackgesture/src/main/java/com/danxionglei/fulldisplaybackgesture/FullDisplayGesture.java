package com.danxionglei.fulldisplaybackgesture;

import android.app.Activity;

/**
 * @author damonlei
 */
public class FullDisplayGesture {

    private static final String TAG = "FullDisplayGesture";

    public static void enable(Activity activity) {
        activity.getWindow().getDecorView();
    }

    public static void disable(Activity activity) {

    }
}
