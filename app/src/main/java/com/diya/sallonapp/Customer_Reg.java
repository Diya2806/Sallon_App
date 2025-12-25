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
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Customer_Reg extends AppCompatActivity {
    private EditText name,number,address,email,pass;
    private Button btnReg;
    private TextView Customerlogin;
    String URL ="https://semicordate-wabbly-veda.ngrok-free.dev/customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_customer_reg);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            name= findViewById(R.id.CustomerRegName);
            number = findViewById(R.id.CustomerRegNumber);
            email = findViewById(R.id.CustomerRegEmail);
            address = findViewById(R.id.CustomerRegAddress);
            pass = findViewById(R.id.CustomerRegPass);
            Customerlogin = findViewById(R.id.CustomerLogin);
            btnReg = findViewById(R.id.btnCustomerReg);


            Customerlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent login = new Intent(Customer_Reg.this, Customer_Login.class);
                    startActivity(login);
                }
            });
            btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sName = name.getText().toString().trim();
                    String sNumber = number.getText().toString().trim();
                    String sEmail = email.getText().toString().trim();
                    String sAddress = address.getText().toString().trim();
                    String sPass = pass.getText().toString().trim();

                    if (sName.isEmpty() || sNumber.isEmpty() || sEmail.isEmpty() || sAddress.isEmpty() || sPass.isEmpty()) {
                        Toast.makeText(Customer_Reg.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        registerCustomer(sName, sEmail, sNumber, sAddress, sPass);
                        name.setText("");
                        email.setText("");
                        number.setText("");
                        address.setText("");
                        pass.setText("");
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void registerCustomer(String name, String email, String phone, String address, String password) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Customername", name);
            jsonBody.put("email", email);
            jsonBody.put("phone", phone);
            jsonBody.put("address", address);
            jsonBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                    response -> {
                        Toast.makeText(Customer_Reg.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(Customer_Reg.this, Customer_Navigation_menu.class));
                    },
                    error -> {
                        try {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                String body = new String(error.networkResponse.data, "UTF-8");
                                JSONObject obj = new JSONObject(body);
                                Toast.makeText(Customer_Reg.this, obj.getString("Message"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Customer_Reg.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}