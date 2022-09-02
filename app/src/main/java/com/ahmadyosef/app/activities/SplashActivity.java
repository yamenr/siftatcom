package com.ahmadyosef.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;

/*
* Splash screen: logo anim, 3 secs delay to opening app*/
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // HERE WE ARE TAKING THE REFERENCE OF OUR IMAGE
        // SO THAT WE CAN PERFORM ANIMATION USING THAT IMAGE
        ImageView backgroundImage = findViewById(R.id.splashLogo);
        Utilities utils = Utilities.getInstance();
        TextView tvHello = findViewById(R.id.tvHelloSplash);
        ImageView ivHello = findViewById(R.id.ivHelloSplash);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        backgroundImage.startAnimation(slideAnimation);

        tvHello.setText(utils.sayHello());
        switch (utils.sayHello())
        {
            case "Good Morning":
                ivHello.setImageResource(R.drawable.sunrise);
                break;
            case "Good Afternoon":
                ivHello.setImageResource(R.drawable.sun);
                break;
            case "Good Evening":
                ivHello.setImageResource(R.drawable.night);
                break;
            case "Good Night":
                ivHello.setImageResource(R.drawable.night);
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}