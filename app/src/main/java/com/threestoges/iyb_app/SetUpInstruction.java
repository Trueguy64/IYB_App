package com.threestoges.iyb_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SetUpInstruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcometoiyb);
        // Makes the activity fullscreen by making top (notification) and bottom bars translucent
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    // Responsible for going to the next Activity
    public void newAccount(View startsaver)
    {
        findViewById(R.id.startsaver).setEnabled(false);
        Intent w = new Intent(this, NewUser.class);
        startActivity(w);
    }
}