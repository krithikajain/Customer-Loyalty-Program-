package com.example.loyaltyfirst;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity6 extends AppCompatActivity {

    private Spinner transactionSpinner;
    private TextView transactionPointsView, familyIdView, familyPercentageView;
    private Button addFamilyPointsButton;
    private String cid;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        initializeUI();
        fetchTransactions();
    }

    private void initializeUI() {
        transactionSpinner = findViewById(R.id.spinner3);
        transactionPointsView = findViewById(R.id.textView22); // For transaction points
        familyIdView = findViewById(R.id.textView23);          // For family ID
        familyPercentageView = findViewById(R.id.textView24);  // For family percentage
        addFamilyPointsButton = findViewById(R.id.button7);
        cid = getIntent().getStringExtra("cid");
        requestQueue = Volley.newRequestQueue(this);
    }

    private void fetchTransactions() {
        String transactionUrl = "http://10.0.2.2:8080/Login/Transactions.jsp?cid=" + cid;

        StringRequest transactionsRequest = new StringRequest(Request.Method.GET, transactionUrl,
                this::populateSpinner,
                error -> Toast.makeText(this, "Error fetching transactions", Toast.LENGTH_SHORT).show());

        requestQueue.add(transactionsRequest);
    }

    private void populateSpinner(String response) {
        ArrayList<String> transactionReferences = new ArrayList<>();
        String[] rows = response.split("#");

        for (String row : rows) {
            String[] columns = row.split(",");
            if (columns.length > 0) {
                transactionReferences.add(columns[0]); // Assuming first column is the transaction reference
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transactionReferences);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionSpinner.setAdapter(adapter);

        setupSpinnerListener();
    }

    private void setupSpinnerListener() {
        transactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTransaction = parent.getItemAtPosition(position).toString();
                Log.d("SelectedTransaction", selectedTransaction);
                fetchTransactionDetails(selectedTransaction);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void fetchTransactionDetails(String transactionReference) {
        String detailsUrl = "http://10.0.2.2:8080/Login/SupportFamilyIncrease.jsp?tref=" + transactionReference + "&cid=" + cid;

        StringRequest detailsRequest = new StringRequest(Request.Method.GET, detailsUrl,
                response -> {
                    Log.d("TransactionDetails", response.trim());
                    displayTransactionDetails(response, transactionReference);
                },
                error -> Toast.makeText(this, "Error fetching transaction details", Toast.LENGTH_SHORT).show());

        requestQueue.add(detailsRequest);
    }

    private void displayTransactionDetails(String response, String transactionReference) {
        String result = response.trim();
        String[] lines = result.split("#");
        int totalPoints = 0;

        String familyId = "";
        String familyPercentage = "";

        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length >= 3) {
                // Add the points (values[2]) for each row to the total
                totalPoints += Integer.parseInt(values[2]);

                // Capture Family ID and Percentage from the first row
                if (familyId.isEmpty() && familyPercentage.isEmpty()) {
                    familyId = values[0];         // Family ID
                    familyPercentage = values[1]; // Family Percentage
                }
            }
        }

        // Update the UI with the fetched data
        transactionPointsView.setText(String.valueOf(totalPoints)); // Total transaction points
        familyIdView.setText(familyId);                             // Family ID
        familyPercentageView.setText(familyPercentage);             // Family Percentage

        setupAddFamilyPointsButton(totalPoints, familyId, familyPercentage);
    }

    private void setupAddFamilyPointsButton(int totalPoints, String familyId, String familyPercentage) {
        addFamilyPointsButton.setOnClickListener(v -> {
            try {
                float familyPercentageFloat = Float.parseFloat(familyPercentage);

                // Calculate points to add
                float pointsToAdd = (familyPercentageFloat / 100) * totalPoints;
                int pointsToAddInt = Math.round(pointsToAdd);

                addFamilyPoints(familyId, pointsToAddInt, totalPoints + pointsToAddInt, familyPercentage);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid family percentage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFamilyPoints(String familyId, int pointsToAdd, int newTotalPoints, String familyPercentage) {
        String pointsUrl = "http://10.0.2.2:8080/Login/FamilyIncrease.jsp?fid=" + familyId + "&cid=" + cid + "&npoints=" + pointsToAdd;

        StringRequest addPointsRequest = new StringRequest(Request.Method.GET, pointsUrl,
                response -> {
                    String message = String.format("%d Points added to Family ID %s with %s%% added! New total points: %d",
                            pointsToAdd, familyId, familyPercentage, newTotalPoints);
                    Toast.makeText(MainActivity6.this, message, Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(MainActivity6.this, "Error adding family points", Toast.LENGTH_SHORT).show());

        requestQueue.add(addPointsRequest);
    }
}
