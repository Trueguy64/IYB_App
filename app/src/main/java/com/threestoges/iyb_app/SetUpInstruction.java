package com.threestoges.iyb_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SetUpInstruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcometoiyb);

        // Makes the activity fullscreen by making top (notification) and bottom bars translucent
        FullScreen.configureWindow(this);

        Button backButton = findViewById(R.id.backButton);
        Button nextButton = findViewById(R.id.createAccount);
        // Responsible for going to the previous Activity
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent back = new Intent(SetUpInstruction.this, StartUp.class);
                startActivity(back);
                finish();
            }
        });
        // Responsible for going to the next Activity
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent next = new Intent(SetUpInstruction.this, NewUser.class);
                startActivity(next);
                finish();
            }
        });
    }
}