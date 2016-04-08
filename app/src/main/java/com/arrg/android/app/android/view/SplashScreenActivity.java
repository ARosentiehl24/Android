package com.arrg.android.app.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.arrg.android.app.android.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchMainActivity();
            }
        }, 1250);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (finish) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 250);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish = true;
    }

    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, findViewById(R.id.unimagdalena_logo), getString(R.string.name_of_university));

        startActivity(intent, options.toBundle());
    }
}
