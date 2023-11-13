package com.example.prochat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    TextView from, proinc;
    Animation topanm,bottomanm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        getSupportActionBar().hide();

        logo = findViewById(R.id.splashLogo);
        from = findViewById(R.id.splashFrom);
        proinc = findViewById(R.id.splashProinc);

        topanm = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanm = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo.setAnimation(topanm);
        from.setAnimation(bottomanm);
        proinc.setAnimation(bottomanm);

//spalash screen will be shown for 4 seconds that means 4000 ms ..
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}