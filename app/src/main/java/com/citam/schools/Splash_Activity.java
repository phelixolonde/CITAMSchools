package com.citam.schools;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Activity extends Activity {
    TextView txtSplash;

    Animation  animation2;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        txtSplash = findViewById(R.id.splash_text);

        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        animation2.reset();


        txtSplash.clearAnimation();
        txtSplash.startAnimation(animation2);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent=new Intent(Splash_Activity.this,Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splash_Activity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Splash_Activity.this.finish();
                }
            }
        };
        thread.start();
    }
}
