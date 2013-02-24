package lecho.sample.clipboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Demonstrates usage of android clipboard api.
 * 
 * @author lecho
 * 
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edit = (EditText) findViewById(R.id.edit);
        final Button copy = (Button) findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                copyToClipboard("Content of edit text", edit.getText().toString());
            }
        });
        final Button paste = (Button) findViewById(R.id.paste);
        paste.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edit.setText(getFromClipboard());

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void copyToClipboard(String label, String content) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getApplicationContext()
                    .getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, content);
            clipboardManager.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getApplicationContext()
                    .getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setText(content);
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Content copied", Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private String getFromClipboard() {
        String content = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getApplicationContext()
                    .getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = clipboardManager.getPrimaryClip();
            if (null != clip && clip.getItemCount() > 0) {
                content = clip.getItemAt(0).getText().toString();
            }
        } else {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getApplicationContext()
                    .getSystemService(CLIPBOARD_SERVICE);
            if (null != clipboardManager.getText()) {
                content = clipboardManager.getText().toString();
            }
        }
        return content;
    }

}
