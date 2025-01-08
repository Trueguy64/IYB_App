package com.example.iyb_app;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class userNew extends AppCompatActivity {
    //Defining Activity Wide Variables
    private final String filepath = "user";
    private final String FILENAME = "saved-data.txt";
    EditText username;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] currencies = {"(៛) Khmer Riel", "($) United States Dollar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_new);
        username = findViewById(R.id.source);
        autoCompleteTextView = findViewById(R.id.dropcurrencies);
        autoCompleteTextView.addTextChangedListener(textWatcher);

        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_currencies, currencies);
        autoCompleteTextView.setAdapter(adapterItems);

        // Set the item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(userNew.this, "Item: " + item, Toast.LENGTH_SHORT).show();
                outputCurrencyToFile(item);
                // Update the button state when an item is selected
                updateButtonState();
            }
        });

        // Update button state initially in case of pre-filled data
        updateButtonState();
    }

    public void handleText(View text) {
        username.addTextChangedListener(textWatcher);
        String input = username.getText().toString().trim();
        Toast.makeText(this, input, Toast.LENGTH_LONG).show();
        outputUsernameToFile();
    }
    public void outputUsernameToFile()
    {
        String fileContent = username.getText().toString().trim();
        if(!fileContent.isEmpty()){
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
            username.setText("");
        } else{
            Toast.makeText(this, "Text field cannot be empty.",Toast.LENGTH_SHORT).show();
        }
    }
    public void outputCurrencyToFile(String item)
    {
        if(!item.isEmpty()){
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
    }
    private final TextWatcher textWatcher = new TextWatcher() {
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