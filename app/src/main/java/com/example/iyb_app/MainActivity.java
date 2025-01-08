package com.example.iyb_app;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.io.File;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startSaving(View start)
    {
        String FILENAME = "saved-data.txt";
        String filepath = "user";
        File userData = new File(getExternalFilesDir(filepath), FILENAME);
        findViewById(R.id.start).setEnabled(false);
        ((Button)findViewById(R.id.start)).setText("Saving Now");
        Intent i = new Intent(this, userNew.class);
        Intent l = new Intent(this, lucasclass.class);
        startActivity(!userData.exists() ? i : l);

    }

}