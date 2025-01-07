package com.example.iyb_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.widget.TextView;
import android.content.Intent;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private final String FILENAME = "saveddata.txt";
    private final String filepath = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void startSaving(View start)
    {
        File userData = new File(getExternalFilesDir(filepath), FILENAME);
        findViewById(R.id.start).setEnabled(false);
        ((Button)findViewById(R.id.start)).setText("Saving Now");
        Intent i = new Intent(this, userNew.class);
        Intent l = new Intent(this, lucasclass.class);
        startActivity(!userData.exists() ? i : l);
//        start.setEnabled(false);
//        Button button = (Button) start;
//        button.setText("Disabled");

    }

}