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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class MainMenu extends AppCompatActivity {
    private String currency;
    private double budget;
    private double netBudget;
    private final String filePath = "user";
    private final String fileName = "logs.txt";
    private final String nbFileName = "net-budget.txt";
    private final List<String> logs = new ArrayList<>();

    private TextView netBudgetText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //retrieving user settings for currency
        File userFile = new File(getExternalFilesDir(filePath), "saved-data.txt");
        currency = (userFile.exists() ? readFile(userFile, 0).toUpperCase() : "USD");

        //retrieving up-to-date user net-budget and budget
        File budgetFile = new File(getExternalFilesDir(filePath), nbFileName);
        budget = (budgetFile.exists() ? Double.parseDouble(readFile(budgetFile, 1)) : 1);
        netBudget = (budgetFile.exists() ? Double.parseDouble(readFile(budgetFile, 0)) : budget);
        netBudgetText = findViewById(R.id.netBudgetText);
        updateNetBudgetText();
        //update bar and surplus text
        updateBar();
        updateSurplusText();

        //extracting expenditure logs from logFile into logs
        File logFile = new File(getExternalFilesDir(filePath), fileName);
        if(logFile.exists()){
            int s = readFile(logFile, -1).split("\n").length;
            for(int q = 0; q < s; q++){
                logs.add(readFile(logFile, q));
            }
        }

        //button input setup
        Button openLogs = findViewById(R.id.openLogsButton);
        Button addEntry = findViewById(R.id.addEntryButton);
        Button openAnalytics = findViewById(R.id.analyticsButton);
        ImageButton editBudget = findViewById(R.id.editBudgetButton);
        openAnalytics.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent analyticsPage = new Intent(MainMenu.this, GraphAnalytics.class);
                startActivity(analyticsPage);
                finish();
            }
        });
        openLogs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent logsPage = new Intent(MainMenu.this, LogsManager.class);
                startActivity(logsPage);
                finish();
            }
        });
        addEntry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){addToLogs();}
        });
        editBudget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent budgetPage = new Intent(MainMenu.this, BudgetSetup.class);
                startActivity(budgetPage);
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
                if(!String.valueOf(name.getText()).isEmpty() && !String.valueOf(cost.getText()).isEmpty()){ //fields cannot be empty or toast message prints out
                    try{
                        //getting values from input fields
                        sName = String.valueOf(name.getText());
                        sCategory = dropdown.getSelectedItem().toString();
                        sCost = Double.parseDouble(cost.getText().toString());
                        if(netBudget - sCost >= 0){
                            netBudget -= sCost;
                            updateNetBudgetText(); //updating net budget text
                            File budgetFile = new File(getExternalFilesDir(filePath), nbFileName);
                            if(budgetFile.exists())
                                budgetFile.delete(); //deleting file to rewrite
                            try(FileOutputStream f = new FileOutputStream(budgetFile,true)){
                                //rewriting budgetFile with new values
                                f.write((netBudget + "\n").getBytes());
                                f.write((String.valueOf(budget)).getBytes());
                            } catch(IOException e){
                                Toast.makeText(MainMenu.this, "ERROR: BUDGET", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainMenu.this, "LOG ERROR, TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }
                            updateBar();
                            updateSurplusText();
                            addToLogs.cancel();
                            //updating bar and surplus text -> closing popup, end of addToLOgs
                        } else {
                            Toast.makeText(MainMenu.this, "COST HIGHER THAN NET BUDGET, TRY AGAIN", Toast.LENGTH_SHORT).show();
                        }
                    } catch(NumberFormatException e){
                        Toast.makeText(MainMenu.this, "INVALID INPUT", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainMenu.this, "MISSING FIELDS", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Log.d("inputlog2", readFile(logFile, -1));
    }

    //file parser for scraping
    String readFile(File file, int line){
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

    //updates net budget text based on value
    @SuppressLint("SetTextI18n")
    private void updateNetBudgetText(){
        DecimalFormat f = new DecimalFormat("#.##"); //decimal format for rounding net budget value to nearest hundred (for cent values)
        f.setRoundingMode(RoundingMode.CEILING);
        netBudgetText.setText(f.format(netBudget) + " " + currency);
    }

    //updates text which appears under net budget value when over the budget
    private void updateSurplusText(){
        TextView surplusText = findViewById(R.id.surplusText);
        surplusText.setText(netBudget > budget ? "+" + (netBudget-budget) : "");
    }

    //updates progress bar
    private void updateBar(){
        ProgressBar bar = findViewById(R.id.netBudgetBar);
        bar.setProgress((int)((netBudget/budget)*100));
    }

}