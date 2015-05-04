package com.cmu.neighbor2you.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cmu.neighbor2you.R;

/**
 * Created by mangobin on 15-5-3.
 */
public class SplashActivity extends Activity{

    private static final int DELAY_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_init);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                SplashActivity.this.finish();
            }
        }, DELAY_TIME);
    }
}
