package lecho.sample.collapsiblelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearLayoutWithChangingLayoutTransition extends LinearLayout {
	
	@SuppressWarnings("unused")
	private static final String TAG = "demo.layout.transition.DemoLinearLayout";

	public LinearLayoutWithChangingLayoutTransition(Context context) {
		super(context);
	}

	public LinearLayoutWithChangingLayoutTransition(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LinearLayoutWithChangingLayoutTransition(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		LayoutTransitionChangingV11.checkAndSetupChangeAnimationForAllChild(this);
		super.onLayout(changed, l, t, r, b);
	}
}
