package com.threestoges.iyb_app;

import android.app.Activity;
import android.graphics.Color;
import android.view.*;
public class FullScreen {
    public static void configureWindow(Activity activity) {
        if (activity != null) {
            Window window = activity.getWindow();
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}

