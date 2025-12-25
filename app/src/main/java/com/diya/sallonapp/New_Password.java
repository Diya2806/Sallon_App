package com.diya.sallonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class New_Password extends AppCompatActivity {
    private EditText newpass,conformpass;
    private Button btnsave;
    private  String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner/newpass/";

    // get userId from previous activity (via Intent)
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_new_password);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            newpass = findViewById(R.id.newpass);
            conformpass = findViewById(R.id.conformpass);
            btnsave = findViewById(R.id.btnnewpass);

            userId = getIntent().getStringExtra("userId");

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String NewPass = newpass.getText().toString().trim();
                    String ConPass = conformpass.getText().toString().trim();

                    if (ConPass.equals(NewPass)){
                        Toast.makeText(New_Password.this, "Save New password", Toast.LENGTH_SHORT).show();
                        UpdatePass(NewPass);
                        Intent home = new Intent(New_Password.this,Shop_login.class);
                        startActivity(home);
                    }
                    else{
                        Toast.makeText(New_Password.this, "New Password And Conform Password Not Same Enter Same ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void UpdatePass(String newPassword) {
        try {
            String url = BASE_URL + userId;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("password", newPassword);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    jsonBody,
                    response -> {
                        Toast.makeText(New_Password.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // close activity
                    },
                    error -> {
                        Toast.makeText(New_Password.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
            );

            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}