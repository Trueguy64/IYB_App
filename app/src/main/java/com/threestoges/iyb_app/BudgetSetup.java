package com.threestoges.iyb_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class BudgetSetup extends AppCompatActivity {
    private double budget = 1;
    private final String filePath = "user";
    private final String nbFileName = "net-budget.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button proceed = findViewById(R.id.proceedButton);
        EditText budgetInput = findViewById(R.id.budgetInputText);
        proceed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!String.valueOf(budgetInput.getText()).isEmpty()){
                    try{
                        budget = Double.parseDouble(budgetInput.getText().toString());
                        double netBudget = budget;
                        File budgetFile = new File(getExternalFilesDir(filePath), nbFileName);
                        if(budgetFile.exists()){
                            try(BufferedReader r = new BufferedReader(new FileReader(budgetFile))){
                                netBudget = Double.parseDouble(r.readLine());
                                r.close();
                            } catch(IOException e){
                                Toast.makeText(BudgetSetup.this, "ERROR: BUFFEREDREADER", Toast.LENGTH_SHORT).show();
                            }
                            budgetFile.delete();
                            try(FileOutputStream f = new FileOutputStream(budgetFile,true)){
                                f.write((netBudget + "\n").getBytes());
                                f.write(String.valueOf(budget).getBytes());
                            } catch(IOException e){
                                Toast.makeText(BudgetSetup.this, "ERROR: FILEOUTPUTSTREAM", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            try(FileOutputStream f = new FileOutputStream(budgetFile,true)){
                                f.write((netBudget + "\n").getBytes());
                                f.write(String.valueOf(budget).getBytes());
                            } catch(IOException e){
                                Toast.makeText(BudgetSetup.this, "ERROR: FILEOUTPUTSTREAM", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //Log.d("inputlog", readFile(budgetFile, 1));
                        Intent mainMenu = new Intent(BudgetSetup.this, MainMenu.class);
                        startActivity(mainMenu);
                        finish();
                    } catch(NumberFormatException e){
                        Toast.makeText(BudgetSetup.this, "INVALID INPUT", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(BudgetSetup.this, "EMPTY FIELD", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}