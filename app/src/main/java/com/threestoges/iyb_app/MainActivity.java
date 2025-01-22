package com.threestoges.iyb_app;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    // Declares the path and filename of the user data
    private final String FILENAME = "saved-data.txt";
    private final String filepath = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    //Reponsible for the "Start Saving" button functionality 
    public void startSaving(View start)
    {
        // Declares an File object that points to the location of the file
        File userData = new File(getExternalFilesDir(filepath), FILENAME);
        findViewById(R.id.start).setEnabled(false);
        ((Button)findViewById(R.id.start)).setText("Saving Now");
        // Creates two Intent object -a messaging object- which is used to start an activity by requesting an action 
        Intent w = new Intent(this, welcometoiyb.class);
        Intent l = new Intent(this, lucasclass.class);
        // If the userdata does not exist then the intent will start welcometoiyb.class activity, else lucasclass.class will start
        startActivity(!userData.exists() ? w : l);
        //Kills the activity to prevent users from going back to this activity and save memory
        finish();
    }
}
