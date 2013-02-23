package lecho.sample.connectdots.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Displaying canvas with optional dots drawn on it. User can connect dots with
 * straight lines. First dot can be connected with second dot, second with third
 * etc.
 * 
 * @author lecho
 * 
 */
public class ConnectDotsView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private static final int TOUCH_TOLERANCE_DP = 24;
    private static final int BACKGROUND = 0xFFDDDDDD;
    // Points to be connected.
    private List<Point> mPoints = new ArrayList<Point>();
    private int mLastPointIndex = 0;
    private int mTouchTolerance;
    private boolean isPathStarted = false;

    public ConnectDotsView(Context context) {
        super(context);
        mCanvas = new Canvas();
        mPath = new Path();
        initPaint();
    }

    public ConnectDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCanvas = new Canvas();
        mPath = new Path();
        initPaint();
    }

    public ConnectDotsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCanvas = new Canvas();
        mPath = new Path();
        initPaint();
    }

    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(BACKGROUND);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        clear();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BACKGROUND);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);

        // TODO remove if you don't want points to be visible.
        for (Point point : mPoints) {
            canvas.drawPoint(point.x, point.y, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            touch_start(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            touch_move(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            touch_up(x, y);
            invalidate();
            break;
        }
        return true;
    }

    private void touch_start(float x, float y) {

        if (checkPoint(x, y, mLastPointIndex)) {
            mPath.reset();
            // User starts from given point so path can be drawn.
            isPathStarted = true;
        } else {
            // User starts move from point which does not belong to mPoints list
            isPathStarted = false;
        }

    }

    private void touch_move(float x, float y) {
        if (isPathStarted) {
            mPath.reset();
            Point point = mPoints.get(mLastPointIndex);
            mPath.moveTo(point.x, point.y);
            if (checkPoint(x, y, mLastPointIndex + 1)) {
                point = mPoints.get(mLastPointIndex + 1);
                mPath.lineTo(point.x, point.y);
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                ++mLastPointIndex;
            } else {
                mPath.lineTo(x, y);
            }
        }
    }

    private void touch_up(float x, float y) {
        mPath.reset();
        if (checkPoint(x, y, mLastPointIndex + 1) && isPathStarted) {
            // Move finished at valid point so I draw whole line.
            // That's the start point of current line segment.
            Point point = mPoints.get(mLastPointIndex);
            mPath.moveTo(point.x, point.y);
            // And that's the end point.
            point = mPoints.get(mLastPointIndex + 1);
            mPath.lineTo(point.x, point.y);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
            // Increment point index.
            ++mLastPointIndex;
            isPathStarted = false;
        }

    }

    /**
     * Checks if user touch point with some tolerance
     */
    private boolean checkPoint(float x, float y, int pointIndex) {
        if (pointIndex >= mPoints.size()) {
            // All dots already connected.
            return false;
        }
        Point point = mPoints.get(pointIndex);
        if (x > (point.x - mTouchTolerance) && x < (point.x + mTouchTolerance)) {
            if (y > (point.y - mTouchTolerance) && y < (point.y + mTouchTolerance)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets up paint attributes.
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        mTouchTolerance = dp2px(TOUCH_TOLERANCE_DP);
    }

    /**
     * Converts dpi units to px
     * 
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public List<Point> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point> points) {
        this.mPoints = points;
    }
}