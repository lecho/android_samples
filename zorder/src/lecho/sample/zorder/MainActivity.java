package lecho.sample.zorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Demonstrates changing Z-order
 * 
 * @author lecho
 * 
 */
public class MainActivity extends Activity {
    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.bringToFront();
        final Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((i % 2) == 0) {
                    final Button btn1 = (Button) findViewById(R.id.btn1);
                    btn1.setText("BUTTON_BLACK_NEW");
                    btn1.bringToFront();
                } else {
                    final Button btn2 = (Button) findViewById(R.id.btn2);
                    btn2.setText("BUTTON_GREY_NEW");
                    btn2.bringToFront();
                }
                ++i;

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((i % 2) == 0) {
            final Button btn1 = (Button) findViewById(R.id.btn1);
            btn1.bringToFront();
        } else {
            final Button btn2 = (Button) findViewById(R.id.btn2);
            btn2.bringToFront();
        }
        ++i;
    }

}
