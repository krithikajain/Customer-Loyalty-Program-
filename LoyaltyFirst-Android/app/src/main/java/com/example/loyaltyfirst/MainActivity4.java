package com.example.loyaltyfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Initialize UI elements
        Spinner spinner = findViewById(R.id.spinner);
        TextView textView11 = findViewById(R.id.textView11);
        TextView textView10 = findViewById(R.id.textView10); // To display the date
        TextView textView9 = findViewById(R.id.textView9);   // To display the total points

        // Retrieve customer ID from Intent
        String cid = getIntent().getStringExtra("cid");
        if (cid == null || cid.isEmpty()) {
            Toast.makeText(this, "Customer ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL to fetch all transactions
        String transactionUrl = "http://10.0.2.2:8080/Login/Transactions.jsp?cid=" + cid;

        // Request queue for network operations
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Fetch transaction references and populate spinner
        StringRequest transactionRequest = new StringRequest(Request.Method.GET, transactionUrl,
                response -> {
                    try {
                        String[] rows = response.trim().split("#");
                        ArrayList<String> transactionReferences = new ArrayList<>();

                        // Populate transaction references
                        for (String row : rows) {
                            String[] columns = row.split(",");
                            if (columns.length > 0) {
                                transactionReferences.add(columns[0]); // Assuming first column is the transaction ID (tref)
                            }
                        }

                        // Update spinner with transaction references
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity4.this, android.R.layout.simple_spinner_item, transactionReferences);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        // Handle spinner item selection
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedTransaction = parent.getItemAtPosition(position).toString();
                                fetchTransactionDetails(selectedTransaction, textView11, textView10, textView9, requestQueue);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                textView11.setText("Please select a transaction.");
                            }
                        });

                    } catch (Exception e) {
                        textView11.setText("Error parsing transactions: " + e.getMessage());
                    }
                },
                error -> Toast.makeText(MainActivity4.this, "Error fetching transactions: " + error.getMessage(), Toast.LENGTH_SHORT).show());
        requestQueue.add(transactionRequest);
    }

    // Method to fetch details of a specific transaction
    private void fetchTransactionDetails(String transactionId, TextView textView11, TextView textView10, TextView textView9, RequestQueue requestQueue) {
        String detailUrl = "http://10.0.2.2:8080/Login/TransactionDetails.jsp?tref=" + transactionId;

        StringRequest detailRequest = new StringRequest(Request.Method.GET, detailUrl,
                response -> {
                    try {
                        // Debug: Log raw server response
                        System.out.println("Raw Response: " + response.trim());

                        // Split response into individual rows
                        String[] transactions = response.trim().split("#");

                        // Initialize a StringBuilder to build the output
                        StringBuilder details = new StringBuilder();
                        details.append("Transaction ID: ").append(transactionId).append("\n\n");

                        // Variables to store the date and total points
                        String date = null;
                        int totalPoints = 0;

                        // Iterate through transactions and append product details
                        for (String transaction : transactions) {
                            String[] values = transaction.split(",");
                            if (values.length >= 6 && values[0].equals(transactionId)) {
                                // Capture the date (same for all rows in the transaction)
                                if (date == null) {
                                    date = values[1]; // Set the date from the first row
                                }

                                // Add points to the total
                                totalPoints += Integer.parseInt(values[2]);

                                details.append("Date: ").append(values[1]).append("\n")
                                        .append("Points: ").append(values[2]).append("\n")
                                        .append("Product: ").append(values[3]).append("\n")
                                        .append("Product Points: ").append(values[4]).append("\n")
                                        .append("Quantity: ").append(values[5]).append("\n\n");
                            }
                        }

                        // Check if details were added
                        if (details.length() > 0) {
                            textView11.setText(details.toString());
                            textView10.setText(date != null ? date : "No Date Found"); // Display date
                            textView9.setText(String.valueOf(totalPoints)); // Display total points
                        } else {
                            textView11.setText("No details found for the selected transaction.");
                            textView10.setText(""); // Clear the date
                            textView9.setText("");  // Clear the total points
                        }

                    } catch (Exception e) {
                        textView11.setText("Error parsing transaction details: " + e.getMessage());
                    }
                },
                error -> textView11.setText("Error fetching transaction details: " + error.getMessage()));
        requestQueue.add(detailRequest);
    }
}
