package lecho.sample.rootshell;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//TODO check roottools http://code.google.com/p/roottools/
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edit = (EditText) findViewById(R.id.edit);
        final Button sh = (Button) findViewById(R.id.sh_button);
        sh.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                RootShell.shExecute(new String[] { edit.getText().toString() });

            }
        });
        final Button su = (Button) findViewById(R.id.su_button);
        su.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                RootShell.suExecute(new String[] { edit.getText().toString() });

            }
        });

        final Button suOut = (Button) findViewById(R.id.su_output_button);
        suOut.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                RootShell.suOutputExecute(edit.getText().toString());

            }
        });

        final Button busybox = (Button) findViewById(R.id.busybox_button);
        busybox.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = getFilesDir() + "/busybox --list";
                RootShell.busyboxExecute(s);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
