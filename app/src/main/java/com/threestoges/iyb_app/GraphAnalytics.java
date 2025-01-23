package com.threestoges.iyb_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphAnalytics extends AppCompatActivity {
    private final String filePath = "user";
    private final String fileName = "logs.txt";
    private final List<String> logReader = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graph_analytics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize PieChart
        PieChart pieChart = findViewById(R.id.pieChart);

        // Reading logFile into logs
        File logFile = new File(getExternalFilesDir(filePath), fileName);
        if(logFile.exists()){
            try{
                BufferedReader r = new BufferedReader(new FileReader(logFile));
                String q;
                while((q = r.readLine()) != null){
                    logReader.add(q);
                }
                r.close();
            } catch(IOException e){
                Toast.makeText(this, "LOG DATA STARTUP FAILED", Toast.LENGTH_SHORT).show();
            }
        }

        // Extracting data from expenditure logs into a HashMap
        Map<String, Double> logMap = new HashMap<>();
        convert(logReader, logMap);

        // Get total spending for denominator
        double total = 0;
        for(String q: logMap.keySet()){
            total += logMap.get(q);
        }
        //Log.d("inputlog", String.valueOf(total));

        // Create data entries for the PieChart
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for(String q: logMap.keySet()){
            pieEntries.add(new PieEntry(Float.parseFloat(String.valueOf(logMap.get(q))), q));
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent back = new Intent(GraphAnalytics.this, MainMenu.class);
                startActivity(back);
                finish();
            }
        });

        // Check if pieEntries is populated
        if (pieEntries.isEmpty()) {
            pieChart.setNoDataText("No data available!");
            return;
        }

        // Customize PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Categories");
        pieDataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW});
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(14f);

        // Create PieData and set it to the PieChart
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // Customize PieChart
        pieChart.setUsePercentValues(true); // Show percentages
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Category Breakdown");
        pieChart.setCenterTextSize(18f);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);

        // Add Description (optional)
        Description description = new Description();
        description.setText("Your Category Spending Pie Chart");
        description.setTextSize(12f);
        pieChart.setDescription(description);

        // Refresh PieChart
        pieChart.invalidate();
    }

    // Method to convert list to hashmap
    private void convert(List<String> list, Map<String, Double> map){
        String category = "";
        Double val = 0d;
        int counter = 0;
        int start = 0;
        int end = 0;
        for(String q: list){
            // Getting start and end indexes for category string
            for(int r = 0; r < q.length(); r++){
                if(q.charAt(r) == ','){
                    counter++;
                    start = (counter == 2 ? r+1 : start);
                    end = (counter == 3 ? r : end);
                }
            }
            category = q.substring(start, end);
            counter = 0;
            // Getting start and end indexes for cost string
            for(int r = 0; r < q.length(); r++){
                if(q.charAt(r) == ','){
                    counter++;
                    start = (counter == 3 ? r+1 : start);
                    end = (counter == 4 ? r : end);
                }
            }
            try{
                val = Double.parseDouble(q.substring(start, end));
            } catch(NumberFormatException e){
                Toast.makeText(this, "FAILED TO MAP LOGS", Toast.LENGTH_SHORT).show();
            }
            if(!map.containsKey(category)){ // Checking for duplicates
                map.put(category, val);
            } else {
                map.replace(category, map.get(category), val+map.get(category));
            }
            counter = 0;
        }
        //Log.d("inputlog", map.toString());
    }
}