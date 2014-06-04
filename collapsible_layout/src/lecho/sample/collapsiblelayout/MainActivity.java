package lecho.sample.collapsiblelayout;

import com.example.sectioncollapsetest.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout mainlayout = (LinearLayout) findViewById(R.id.main_layout);
		CollapsibleLinearLayout section = new CollapsibleLinearLayout(getApplicationContext());
		section.setHeaderText("sekcja1");
		for (int i = 0; i < 5; ++i) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText("Text Item");
			section.addView(tv);
		}

		mainlayout.addView(section);
		CollapsibleLinearLayout section2 = new CollapsibleLinearLayout(getApplicationContext());
		section2.setHeaderText("sekcja2");
		for (int i = 0; i < 5; ++i) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText("Text Item");
			section2.addView(tv);
		}
		mainlayout.addView(section2);
		for (int i = 0; i < 5; ++i) {
			View v = new View(getApplicationContext());
			v.setBackgroundColor(Color.LTGRAY);
			mainlayout.addView(v);
		}
		// Setting changing transition for main layout, this layout contains sections which size will change.
		LayoutTransitionChangingV11.enableChangeTransition(mainlayout.getLayoutTransition());
		LayoutTransitionChangingV11.setupChangeAnimation(mainlayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
