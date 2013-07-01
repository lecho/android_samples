package lecho.sample.listpopupwindow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String[] STRINGS = { "Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option", "Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option","Option1",
			"Option2", "Option3", "Option"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopup(v);

			}
		});
	}

	private void showPopup(View anchorView) {
		final ListPopupWindow popup = new ListPopupWindow(this);
		popup.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, STRINGS));
		popup.setAnchorView(anchorView);
		popup.setWidth(ListPopupWindow.WRAP_CONTENT);
		popup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this, "Clicked item " + position,
						Toast.LENGTH_SHORT).show();
				popup.dismiss();
			}
		});
		popup.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}
}
