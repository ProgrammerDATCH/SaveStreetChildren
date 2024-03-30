package com.savestreetchildren.app;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView logoImage;
    private Handler checkConn;
    private Runnable checkRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoImage = findViewById(R.id.logoImage);

        Animation zoomInOut = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInOut.setDuration(1000);
        zoomInOut.setRepeatCount(Animation.INFINITE);
        zoomInOut.setRepeatMode(Animation.REVERSE);


        logoImage.startAnimation(zoomInOut);


        checkConn = new Handler();
        checkRun = new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected())
                {
                    Toast.makeText(MainActivity.this, "No Network!", Toast.LENGTH_LONG).show();
                    checkConn.postDelayed(checkRun, 10000);
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 1000);
                }
            }
        };

        checkConn.post(checkRun);

    }
}