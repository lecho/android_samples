package com.github.lecho.mocklocationexample;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Location location1 = new Location(LocationManager.GPS_PROVIDER);
        location1.setAccuracy(5);
        location1.setLongitude(19);
        location1.setLatitude(51);
        location1.setTime(System.currentTimeMillis());
        location1.setAltitude(150);
        location1.setBearing(0.5f);

        final Location location2 = new Location(location1);
        location2.setProvider(LocationManager.NETWORK_PROVIDER);

        final MockProvider mockProvider1 = new MockProvider(getApplicationContext(), LocationManager.GPS_PROVIDER);
        mockProvider1.startProvider(location1);

        final MockProvider mockProvider2 = new MockProvider(getApplicationContext(), LocationManager.NETWORK_PROVIDER);
        mockProvider2.startProvider(location2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mockProvider1.stopProvider();
                mockProvider2.stopProvider();
                Log.w("TAG", "Stopping providers");
            }
        }, 5000);
    }


    /**
     * Mock location provider
     */
    private static class MockProvider {

        private static final String TAG = MockProvider.class.getSimpleName();
        private final ExecutorService executor = Executors.newFixedThreadPool(1);
        private final LocationManager locationManager;
        private final String providerName;
        public Future future;

        public MockProvider(Context context, String providerName) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            this.providerName = providerName;
        }

        public void startProvider(Location location) {
            stopProvider();
            locationManager.addTestProvider(providerName, false, false, false, false, true, false, false, 0, 5);
            locationManager.setTestProviderEnabled(providerName, true);
            final MockProviderRunnable runnable = new MockProviderRunnable(locationManager, providerName, location);
            future = executor.submit(runnable);
        }

        public void stopProvider() {
            if (future != null) {
                future.cancel(true);
                locationManager.setTestProviderEnabled(providerName, false);
                locationManager.removeTestProvider(providerName);
            }
        }
    }

    private static class MockProviderRunnable implements Runnable {

        private static final String TAG = MockProviderRunnable.class.getSimpleName();
        private static final long LOCATION_UPDATE_INTERVAL_MS = 800;
        private final LocationManager locationManager;
        private final String providerName;
        private final Location location;

        public MockProviderRunnable(LocationManager locationManager, String providerName, Location location) {
            this.locationManager = locationManager;
            this.providerName = providerName;
            this.location = new Location(location);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    location.setTime(System.currentTimeMillis());
                    locationManager.setTestProviderLocation(providerName, location);
                    Log.d(TAG, "Update location: " + location.toString());
                    TimeUnit.MILLISECONDS.sleep(LOCATION_UPDATE_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Log.w(TAG, "InterruptedException - stopping provider: " + providerName);
                    return;
                }
            }
        }
    }
}
