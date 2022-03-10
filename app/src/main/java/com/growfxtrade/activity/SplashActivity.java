package com.growfxtrade.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.R;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private Activity mActivity;
    protected boolean _active = true;
    protected int _splashTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        openScreen();
    }


    public void openScreen() {
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {

                    if (PrefrenceManager.getString(SplashActivity.this, PrefrenceManager.LOGIN_STATUS).equalsIgnoreCase("true")) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }
            }
        };
        splashTread.start();

    }


}
