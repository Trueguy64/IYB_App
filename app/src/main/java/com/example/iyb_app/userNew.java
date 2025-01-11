package com.example.iyb_app;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class userNew extends AppCompatActivity {
    //Defining Activity Wide Variables
    private final String filepath = "user";
    private final String FILENAME = "saved-data.txt";
    EditText family;
    EditText given;
    private String item;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] currencies = {"(៛) Khmer Riel", "($) United States Dollar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_new);
        family = findViewById(R.id.familyName);
        given = findViewById(R.id.givenName);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        autoCompleteTextView = findViewById(R.id.dropcurrencies);
        autoCompleteTextView.addTextChangedListener(textWatcher);

        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_currencies, currencies);
        autoCompleteTextView.setAdapter(adapterItems);

        // Set the item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            item = parent.getItemAtPosition(position).toString();
            // Update the button state when an item is selected
            updateButtonState();
        });
        // Update button state initially in case of pre-filled data
        updateButtonState();

    }

    public void handleText(View text) {
        family.addTextChangedListener(textWatcher);
        given.addTextChangedListener(textWatcher);
        String familyInput = family.getText().toString().trim();
        String givenInput = given.getText().toString().trim();
        outputCurrencyToFile(item);
        outputUsernameToFile(familyInput);
        outputUsernameToFile(givenInput);
        Intent l = new Intent(this, lucasclass.class);
        startActivity(l);
    }
    public void outputUsernameToFile(String fileContent)
    {
        if(!fileContent.isEmpty()){
            File myExternalFile = new File(getExternalFilesDir(filepath), FILENAME);
            try(FileOutputStream fileOutputStream = new FileOutputStream(myExternalFile,true)) {
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(fileContent.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            family.setText("");
            given.setText("");
        } else{
            Toast.makeText(this, "Text field cannot be empty.",Toast.LENGTH_SHORT).show();
        }
    }
    public void outputCurrencyToFile(String item) {
        if (!item.isEmpty()) {
            File myExternalFile = new File(getExternalFilesDir(filepath), FILENAME);
            try (FileOutputStream fileOutputStream = new FileOutputStream(myExternalFile,true)) {
                if (item.equals("(៛) Khmer Riel")) {
                    fileOutputStream.write("khr".getBytes());
                } else if (item.equals("($) United States Dollar")) {
                    fileOutputStream.write("usd".getBytes());
                }
            } catch (IOException e) {
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
        String familyNameInput = family.getText().toString().trim();
        String givenNameInput = given.getText().toString().trim();
        String listInput = autoCompleteTextView.getText().toString().trim();
        // Enable the button if both fields are not empty
        findViewById(R.id.button).setEnabled(!familyNameInput.isEmpty() && !givenNameInput.isEmpty() && !listInput.isEmpty());
    }
}