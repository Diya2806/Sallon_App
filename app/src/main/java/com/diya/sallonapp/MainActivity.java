package com.diya.sallonapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button btnlogin;
    private TextView shop,customer,shoplogin,customerlogin;
    private EditText etEmail,etPassword;
    private String url ="https://semicordate-wabbly-veda.ngrok-free.dev/admin";
    private final String PREFS_NAME = "MyAdminPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            btnlogin = findViewById(R.id.btnLogin);
            shop = findViewById(R.id.shopReg);
            shoplogin = findViewById(R.id.shoplog);
            customer =findViewById(R.id.customerReg);
            customerlogin = findViewById(R.id.customerlog);
            etEmail = findViewById(R.id.edtEmail);
            etPassword = findViewById(R.id.edtPassword);

            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
                   // Intent Home = new Intent(MainActivity.this, navigation_menu.class);
                    //startActivity(Home);
                }
            });
            shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ShopReg = new Intent(MainActivity.this, Shop_Register.class);
                    startActivity(ShopReg);
                }
            });
            shoplogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ShopLogin = new Intent(MainActivity.this, Shop_login.class);
                    startActivity(ShopLogin);
                }
            });
            customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent CustomerReg = new Intent(MainActivity.this, Customer_Reg.class);
                    startActivity(CustomerReg);
                }
            });
            customerlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent CustomerLogin = new Intent(MainActivity.this, Customer_Login.class);
                    startActivity(CustomerLogin);
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void attemptLogin() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty()) {
            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }

        // Disable button to prevent double clicks
        btnlogin.setEnabled(false);

        // Request users from backend (assumes the endpoint returns the JSON { Message: "...", List: [ ... ] })
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean matched = false;

                        // If your backend returns "List" array:
                        if (response.has("List")) {
                            JSONArray list = response.getJSONArray("List");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject user = list.getJSONObject(i);
                                String userId = user.optString("_id");
                                String userEmail = user.optString("Email", "");
                                String userPassword = user.optString("Password", ""); // plaintext per your sample

                                if (email.equalsIgnoreCase(userEmail) && password.equals(userPassword)) {
                                    matched = true;
                                    SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("AdminId", userId);
                                    editor.apply();

                                    Intent intent = new Intent(MainActivity.this, navigation_menu.class);
                                    // pass user data if needed:
                                     intent.putExtra("userId", userId);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            }
                        } else {
                            // If API structure differs, try parse differently or show error
                            Toast.makeText(MainActivity.this, "Unexpected server response", Toast.LENGTH_LONG).show();
                        }

                        if (!matched) {
                            Toast.makeText(MainActivity.this, "Enter valid id or password", Toast.LENGTH_LONG).show();
                            btnlogin.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnlogin.setEnabled(true);
                    }
                },
                error -> {
                    // Network or server error
                    Toast.makeText(MainActivity.this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show();
                    btnlogin.setEnabled(true);
                }
        );

        // Add request to queue
        queue.add(request);
    }
}