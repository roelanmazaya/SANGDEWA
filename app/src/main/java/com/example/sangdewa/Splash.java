/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Splash extends Activity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 3000; //This is 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        splashProgress = findViewById(R.id.splashProgress);
        playProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(Splash.this, Login.class);
                startActivity(mySuperIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(3000)
                .start();
    }
}




