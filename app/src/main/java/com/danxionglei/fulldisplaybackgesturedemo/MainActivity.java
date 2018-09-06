package com.danxionglei.fulldisplaybackgesturedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.danxionglei.fulldisplaybackgesture.BackGestureDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackGestureDrawable drawable = new BackGestureDrawable(getApplicationContext());
        drawable.setRatio(1);
        View v = findViewById(R.id.central_view);
        v.setBackground(drawable);
    }
}
