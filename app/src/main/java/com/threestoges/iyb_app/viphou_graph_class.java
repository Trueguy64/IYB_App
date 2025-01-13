package com.threestoges.iyb_app;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class viphou_graph_class extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viphou_graph_class);

        // Initialize PieChart
        PieChart pieChart = findViewById(R.id.pieChart);

        // Create data entries for the PieChart
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(45f, "Category A"));
        pieEntries.add(new PieEntry(25f, "Category B"));
        pieEntries.add(new PieEntry(20f, "Category C"));
        pieEntries.add(new PieEntry(10f, "Category D"));

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
        description.setText("Pie Chart Example");
        description.setTextSize(12f);
        pieChart.setDescription(description);

        // Refresh PieChart
        pieChart.invalidate();
    }
}