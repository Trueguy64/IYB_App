package com.example.iyb_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class userNew extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;
    String[] currencies = {"(៛) Khmer Riel","($) United States Dollar"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_new);

        autoCompleteTextView = findViewById(R.id.dropcurrencies);
        adapterItems = new ArrayAdapter<String>(this,R.layout.dropdown_currencies, currencies);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(userNew.this,"Item: "+ item, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void handleText(View text)
    {
        EditText t = findViewById(R.id.source);
        String input = t.getText().toString();
        ((TextView)findViewById(R.id.output)).setText(input);
        Toast.makeText(this, input, Toast.LENGTH_LONG).show();
    }
}