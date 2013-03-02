package lecho.sample.contactbyphone;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.view.Menu;
import android.widget.Toast;

/**
* Search contact by given phone number
*
* @author Lecho
*/
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find contact by given number
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode("xxxxxxxxx"));
        String[] projection = new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup.NUMBER,
                PhoneLookup.NORMALIZED_NUMBER };
        Cursor c = getContentResolver().query(uri, projection, null, null, null);
        if (c.moveToFirst()) {// while(c.moveToNext()){
            // get display name
            String name = c.getString(c.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
            // get number assigned by user to given contact
            String number = c.getString(c.getColumnIndexOrThrow(PhoneLookup.NUMBER));
            // get normalized E164 number
            String normalized = c.getString(c.getColumnIndexOrThrow(PhoneLookup.NORMALIZED_NUMBER));
            Toast.makeText(getApplicationContext(),
                    "Name: " + name + "Number: " + number + "; normalized: " + normalized, Toast.LENGTH_LONG).show();
        }
        c.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
