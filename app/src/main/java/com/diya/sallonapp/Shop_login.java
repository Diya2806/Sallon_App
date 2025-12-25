package com.diya.sallonapp;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Shop_login extends AppCompatActivity {
    private TextView reg,forget;
    private EditText shopid,pass;
    private Button btn;
    private  String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner/login";
    private final String PREFS_NAME = "MyAppPrefs";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_shop_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            reg = findViewById(R.id.shopownerReg);
            shopid = findViewById(R.id.shoplogid);
            pass = findViewById(R.id.shoplogpass);
            btn = findViewById(R.id.btnShoplog);
            forget=findViewById(R.id.ShopForget);

            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Reg = new Intent(Shop_login.this, Shop_Register.class);
                    startActivity(Reg);
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = shopid.getText().toString().trim();
                    String password = pass.getText().toString().trim();
                    checkLogin(email, password);
                }
            });
            forget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forgot = new Intent(Shop_login.this, Forgot_password.class);
                    startActivity(forgot);
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void checkLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            String url = BASE_URL + "/" + email + "/" + password;

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null, // GET request does not need a body
                    response -> {
                        try {
                            boolean success = response.optBoolean("success", false);
                            if (success) {
                                Toast.makeText(Shop_login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                String userId = response.optString("userId");

                                // Save ShopId in SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("shopId", userId);
                                editor.apply();

                                Intent home = new Intent(Shop_login.this, Shop_navigation_menu.class);
                                home.putExtra("userId", userId);
                                startActivity(home);
                                finish();
                            } else {
                                Toast.makeText(Shop_login.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Shop_login.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        String errMsg = (error.networkResponse != null) ?
                                new String(error.networkResponse.data) : error.toString();
                        Toast.makeText(Shop_login.this, "Login Failed: " + errMsg, Toast.LENGTH_LONG).show();
                    }
            );



            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}