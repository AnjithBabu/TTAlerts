package com.abdevs.project.ttcalerts;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import utils.AppConstants;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT_TEMP = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        TextView logoTxt = findViewById(R.id.logoTxt);
        TextView logoDesc = findViewById(R.id.logoDesc);
        TextView logoSub = findViewById(R.id.logoSub);
        logoTxt.setTypeface(Typeface.createFromAsset(getAssets(),
                AppConstants.LOGO_REG_FONT));
        logoDesc.setTypeface(Typeface.createFromAsset(getAssets(),
                AppConstants.CRETE_REG_FONT));
        logoSub.setTypeface(Typeface.createFromAsset(getAssets(),
                AppConstants.CRETE_REG_FONT));

        int SPLASH_TIME_OUT = getResources().getInteger(R.integer.splash_timeout);

        // delaying the home screen activity to show after splash screen has been finished
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT == 0 ? SPLASH_TIME_OUT_TEMP : SPLASH_TIME_OUT);
    }
}
