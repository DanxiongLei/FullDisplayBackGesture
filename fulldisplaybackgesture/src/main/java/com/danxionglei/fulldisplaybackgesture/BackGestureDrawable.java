package com.danxionglei.fulldisplaybackgesture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author damonlei
 */
public class BackGestureDrawable extends Drawable {

    private static final String TAG = "MicroMsg.BackDrawable";

    private Bitmap indicator;
    private float indicatorAlpha = .85f;
    private Paint indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        indicatorPaint.setColor(Color.WHITE);
        indicatorPaint.setAlpha((int) (indicatorAlpha * 255));
    }

    // [0, 1]
    private float backgroundAlpha = 0.775f;

    // [0, 1]
    private float currentRatio = 0f;

    private float indicatorRatio = 0.5f;

    private int maxWidthPx;
    private int maxHeightPx;

    private int pivotXPx;
    private int pivotYPx;

    private final float DISPLAY_DENSITY;

    private Path path = new Path();

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        paint.setColor(Color.BLACK);
        paint.setAlpha((int) (backgroundAlpha * 255));
        paint.setStyle(Paint.Style.FILL);
    }

    public enum Direction {
        FROM_LEFT_TO_RIGHT, FROM_RIGHT_TO_LEFT
    }

    private Direction direction = Direction.FROM_LEFT_TO_RIGHT;

    public BackGestureDrawable(Context context) {
        DISPLAY_DENSITY = context.getResources().getDisplayMetrics().density;
        maxWidthPx = fromDpToPixel(28.5f);
        maxHeightPx = fromDpToPixel(250);
        pivotXPx = 0;
        pivotYPx = maxHeightPx / 2;
        indicator = createIndicatorBitmap();
    }

    private Bitmap createIndicatorBitmap() {
        int indicatorWidthPx = fromDpToPixel(4.5f);
        int indicatorHeightPx = fromDpToPixel(11f);

        Bitmap indicator = Bitmap.createBitmap(fromDpToPixel(13), fromDpToPixel(20), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(indicator);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(fromDpToPixel(2.3f));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path path = new Path();
        path.moveTo((indicator.getWidth() + indicatorWidthPx) / 2f, (indicator.getHeight() - indicatorHeightPx) / 2f);
        path.lineTo((indicator.getWidth() - indicatorWidthPx) / 2f, indicator.getHeight() / 2f);
        path.lineTo((indicator.getWidth() + indicatorWidthPx) / 2f, (indicator.getHeight() + indicatorHeightPx) / 2f);
        path.offset(-fromDpToPixel(1f), 0);

        canvas.drawPath(path, paint);

//        paint.setColor(Color.RED);
//        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        return indicator;
    }

    public void setDirection(@NonNull Direction direction) {
        if (currentRatio != 0) {
            LogDelegate.Log.e(TAG, "Direction can not be set before currentRation == 0 ! currentRatio = [%f] direction = [%s]", currentRatio, direction.name());
            return;
        }
        this.direction = direction;
    }

    public void setIndicatorAlpha(float alpha) {
        if (alpha < 0 || alpha > 1) {
            return;
        }
        indicatorAlpha = alpha;
        invalidateSelf();
    }

    public void setBackgroundColor(int color) {
        paint.setColor(color);
        backgroundAlpha = Color.alpha(color) / 255f;
        invalidateSelf();
    }

    public void setBackgroundAlpha(float alpha) {
        if (alpha < 0 || alpha > 1) {
            return;
        }
        paint.setAlpha((int) (alpha * 255));
        backgroundAlpha = alpha;
        invalidateSelf();
    }

    public void setMaxWidthDp(float widthDp) {
        int widthPx = fromDpToPixel(widthDp);
        if (widthPx < 0) {
            return;
        }
        maxWidthPx = widthPx;
        invalidateSelf();
    }

    public void setMaxHeightDp(float heightDp) {
        int heightPx = fromDpToPixel(heightDp);
        if (heightPx < 0) {
            return;
        }
        maxHeightPx = heightPx;
        invalidateSelf();
    }

    public void setRatio(float ratio) {
        if (ratio < 0) {
            return;
        }
        currentRatio = Math.min(ratio, 1);
        invalidateSelf();
    }

    public void setMovement(int px) {
        setRatio(px / (float) maxWidthPx);
    }

    public void setPivot(int xPx, int yPx) {
        pivotXPx = xPx;
        pivotYPx = yPx;
    }

    public float getRatio() {
        return currentRatio;
    }

    public void setIndicatorRatio(float indicatorRatio) {
        this.indicatorRatio = indicatorRatio;
        invalidateSelf();
    }

    // |\
    // | \
    // |up|
    // |--|
    // |Dn|
    // | /
    // |/
    @Override
    public void draw(@NonNull Canvas canvas) {
        float ctrl1 = maxHeightPx / 2f / 1.7f;
        float ctrl2 = maxHeightPx / 2f / 2.95f;

        float currentWidth = currentRatio * maxWidthPx;
        float curveHeight = maxHeightPx / 2f;

        path.moveTo(pivotXPx, pivotYPx - curveHeight);
        if (direction == Direction.FROM_LEFT_TO_RIGHT) {
            createCurve(path, ctrl1, ctrl2, currentWidth, curveHeight, Direction.FROM_LEFT_TO_RIGHT);
            createCurve(path, ctrl2, ctrl1, currentWidth, curveHeight, Direction.FROM_RIGHT_TO_LEFT);
        } else {
            createCurve(path, ctrl1, ctrl2, currentWidth, curveHeight, Direction.FROM_RIGHT_TO_LEFT);
            createCurve(path, ctrl2, ctrl1, currentWidth, curveHeight, Direction.FROM_LEFT_TO_RIGHT);
        }
        path.close();

        canvas.drawPath(path, paint);

        drawIndicator(canvas, currentWidth);
    }

    private void drawIndicator(Canvas canvas, float width) {
        int indicatorWidthPx = indicator.getWidth();
        int indicatorHeightPx = indicator.getHeight();
        cacheRectF.top = pivotYPx - indicatorHeightPx / 2f;
        cacheRectF.bottom = cacheRectF.top + indicatorHeightPx;
        if (direction == Direction.FROM_LEFT_TO_RIGHT) {
            cacheRectF.left = pivotXPx + (width - indicatorWidthPx) / 2f;
            cacheRectF.right = cacheRectF.left + indicatorWidthPx;
        } else {
            cacheRectF.right = pivotXPx - (width - indicatorWidthPx) / 2f;
            cacheRectF.left = cacheRectF.right - indicatorWidthPx;
        }
        canvas.drawBitmap(indicator, null, cacheRectF, indicatorPaint);
    }

    // 从上往下绘制的path
    private static void createCurve(Path path, float ctrl1, float ctrl2, float width, float curveHeight, @NonNull Direction direction) {
        if (direction == Direction.FROM_LEFT_TO_RIGHT) {
            path.rCubicTo(0, ctrl1, width, curveHeight - ctrl2, width, curveHeight);
        } else {
            path.rCubicTo(0, ctrl1, -width, curveHeight - ctrl2, -width, curveHeight);
        }
    }

    private final static RectF cacheRectF = new RectF();


    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha((int) (alpha * backgroundAlpha));
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
        // ignore
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private int fromDpToPixel(float dp) {
        return Math.round(DISPLAY_DENSITY * dp);
    }

}
