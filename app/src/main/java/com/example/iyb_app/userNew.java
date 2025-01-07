package com.example.iyb_app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class userNew extends AppCompatActivity {
    TextView fieler;
    private final String filepath = "user";
    private String fileContent = "";
    private final String FILENAME = "saveddata.txt";
    EditText username;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] currencies = {"(៛) Khmer Riel", "($) United States Dollar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_new);
        username = findViewById(R.id.source);
        fieler = findViewById(R.id.fieler);
        autoCompleteTextView = findViewById(R.id.dropcurrencies);
        autoCompleteTextView.addTextChangedListener(textWatcher);

        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_currencies, currencies);
        autoCompleteTextView.setAdapter(adapterItems);

        // Set the item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ((TextView) findViewById(R.id.currencyOutput)).setText(item);
                Toast.makeText(userNew.this, "Item: " + item, Toast.LENGTH_SHORT).show();
                if(!item.equals("")){
                    File myExternalFile = new File(getExternalFilesDir(filepath), FILENAME);
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(myExternalFile);
                        if(item.equals("(៛) Khmer Riel")) {
                            try {
                                fileOutputStream.write("khr".getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (item.equals("($) United States Dollar")) {
                            try {
                                fileOutputStream.write("usd".getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Update the button state when an item is selected
                updateButtonState();
            }
        });

        // Update button state initially in case of pre-filled data
        updateButtonState();
    }

    public void handleText(View text) {
        username.addTextChangedListener(textWatcher);
        String input = username.getText().toString();
        ((TextView) findViewById(R.id.nameOutput)).setText(input);
        Toast.makeText(this, input, Toast.LENGTH_LONG).show();
        fileContent = username.getText().toString().trim();
        if(!fileContent.equals("")){
            File myExternalFile = new File(getExternalFilesDir(filepath), FILENAME);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(myExternalFile,true);
                try {
                    fileOutputStream.write("\n".getBytes());
                    fileOutputStream.write(fileContent.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            fieler.setText(fileContent);
            username.setText("");
        } else{
            Toast.makeText(this, "Text field cannot be empty.",Toast.LENGTH_SHORT);
        }

    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Update the button state when username is changed
            updateButtonState();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // Method to update button state based on inputs
    private void updateButtonState() {
        String usernameInput = username.getText().toString().trim();
        String listInput = autoCompleteTextView.getText().toString().trim();

        // Enable the button if both fields are not empty
        findViewById(R.id.button).setEnabled(!usernameInput.isEmpty() && !listInput.isEmpty());
    }
}