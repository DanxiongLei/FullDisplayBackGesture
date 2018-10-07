package com.danxionglei.fulldisplaybackgesturedemo;

import android.os.Bundle;

import com.danxionglei.fulldisplaybackgesture.FullDisplayActivity;
import com.danxionglei.fulldisplaybackgesture.FullDisplayFrameLayout;
import com.danxionglei.fulldisplaybackgesture.IFullDisplayLayout;
import com.danxionglei.fulldisplaybackgesture.back.BackGestureDrawableConfig;
import com.danxionglei.fulldisplaybackgesture.util.Dp;

public class MainActivity extends FullDisplayActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FullDisplayFrameLayout fd = getFullDisplayFrameLayout();

        // back gesture config
        fd.setBackGestureEnable(true);
        fd.setBackGestureDragVertical(true);
        fd.setBackGestureDragThreshold(new Dp(100));

        // callback
        fd.setBackGestureCallback(new IFullDisplayLayout.BackGestureCallback() {
            @Override
            public void onBackGestureDone() {
                onBackPressed();
            }
        });

        // drawable config
        BackGestureDrawableConfig config = new BackGestureDrawableConfig();
        config.setBackgroundAlpha(.775f);
        config.setMaxWidth(new Dp(28.5f));
        config.setMaxHeight(new Dp(250f));
        fd.setBackGestureDrawableConfig(config);
    }
}
