package com.danxionglei.fulldisplaybackgesture;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * @author damonlei
 */
public class FullDisplayGesture {

    private static final String TAG = "FullDisplayGesture";

    public static void enable(@NonNull Activity activity) {
        activity.getWindow().getDecorView();
    }

    public static void disable(@NonNull Activity activity) {

    }
}
