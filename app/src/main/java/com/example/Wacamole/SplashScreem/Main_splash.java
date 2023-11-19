package com.example.Wacamole.SplashScreem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.Wacamole.MainActivity;
import com.example.Wacamole.R;


public class Main_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);

        ImageView image = findViewById(R.id.topoFondo);

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        image.startAnimation(slideAnimation);
        openMain();
    }

    public void openMain(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main_splash.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 3000);
    }
}