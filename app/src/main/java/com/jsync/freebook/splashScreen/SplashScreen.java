package com.jsync.freebook.splashScreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jsync.freebook.CentralActivity;
import com.jsync.freebook.MainActivity;
import com.jsync.freebook.R;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ParseUser.getCurrentUser() != null){
                 goToHomeActivity();
                }else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);

    }
    private void goToHomeActivity(){
        Intent centralActivity = new Intent(SplashScreen.this,CentralActivity.class);
        startActivity(centralActivity);
        finish();
    }
}
