package com.example.loyaltyfirst;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity3 extends AppCompatActivity {

    private TextView titleView, transactionsView;
    private static final String TAG = "MainActivity3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initializeUI();
        setTitleText("Transactions");
        displayTransactionData(getIntent().getStringExtra("data"));
    }

    private void initializeUI() {
        titleView = findViewById(R.id.textView7);
        transactionsView = findViewById(R.id.textView25);
    }

    private void setTitleText(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    private void displayTransactionData(String inputData) {
        if (isValidData(inputData)) {
            transactionsView.setText(formatTransactionData(inputData));
        } else {
            Log.e(TAG, "No transaction data received.");
            transactionsView.setText("No transaction data available.");
        }
    }

    private boolean isValidData(String inputData) {
        return inputData != null && !inputData.trim().isEmpty();
    }

    private String formatTransactionData(String inputData) {
        StringBuilder formattedOutput = new StringBuilder();
        String[] rows = inputData.trim().split("#");

        for (String row : rows) {
            String[] columns = extractColumnsFromRow(row);
            Log.d(TAG, "Extracted Columns: " + Arrays.toString(columns));
            formattedOutput.append(String.format("%-12s %-20s %-10s %-15s\n",
                    columns[0], columns[1], columns[2], columns[3]));
        }
        return formattedOutput.toString();
    }

    private String[] extractColumnsFromRow(String row) {
        String[] columns = new String[4];
        if (row.length() >= 30) {
            columns[0] = row.substring(0, 4).trim();
            columns[1] = row.substring(5, 25).trim();
            columns[2] = row.substring(26, 30).trim();
            columns[3] = row.substring(31).trim();
        } else {
            Log.e(TAG, "Row too short: " + row);
            Arrays.fill(columns, "Incomplete data");
        }
        return columns;
    }
}