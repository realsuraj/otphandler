package com.genxhire.opt.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

                if(sh.getString("token", null) != null){
                    Intent i = new Intent(SplashScreen.this, HomePage.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashScreen.this, LoginPage.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);

    }

}