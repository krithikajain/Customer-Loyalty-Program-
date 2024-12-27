package com.example.loyaltyfirst;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView4, textView5;
    private ImageView imageView;
    private Button allTxns, txnDetail, redmptnDetail, addToFamily, exit;
    private String cid, username;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        setupUI();
        adjustSystemBars();
        retrieveIntentData();
        initializeRequestQueue();
        displayUserInfo();
        setupButtonListeners();
    }

    private void setupUI() {
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        imageView = findViewById(R.id.imageView);
        allTxns = findViewById(R.id.button2);
        txnDetail = findViewById(R.id.button3);
        redmptnDetail = findViewById(R.id.button4);
        addToFamily = findViewById(R.id.button5);
        exit = findViewById(R.id.button6);
    }

    private void adjustSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void retrieveIntentData() {
        username = getIntent().getStringExtra("username");
        cid = getIntent().getStringExtra("cid");
    }

    private void initializeRequestQueue() {
        requestQueue = Volley.newRequestQueue(this);
    }

    private void displayUserInfo() {
        fetchUserImage();
        fetchRewardPoints();
    }

    private void fetchUserImage() {
        String imageUrl = "http://10.0.2.2:8080/Login/images/" + cid + ".jpg";
        Log.d("ImageURL", imageUrl);

        ImageRequest imageRequest = new ImageRequest(imageUrl, bitmap -> imageView.setImageBitmap(bitmap), 800, 800, null, null);
        requestQueue.add(imageRequest);
    }

    private void fetchRewardPoints() {
        String infoUrl = "http://10.0.2.2:8080/Login/Info.jsp?cid=" + cid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, infoUrl, response -> {
            try {
                // Split by # to get only the first part of the response
                String sanitizedResponse = response.trim().split("#")[0];

                // Split by comma to extract name and points
                String[] parts = sanitizedResponse.split(",");
                if (parts.length >= 2) {
                    String fullName = parts[0]; // Extract the name
                    String rewardPoints = parts[1].trim(); // Extract the reward points

                    textView4.setText(fullName);    // Display the name
                    textView5.setText(rewardPoints); // Display the points
                } else {
                    throw new Exception("Invalid response format");
                }
            } catch (Exception e) {
                Log.e("ParseError", "Failed to parse user details: " + e.getMessage());
                Toast.makeText(this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        }, error -> Log.e("RewardPointsError", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void setupButtonListeners() {
        txnDetail.setOnClickListener(view -> navigateToActivity(MainActivity4.class));
        allTxns.setOnClickListener(view -> fetchAllTransactions());
        redmptnDetail.setOnClickListener(view -> navigateToActivity(MainActivity5.class));
        addToFamily.setOnClickListener(view -> navigateToActivity(MainActivity6.class));
        exit.setOnClickListener(view -> navigateToActivity(MainActivity.class));
    }

    private void fetchAllTransactions() {
        String transactionsUrl = "http://10.0.2.2:8080/Login/Transactions.jsp?cid=" + cid;

        StringRequest allTxnsRequest = new StringRequest(Request.Method.GET, transactionsUrl, response -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            intent.putExtra("data", response);
            startActivity(intent);
        }, error -> Log.e("TransactionsError", error.toString()));

        requestQueue.add(allTxnsRequest);
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(MainActivity2.this, targetActivity);
        intent.putExtra("cid", cid);
        startActivity(intent);
    }
}
