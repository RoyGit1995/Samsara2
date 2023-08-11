package com.example.samsara;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationContentActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final double TARGET_LATITUDE = 52.679338513377715; // Example latitude
    private static final double TARGET_LONGITUDE = -8.578365547567454; // Example longitude
    private static final float PROXIMITY_THRESHOLD_METERS = 30; // Example proximity threshold in meters

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geolocationbasedprofile);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    float distance = calculateDistance(location.getLatitude(), location.getLongitude(),
                            TARGET_LATITUDE, TARGET_LONGITUDE);


                    Log.d("Distance to target", "Float value: " + distance);
                    //Log.d("My location Latitude ", "Float value: " + location.getLatitude());
                    //Log.d("My location Longitude ", "Float value: " + location.getLongitude());
                    //Log.d("My target Latitude ", "Float value: " + TARGET_LATITUDE);
                    //Log.d("My target Longitude ", "Float value: " + TARGET_LONGITUDE);



                    if (distance <= PROXIMITY_THRESHOLD_METERS) {
                        // User is within proximity, trigger content display
                        showContentAvailableLayout();
                    } else {
                        // User is outside proximity, hide content
                        hideContentAvailableLayout();
                    }
                }
            }
        };


        requestLocationUpdates();
    }

    // Calculate distance between two points using Haversine formula
    private float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in meters
        final double earthRadius = 6371000; // Approximate value

        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return (float) distance;
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(5000) // Update interval in milliseconds
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove location updates when the activity is destroyed
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void showContentAvailableLayout() {
        // Inflate and show the content available layout
        View contentAvailableLayout = getLayoutInflater().inflate(R.layout.promptpopupspecificrecipient, null);
        ConstraintLayout constraintLayout = findViewById(R.id.geolocationLayout); // Assuming you have a FrameLayout in your activity's layout
        videoView = findViewById(R.id.videoView);
        // Load video from the drawable folder

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoplayback_italy);
        videoView.setVideoURI(videoUri);
        // Add media controller for play, pause, etc.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // The video is prepared, but don't start playing automatically
                // You can add a play button or handle playback as needed
            }
        });
        constraintLayout.addView(contentAvailableLayout);
    }

    private void hideContentAvailableLayout() {
        // Remove the content available layout from the FrameLayout
        ConstraintLayout constraintLayout = findViewById(R.id.geolocationLayout);
        constraintLayout.removeAllViews();
    }
}
