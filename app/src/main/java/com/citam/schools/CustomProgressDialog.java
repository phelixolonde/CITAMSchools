package com.citam.schools;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by HANSEN on 17/01/2018.
 */

public class CustomProgressDialog extends Dialog {

    private ImageView iv;

    public CustomProgressDialog(Context context, int resourceIdOfImage) {
        super(context, R.style.TransProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int width = 70, height = 70;
        int h = 400, w = 400;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.gravity=Gravity.CENTER;
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(h, w);
        layout.setLayoutParams(params1);
        iv = new ImageView(context);

        iv.setImageResource(resourceIdOfImage);
        layout.addView(iv, params);
        addContentView(layout, params1);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        iv.setAnimation(anim);
        iv.startAnimation(anim);
    }
}

