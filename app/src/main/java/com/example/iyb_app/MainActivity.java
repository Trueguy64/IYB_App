package com.example.iyb_app;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.io.File;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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
        finish();
    }
}