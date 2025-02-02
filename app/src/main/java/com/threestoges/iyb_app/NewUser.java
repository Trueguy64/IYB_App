package com.threestoges.iyb_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class NewUser extends AppCompatActivity {
    //Defining Activity Wide Variables
    private final String filepath = "user";
    private final String FILENAME = "saved-data.txt";
    EditText firstname;
    EditText lastname;
    private String item;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] currencies = {"(៛) Khmer Riel", "($) United States Dollar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new);
        firstname = findViewById(R.id.firstName);
        lastname = findViewById(R.id.lastName);

        // Makes the activity fullscreen by making top (notification) and bottom bars translucent
        FullScreen.configureWindow(this);

        // Custom ArrayAdapter for the currency selection
        autoCompleteTextView = findViewById(R.id.dropcurrencies);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_currencies, currencies);
        autoCompleteTextView.setAdapter(adapterItems);

        new KeyboardUtil(this, findViewById(android.R.id.content).getRootView());
        // Set the item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            item = parent.getItemAtPosition(position).toString();
            // Update the button state when an item is selected
            updateButtonState();
            hideKeyboard(this);
        });
        // Update button state initially in case of pre-filled data
        updateButtonState();

    }
    // Handles the all the user's input including name and currency selection
    public void handleText(View text) {
        firstname.addTextChangedListener(textWatcher);
        lastname.addTextChangedListener(textWatcher);
        String familyInput = firstname.getText().toString().trim();
        String givenInput = lastname.getText().toString().trim();
        // Calls the output functions which are responsible for writing user input into the user data file
        outputCurrencyToFile(item);
        outputUsernameToFile(familyInput);
        outputUsernameToFile(givenInput);
        // Goes to the budget setting up activity after the output is done
        Intent l = new Intent(this, BudgetSetup.class);
        startActivity(l);
        finish();
    }
    // Responsible for hiding the keyboard after the user selects a currency
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        // Hides the soft keyboard
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    // Writes the user's first and last name to the saved-data file
    public void outputUsernameToFile(String fileContent)
    {
        // Checks if the user input is empty, and if not the conditional statement will continue
        if(!fileContent.isEmpty()){
            File myExternalFile = new File(getExternalFilesDir(filepath), FILENAME);
            try(FileOutputStream fileOutputStream = new FileOutputStream(myExternalFile,true)) {
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.write(fileContent.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Empties both text files
            firstname.setText("");
            lastname.setText("");
        } else{
            Toast.makeText(this, "Text field cannot be empty.",Toast.LENGTH_SHORT).show();
        }
    }
    public void outputCurrencyToFile(String item) {
        if (!item.isEmpty()) {
            File myExternalFile = new File(getExternalFilesDir(filepath), FILENAME);
            try (FileOutputStream fileOutputStream = new FileOutputStream(myExternalFile,true)) {
                if (item.equals("(៛) Khmer Riel")) {
                    // Writes khr to the file if the user selects Khmer Riel
                    fileOutputStream.write("khr".getBytes());
                } else if (item.equals("($) United States Dollar")) {
                    // Writes usd to the file if the user selects United States Dollar
                    fileOutputStream.write("usd".getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // Responsible for detecting if the text fields are empty or not by actively checking it
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        // Enables the button if there is text with text fields
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
        // Cleans the inputted text
        String familyNameInput = firstname.getText().toString().trim();
        String givenNameInput = lastname.getText().toString().trim();
        String listInput = autoCompleteTextView.getText().toString().trim();
        // Enable the button if all three fields (first name, last name, and currency selection) are not empty
        findViewById(R.id.button).setEnabled(!familyNameInput.isEmpty() && !givenNameInput.isEmpty() && !listInput.isEmpty());
    }

}