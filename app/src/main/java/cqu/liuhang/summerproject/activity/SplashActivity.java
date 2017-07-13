package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cqu.liuhang.summerproject.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
