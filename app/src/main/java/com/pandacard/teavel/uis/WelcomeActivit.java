package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.pandacard.teavel.R;

public class WelcomeActivit extends AppCompatActivity {

    public Runnable toSplashActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(WelcomeActivit.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    };

    class WelcomeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);


        WelcomeHandler Handler = new WelcomeHandler();
        Handler.postDelayed(toSplashActivity, 2000);
    }
}
