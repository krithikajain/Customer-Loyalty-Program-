package com.example.loyaltyfirst;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {

    private Spinner prizeSpinner;
    private TextView prizeDescriptionView, pointsNeededView, additionalInfoView;
    private String cid;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        initializeUI();
        fetchPrizeIds();
    }

    private void initializeUI() {
        prizeSpinner = findViewById(R.id.spinner2);
        prizeDescriptionView = findViewById(R.id.textView14);
        pointsNeededView = findViewById(R.id.textView16);
        additionalInfoView = findViewById(R.id.textView17);
        cid = getIntent().getStringExtra("cid");
        requestQueue = Volley.newRequestQueue(this);

        setupSpinnerListener();
    }

    private void fetchPrizeIds() {
        String prizeIdsUrl = "http://10.0.2.2:8080/Login/PrizeIds.jsp?cid=" + cid;

        StringRequest prizeIdsRequest = new StringRequest(Request.Method.GET, prizeIdsUrl,
                this::populateSpinner,
                error -> prizeDescriptionView.setText("Error fetching prize IDs"));

        requestQueue.add(prizeIdsRequest);
    }

    private void populateSpinner(String response) {
        ArrayList<String> prizeIds = new ArrayList<>();
        String[] rows = response.split("#");

        for (String row : rows) {
            String[] columns = row.split(",");
            if (columns.length > 0) {
                prizeIds.add(columns[0]); // Assuming the first column is the prize ID
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prizeIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prizeSpinner.setAdapter(adapter);
    }

    private void setupSpinnerListener() {
        prizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPrizeId = parent.getItemAtPosition(position).toString();
                fetchPrizeDetails(selectedPrizeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayDefaultMessage();
            }
        });
    }

    private void fetchPrizeDetails(String prizeId) {
        String prizeDetailsUrl = "http://10.0.2.2:8080/Login/RedemptionDetails.jsp?prizeid=" + prizeId + "&cid=" + cid;

        StringRequest prizeDetailsRequest = new StringRequest(Request.Method.GET, prizeDetailsUrl,
                this::displayPrizeDetails,
                error -> prizeDescriptionView.setText("Error fetching prize details"));

        requestQueue.add(prizeDetailsRequest);
    }

    private void displayPrizeDetails(String response) {
        try {
            // Split response by "#" and take the first segment
            String[] transactions = response.split("#");

            if (transactions.length > 0) {
                // Split the first transaction into its components
                String[] details = transactions[0].split(",");

                if (details.length >= 4) {
                    // Map the details to the respective TextViews
                    prizeDescriptionView.setText("Prize Description: " + details[0]); // Prize Description
                    pointsNeededView.setText(details[1]);        // Points Needed
                    additionalInfoView.setText(String.format("Date: %s\nLocation: %s", details[2], details[3])); // Date and Additional Info
                } else {
                    // Handle insufficient data
                    prizeDescriptionView.setText("Insufficient details available.");
                    pointsNeededView.setText("");
                    additionalInfoView.setText("");
                }
            } else {
                // Handle empty response
                prizeDescriptionView.setText("No prize details available.");
                pointsNeededView.setText("");
                additionalInfoView.setText("");
            }
        } catch (Exception e) {
            // Handle any parsing or runtime exceptions
            prizeDescriptionView.setText("Error parsing prize details.");
            pointsNeededView.setText("");
            additionalInfoView.setText("");
        }
    }

    private void displayDefaultMessage() {
        prizeDescriptionView.setText("Please select a prize ID.");
        pointsNeededView.setText("");
        additionalInfoView.setText("");
    }
}
