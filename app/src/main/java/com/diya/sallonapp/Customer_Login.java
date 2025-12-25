package com.diya.sallonapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

public class Customer_Login extends AppCompatActivity {
    private EditText userId,userpass;
    private Button btnlogin;
    private TextView CustomerReg;
    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/customer/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_customer_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;

            });

            userId = findViewById(R.id.customerlogid);
            userpass = findViewById(R.id.customerlogpass);
            btnlogin = findViewById(R.id.btnCustomerlog);
            CustomerReg = findViewById(R.id.CustomerReg);


            CustomerReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Reg = new Intent(Customer_Login.this, Customer_Reg.class);
                    startActivity(Reg);
                }
            });
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = userId.getText().toString().trim();
                    String password = userpass.getText().toString().trim();
                    loginCustomer(email, password);
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void loginCustomer(String email, String password) {
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Enter Email & Password", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "/" + email + "/" + password; // URL with params

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    boolean success = response.optBoolean("success", false);
                    if(success){
                        String userId = response.optString("userId");
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        Intent home = new Intent(Customer_Login.this, Customer_Navigation_menu.class);
                        home.putExtra("customerId", userId);
                        startActivity(home);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errMsg = (error.networkResponse != null) ?
                            new String(error.networkResponse.data) : error.toString();
                    Toast.makeText(this, "Login Failed: " + errMsg, Toast.LENGTH_LONG).show();
                }
        );
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000, // timeout
                0,     // max retries = 0
                1f
        ));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}