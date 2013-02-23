package lecho.sample.imagespager;

import java.util.ArrayList;
import java.util.List;

import lecho.sample.imagespager.R;
import lecho.sample.imagespager.adapter.ImagesPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        List<Integer> imagesResources = new ArrayList<Integer>();
        imagesResources.add(R.drawable.one);
        imagesResources.add(R.drawable.two);
        imagesResources.add(R.drawable.three);
        pager.setAdapter(new ImagesPagerAdapter(getApplicationContext(), imagesResources));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
