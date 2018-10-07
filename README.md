# FullDisplayBackGesture

仿照 MIUI 全面屏后退手势造的轮子，便于在非 MIUI 机器上保持体验一致，respect

![](screenshot/screenshot.png)


## Usage

直接继承自 FullDisplayActivity ( AppCompatActivity ) 即可

也可以将 FullDisplayLayout ( FrameLayout ) 作为根 View 使用

```java
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
```

## TODO

- [x] ~~纵向跟手~~
- [ ] 配置仅半屏响应
- [ ] 弹性动画
