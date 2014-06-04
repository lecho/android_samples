package lecho.sample.collapsiblelayout;

import com.example.sectioncollapsetest.R;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollapsibleLinearLayout extends LinearLayout {

	private static final int ANIMATION_DURATION = 250;
	private TextView mHeader;
	private ImageButton mButton;
	private LinearLayout mContent;
	private boolean isExpanded = true;

	public CollapsibleLinearLayout(Context context) {
		super(context);
		setOrientation(VERTICAL);
		inflate(context, R.layout.widget_tab_section, this);
		mHeader = (TextView) findViewById(R.id.section_header);
		mButton = (ImageButton) findViewById(R.id.section_button);
		mButton.setOnClickListener(new SectionButtonClickListener());
		mButton.setOnClickListener(new SectionButtonClickListener());
		mContent = (LinearLayout) findViewById(R.id.section_content);
		mContent.setPivotY(0);
		ObjectAnimator changeAnimatorAppearing = ObjectAnimator.ofFloat((Object) null, "scaleY", 0f, 1f);
		ObjectAnimator changeAnimatorDisappearing = ObjectAnimator.ofFloat((Object) null, "scaleY", 1f, 0f);
		LayoutTransition layoutTransition = new LayoutTransition();
		layoutTransition.setAnimator(LayoutTransition.APPEARING, changeAnimatorAppearing);
		layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, changeAnimatorDisappearing);
		layoutTransition.setDuration(ANIMATION_DURATION);
		layoutTransition.setStartDelay(LayoutTransition.APPEARING, 0);
		layoutTransition.setStartDelay(LayoutTransition.DISAPPEARING, 0);
		setLayoutTransition(layoutTransition);
	}

	public void setHeaderText(CharSequence text) {
		mHeader.setText(text);
	}

	public void addView(View view) {
		mContent.addView(view);
	}

	public void addView(View view, LayoutParams layoutParams) {
		mContent.addView(view, layoutParams);
	}

	public void addContent(View view) {
		super.addView(view);
	}

	private class SectionButtonClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (isExpanded) {
				// In section I add and remove mContent layout so default Appearing layout transition is enough that
				// why section itself doesn't use LinearLayoutWithChangingLayoutTransition
				CollapsibleLinearLayout.this.removeView(mContent);
				mButton.setImageResource(R.drawable.ic_navigation_expand);
			} else {
				CollapsibleLinearLayout.this.addContent(mContent);
				mButton.setImageResource(R.drawable.ic_navigation_collapse);
			}
			// LayoutTransitionChangingV11.setupChangeAnimationOneTime(mContainer);
			isExpanded = !isExpanded;
		}
	}

}
