package com.example.iyb_app;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import java.io.File;

public class MainActivity extends AppCompatActivity {
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
    public void startSaving(View start)
    {
        String FILENAME = "saved-data.txt";
        String filepath = "user";
        File userData = new File(getExternalFilesDir(filepath), FILENAME);
        findViewById(R.id.start).setEnabled(false);
        ((Button)findViewById(R.id.start)).setText("Saving Now");
        Intent w = new Intent(this, welcometoiyb.class);
        Intent l = new Intent(this, lucasclass.class);
        startActivity(!userData.exists() ? w : l);
        finish();
    }
}