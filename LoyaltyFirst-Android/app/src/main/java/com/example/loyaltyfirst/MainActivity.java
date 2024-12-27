package com.example.loyaltyfirst;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

        loginButton.setOnClickListener(view -> handleLogin());
    }

    private void initializeUI() {
        editTextUsername = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextText2);
        loginButton = findViewById(R.id.button);
    }

    private void handleLogin() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (validateInputs(username, password)) {
            sendLoginRequest(username, password);
        } else {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String username, String password) {
        return !(username.isEmpty() || password.isEmpty());
    }

    private void sendLoginRequest(String username, String password) {
        String url = buildRequestUrl(username, password);
        Log.d("RequestURL", url);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = createStringRequest(url, username);
        queue.add(request);
    }

    private String buildRequestUrl(String username, String password) {
        return "http://10.0.2.2:8080/Login/login?user=" + username + "&pass=" + password;
    }

    private StringRequest createStringRequest(String url, String username) {
        return new StringRequest(Request.Method.GET, url,
                response -> handleResponse(response, username),
                error -> handleError(error));
    }

    private void handleResponse(String response, String username) {
        Log.d("Response", response);
        String result = response.trim();

        if (result.contains("Yes")) {
            navigateToNextActivity(result, username);
        } else {
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(VolleyError error) {
        Log.e("VolleyError", error.toString());
        Toast.makeText(this, "Error: Unable to connect to the server", Toast.LENGTH_SHORT).show();
    }

    private void navigateToNextActivity(String response, String username) {
        String[] parts = response.split("[-\n:]");
        Log.d("Parts", Arrays.toString(parts));

        String cid = parts[1].trim();
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("cid", cid);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
