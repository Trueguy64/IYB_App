package com.threestoges.iyb_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.*;

import java.io.*;
import java.util.*;

public class LogsManager extends AppCompatActivity {
    private String currency;
    private double budget;
    private double netBudget;
    private final String filePath = "user";
    private final String fileName = "logs.txt";
    private final String nbFileName = "net-budget.txt";
    private final List<String> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logs_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setup, file integrity
        File userFile = new File(getExternalFilesDir(filePath), "saved-data.txt");
        currency = (userFile.exists() ? readFile(userFile, 0).toUpperCase() : "USD");

        File budgetFile = new File(getExternalFilesDir(filePath), nbFileName);
        budget = (budgetFile.exists() ? Double.parseDouble(readFile(budgetFile, 1)) : 1);
        netBudget = (budgetFile.exists() ? Double.parseDouble(readFile(budgetFile, 0)) : budget);

        File logFile = new File(getExternalFilesDir(filePath), fileName);
        if(logFile.exists()){
            int s = readFile(logFile, -1).split("\n").length;
            for(int q = 0; q < s; q++){
                logs.add(readFile(logFile, q));
            }
        }
        //end of setup

        //setting up listview
        ListView simpleList = findViewById(R.id.simpleListView);
        String[] converted = new String[logs.size()]; //number of entries in logs
        String line;
        for(int q = converted.length-1; q >= 0; q--){ //loop from top to bottom for latest entry
            line = logs.get(q);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat getDate = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = getDate.format(new Date());
            if(line.substring(3, 5).equals(currentDate.substring(3,5))){ //only add entries from current month
                line = line.replaceAll(",", "\n");
                converted[converted.length-q-1] = line;
            }
        }
        //Log.d("inputlog", Arrays.toString(converted));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, converted);
        simpleList.setAdapter(arrayAdapter); //set array converted to listview

        Button backButton = findViewById(R.id.backButton);
        Button addEntry = findViewById(R.id.addEntryButton);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){addToLogs();}
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent back = new Intent(LogsManager.this, MainMenu.class);
                startActivity(back);
                finish();
            }
        });
    }

    private String sName;
    private String sCategory;
    private double sCost;

    @SuppressLint("SetTextI18n")
    private void addToLogs(){
        //start dialog/popup
        final Dialog addToLogs = new Dialog(this);
        Objects.requireNonNull(addToLogs.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(255, 7, 17, 13)));
        addToLogs.setContentView(R.layout.popup_add_entry);
        addToLogs.setCancelable(true);
        addToLogs.show();
        TextView titleCost = addToLogs.findViewById(R.id.costTitle);
        titleCost.setText("TOTAL COST (" + currency + "):"); //user currency

        //setting up all of the input fields in popup
        Spinner dropdown = addToLogs.findViewById(R.id.categoryDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item); //string array from strings.xml
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        EditText name = addToLogs.findViewById(R.id.logName);
        EditText cost = addToLogs.findViewById((R.id.totalCost));
        Button logAll = addToLogs.findViewById(R.id.logAllButton);
        File logFile = new File(getExternalFilesDir(filePath), fileName);
        logAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!String.valueOf(name.getText()).isEmpty() && !String.valueOf(cost.getText()).isEmpty()){
                    try{
                        //getting values from input fields
                        sName = String.valueOf(name.getText());
                        sCategory = dropdown.getSelectedItem().toString();
                        sCost = Double.parseDouble(cost.getText().toString());
                        if(netBudget - sCost >= 0){
                            netBudget -= sCost;
                            File budgetFile = new File(getExternalFilesDir(filePath), nbFileName);
                            if(budgetFile.exists())
                                budgetFile.delete(); //deleting file to rewrite
                            try(FileOutputStream f = new FileOutputStream(budgetFile,true)){
                                //rewriting budgetFile with new values
                                f.write((netBudget + "\n").getBytes());
                                f.write((String.valueOf(budget)).getBytes());
                            } catch(IOException e){
                                Toast.makeText(LogsManager.this, "ERROR: BUDGET", Toast.LENGTH_SHORT).show();
                            }

                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat getDate = new SimpleDateFormat("dd-MM-yyyy");
                            String currentDate = getDate.format(new Date());
                            int id = 0;
                            String q = (currentDate + "," + sName + "," + sCategory + "," + sCost + "," + currency + ",id: " + id); //logs string format
                            for(String c: logs){
                                id += c.equals(q) ? 1 : 0; //checking for duplicates
                            }
                            q = (currentDate + "," + sName + "," + sCategory + "," + sCost + "," + currency + ",id: " + id);
                            logs.add(q);
                            //writing new log entry into logFIle
                            try(FileOutputStream f = new FileOutputStream(logFile,true)){
                                f.write((q + "\n").getBytes());
                            } catch(IOException e){
                                Toast.makeText(LogsManager.this, "LOG ERROR, TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }
                            Intent refresh = new Intent(LogsManager.this, LogsManager.class);
                            startActivity(refresh);
                        } else {
                            Toast.makeText(LogsManager.this, "COST HIGHER THAN NET BUDGET, TRY AGAIN", Toast.LENGTH_SHORT).show();
                        }
                    } catch(NumberFormatException e){
                        Toast.makeText(LogsManager.this, "INVALID INPUT", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogsManager.this, "MISSING FIELDS", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Log.d("inputlog2", readFile(logFile, -1));
    }

    //file parser for scraping
    private String readFile(File file, int line){
        if(line < 0){
            try(BufferedReader r = new BufferedReader(new FileReader(file))){
                StringBuilder listed = new StringBuilder(); //to append each line
                String add;
                while((add = r.readLine()) != null){
                    listed.append(add).append("\n");
                }
                r.close();
                return listed.toString(); //returns string that can be converted into array || list
            } catch(IOException e){
                Toast.makeText(this, "ERROR: BUFFEREDREADER", Toast.LENGTH_SHORT).show();
                return "empty";
            }
        }
        //gets string from parameter value line
        try(BufferedReader r = new BufferedReader(new FileReader(file))){
            String result = r.readLine();
            for(int q = 0; q < line; q++){
                result = r.readLine();
            }
            r.close();
            return result;
        } catch(IOException e){
            Toast.makeText(this, "NOTHING TO READ", Toast.LENGTH_SHORT).show();
            return "empty";
        }
    }
}