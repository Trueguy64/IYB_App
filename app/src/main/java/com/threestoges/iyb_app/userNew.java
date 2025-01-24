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

import static android.app.PendingIntent.getActivity;

public class userNew extends AppCompatActivity {
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_user_new);
        firstname = findViewById(R.id.firstName);
        lastname = findViewById(R.id.lastName);
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
            hideKeyboard(this);
        });
        // Update button state initially in case of pre-filled data
        updateButtonState();

    }

    public void handleText(View text) {
        firstname.addTextChangedListener(textWatcher);
        lastname.addTextChangedListener(textWatcher);
        String familyInput = firstname.getText().toString().trim();
        String givenInput = lastname.getText().toString().trim();
        outputCurrencyToFile(item);
        outputUsernameToFile(familyInput);
        outputUsernameToFile(givenInput);
        Intent l = new Intent(this, BudgetSetup.class);
        startActivity(l);
        finish();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        String familyNameInput = firstname.getText().toString().trim();
        String givenNameInput = lastname.getText().toString().trim();
        String listInput = autoCompleteTextView.getText().toString().trim();
        // Enable the button if both fields are not empty
        findViewById(R.id.button).setEnabled(!familyNameInput.isEmpty() && !givenNameInput.isEmpty() && !listInput.isEmpty());
    }

}