package lecho.sample.connectdots;

import java.util.ArrayList;
import java.util.List;

import lecho.sample.connectdots.view.ConnectDotsView;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Point> points = new ArrayList<Point>();
        Point p1 = new Point(20, 20);
        points.add(p1);
        Point p2 = new Point(100, 100);
        points.add(p2);
        Point p3 = new Point(200, 250);
        points.add(p3);
        Point p4 = new Point(280, 400);
        points.add(p4);
        Point p5 = new Point(350, 600);
        points.add(p5);
        Point p6 = new Point(400, 500);
        points.add(p6);
        ConnectDotsView connectDotsView = (ConnectDotsView) findViewById(R.id.connect_dots_view);
        connectDotsView.setPoints(points);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
