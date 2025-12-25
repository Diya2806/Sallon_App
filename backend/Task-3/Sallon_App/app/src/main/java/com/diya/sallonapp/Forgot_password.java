package com.diya.sallonapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Forgot_password extends AppCompatActivity {
    private EditText edtEmail, otp1, otp2, otp3, otp4, otp5, otp6;
    private Button btnCheck, btnVerifyOTP;
    private LinearLayout otpLayout;
    private String OTP_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner/send-otp";
    private String sentOTP = "";
    private String userId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_forgot_password);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            edtEmail = findViewById(R.id.forgotemail);
            btnCheck = findViewById(R.id.btnforgot);
            otpLayout = findViewById(R.id.otpLayout);
            btnVerifyOTP = findViewById(R.id.btnVerifyOTP);

            otp1 = findViewById(R.id.otp1);
            otp2 = findViewById(R.id.otp2);
            otp3 = findViewById(R.id.otp3);
            otp4 = findViewById(R.id.otp4);
            otp5 = findViewById(R.id.otp5);
            otp6 = findViewById(R.id.otp6);

            setupOTPInputs();

            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edtEmail.getText().toString().trim();
                    if(email.isEmpty()){
                        Toast.makeText(Forgot_password.this, "Enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendOTP(email);
                }
            });
            btnVerifyOTP.setOnClickListener(v -> verifyOTP());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupOTPInputs() {
        EditText[] otpFields = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() == 1 && index < otpFields.length - 1){
                        otpFields[index + 1].requestFocus();
                    } else if(s.length() == 0 && index > 0){
                        otpFields[index - 1].requestFocus();
                    }
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }
    }
    private void sendOTP(String email) {
        try {
            btnCheck.setEnabled(false);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    OTP_URL,
                    jsonBody,
                    response -> {
                        btnCheck.setEnabled(true);
                        Toast.makeText(Forgot_password.this, "OTP sent to email!", Toast.LENGTH_SHORT).show();

                        // Show OTP input fields and Verify button
                        otpLayout.setVisibility(View.VISIBLE);
                        btnVerifyOTP.setVisibility(View.VISIBLE);
                        otp1.requestFocus();

                        // If backend sends the OTP in response (for testing), save it
                        sentOTP = response.optString("OTP", "");
                        userId = response.optString("userId", "");
                    },
                    error -> {
                        btnCheck.setEnabled(true);
                        if(error.networkResponse != null && error.networkResponse.statusCode == 404){
                            Toast.makeText(Forgot_password.this, "Email not found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Forgot_password.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                    10000, // timeout
                    0,     // max retries = 0
                    1f
            ));

            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } catch (Exception e){
            btnCheck.setEnabled(true);
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOTP() {
        String enteredOTP = otp1.getText().toString().trim() +
                otp2.getText().toString().trim() +
                otp3.getText().toString().trim() +
                otp4.getText().toString().trim() +
                otp5.getText().toString().trim() +
                otp6.getText().toString().trim();

        if(enteredOTP.isEmpty() || enteredOTP.length() < 6){
            Toast.makeText(this, "Enter complete OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if(enteredOTP.equals(sentOTP)){
            Toast.makeText(this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();
            Intent newPassword = new Intent(Forgot_password.this,New_Password.class);
            newPassword.putExtra("userId", userId);

            startActivity(newPassword);
        } else {
            Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
        }
    }
}