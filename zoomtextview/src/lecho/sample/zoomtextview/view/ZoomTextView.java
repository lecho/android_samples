package lecho.sample.zoomtextview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;

/**
 * Text view with pinch-to-zoom and double-tap zoom ability. For scrolling wrap
 * it with CustomScrollView.
 * 
 * @author lecho
 * 
 */
public class ZoomTextView extends TextView {

    private static final float MIN_SCALE_FACTOR = 1.0f;
    private static final float MAX_SCALE_FACTOR = 2.0f;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mTapDetector;
    private float mScaleFactor = MIN_SCALE_FACTOR;
    private boolean mFullZoom = false;
    private float mOriginalFontSize;

    public ZoomTextView(Context context) {
        this(context, null, 0);

    }

    public ZoomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mTapDetector = new GestureDetector(context, new TapListener());
        mOriginalFontSize = getTextSize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mTapDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(mScaleFactor, MAX_SCALE_FACTOR));
            if (mScaleFactor == MAX_SCALE_FACTOR) {
                mFullZoom = true;
            } else {
                mFullZoom = false;
            }

            setTextSize(TypedValue.COMPLEX_UNIT_PX, mScaleFactor * mOriginalFontSize);
            invalidate();
            return true;
        }
    }

    private class TapListener extends SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mFullZoom) {
                mScaleFactor = MIN_SCALE_FACTOR;
                mFullZoom = false;
            } else {
                mScaleFactor = MAX_SCALE_FACTOR;
                mFullZoom = true;
            }
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mScaleFactor * mOriginalFontSize);
            return true;
        }
    }

}
