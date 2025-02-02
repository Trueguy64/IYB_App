package com.threestoges.iyb_app;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import java.io.File;

public class StartUp extends AppCompatActivity {
    // Declares the file names of user data
    private final String FILENAME = "saved-data.txt";
    private final String filepath = "user";
    private final String nbFileName = "net-budget.txt";
    // Declares the pointer objects to both files
    private File userData;
    private File userBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Will display the splashscreen on this particular activity when the application starts
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        // Makes the activity fullscreen by making top (notification) and bottom bars translucent
        FullScreen.configureWindow(this);
        userData = new File(getExternalFilesDir(filepath), FILENAME);
        userBudget = new File(getExternalFilesDir(filepath), nbFileName);
        checkUserData();
    }
    // Responsible for the button function present in the start screen
    public void startSaving(View start)
    {
        ((Button)findViewById(R.id.start)).setText("Saving Now");
        checkUserData();
    }
    public void checkUserData()
    {
        Intent l = new Intent(this, MainMenu.class);
        Intent b = new Intent(this, BudgetSetup.class);
        Intent w = new Intent(this, SetUpInstruction.class);
        // The application will only bring the user to the main menu by checking if both user data and their set budget exists
        if(userData.exists()&&userBudget.exists()){
            startActivity(l);
            finish();
        } else if (userData.exists()&&!userBudget.exists()) {
            startActivity(b);
            finish();
        }
        else
        {
            startActivity(w);
            finish();
        }
    }
}