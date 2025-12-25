package com.diya.sallonapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.diya.sallonapp.VolleyMultipartRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Shop_Register extends AppCompatActivity {

    private EditText password,name,ownername,moblie,email,address,state,city,area;
    private ScrollView scrollView;
    private TextView login,txtid,txtshop,txtowner;
    private Button btn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_SHOP = 2;
    private static final int PICK_IMAGE_OWNER = 3;
    private Uri imageUri ,shopUri,owneruri;
    private FrameLayout idimg,shopimg,ownerimg;
    private ImageView Idproof,shop,owner;
    private String URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner/register";

    private boolean isSubmitting = false; // prevent double submit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_shop_register);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialize views
            name=findViewById(R.id.regshopname);
            ownername = findViewById(R.id.regshopownername);
            moblie=findViewById(R.id.regshopownernumber);
            email =findViewById(R.id.regshopowneremail);
            address = findViewById(R.id.regshopowneraddress);
            password = findViewById(R.id.regshopownerpass);
            btn = findViewById(R.id.btnShopReg);
            scrollView =findViewById(R.id.shoregscroll);
            login = findViewById(R.id.shopownerlogin);
            state = findViewById(R.id.regshopownerstate);
            city =findViewById(R.id.regshopownercity);
            area =findViewById(R.id.regshopownerarea);
            idimg = findViewById(R.id.Regframidproof);
            Idproof = findViewById(R.id.imgIDProof);
            txtid = findViewById(R.id.textidproof);
            shopimg = findViewById(R.id.Regframshopimg);
            shop = findViewById(R.id.imgShopPhoto);
            txtshop = findViewById(R.id.txtshopimg);
            ownerimg = findViewById(R.id.Regframownerimg);
            owner = findViewById(R.id.imgOwnerPhoto);
            txtowner =findViewById(R.id.txtownerimg);

            // Set default camera drawable size
            Idproof.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Idproof.setAdjustViewBounds(true);
            shop.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            shop.setAdjustViewBounds(true);
            owner.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            owner.setAdjustViewBounds(true);

            // Image click listeners
            idimg.setOnClickListener(v -> selectImage(PICK_IMAGE_REQUEST));
            shopimg.setOnClickListener(v -> selectImage(PICK_IMAGE_SHOP));
            ownerimg.setOnClickListener(v -> selectImage(PICK_IMAGE_OWNER));

            // Login redirect
            login.setOnClickListener(v -> {
                Intent loginIntent = new Intent(Shop_Register.this, Shop_login.class);
                startActivity(loginIntent);
            });

            // Submit button
            btn.setOnClickListener(v -> submitForm());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = selectedUri;
                Idproof.setImageURI(imageUri);
                txtid.setVisibility(View.GONE);
            } else if (requestCode == PICK_IMAGE_SHOP) {
                shopUri = selectedUri;
                shop.setImageURI(shopUri);
                txtshop.setVisibility(View.GONE);
            } else if (requestCode == PICK_IMAGE_OWNER) {
                owneruri = selectedUri;
                owner.setImageURI(owneruri);
                txtowner.setVisibility(View.GONE);
            }
        }
    }

    private void submitForm() {
        if (isSubmitting) return; // prevent double click

        // Trim input values
        String OwnerName = ownername.getText().toString().trim();
        String ShopName = name.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Phone = moblie.getText().toString().trim();
        String Pass = password.getText().toString().trim();
        String Address = address.getText().toString().trim();
        String State = state.getText().toString().trim();
        String City = city.getText().toString().trim();
        String Area = area.getText().toString().trim();

        if (OwnerName.isEmpty() || ShopName.isEmpty() || Email.isEmpty() || Phone.isEmpty() || Pass.isEmpty() ||
                Address.isEmpty() || State.isEmpty() || City.isEmpty() || Area.isEmpty() ||
                imageUri == null || shopUri == null || owneruri == null) {
            Toast.makeText(this, "Please fill all fields and select all images", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button and set submitting flag
        isSubmitting = true;
        btn.setEnabled(false);

        // Call API
        AddData(OwnerName, ShopName, Email, Phone, Pass, Address, State, City, Area, imageUri, shopUri, owneruri);
    }
    private void resetImageView(ImageView iv, TextView txt, String text){
        iv.setImageDrawable(null); // clear selected image

        // Reset TextView
        txt.setVisibility(View.VISIBLE);
        txt.setText(text);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.camera, 0, 0);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(0, 40, 0, 0);
    }
    void AddData(String OwnerName, String ShopName, String Email, String PhoneNumber, String Password,
                 String Address, String State, String City, String Area, Uri imageUri, Uri shopUri, Uri owneruri) {
        try {
            VolleyMultipartRequest request = new VolleyMultipartRequest(
                    Request.Method.POST,
                    URL,
                    response -> {
                        isSubmitting = false;
                        btn.setEnabled(true);
                        try {
                            String respStr = new String(response.data);
                            JSONObject respObj = new JSONObject(respStr);

                            String message = respObj.optString("Message", "No message from server");
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                            // Reset form
                            if(message.equals("Shop registered successfully")) {
                                name.setText("");
                                ownername.setText("");
                                moblie.setText("");
                                email.setText("");
                                address.setText("");
                                password.setText("");
                                state.setText("");
                                city.setText("");
                                area.setText("");
                                resetImageView(Idproof, txtid, "Upload ID");
                                resetImageView(shop, txtshop, "Upload Shop Photo");
                                resetImageView(owner, txtowner, "Upload Owner Photo");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Response parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        isSubmitting = false;
                        btn.setEnabled(true);
                        if (error.networkResponse != null) {
                            String errMsg = new String(error.networkResponse.data);
                            Toast.makeText(this, "Error: " + errMsg, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Ownername", OwnerName);
                    params.put("shopName", ShopName);
                    params.put("email", Email);
                    params.put("password", Password); // backend will hash
                    params.put("phone", PhoneNumber);
                    params.put("address", Address);
                    params.put("state", State);
                    params.put("city", City);
                    params.put("area", Area);
                    params.put("status", "pending");
                    params.put("EntryDate", new java.text.SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> files = new HashMap<>();
                    try {
                        files.put("idProof", new DataPart("id.jpg", getBytesFromUri(imageUri), "image/jpeg"));
                        files.put("shopPhoto", new DataPart("shop.jpg", getBytesFromUri(shopUri), "image/jpeg"));
                        files.put("ownerPhoto", new DataPart("owner.jpg", getBytesFromUri(owneruri), "image/jpeg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Shop_Register.this, "Image upload error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    return files;
                }
            };
            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                    10000, // timeout
                    0,     // max retries = 0
                    1f
            ));

            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } catch (Exception e) {
            isSubmitting = false;
            btn.setEnabled(true);
            Toast.makeText(this, "AddData error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private byte[] getBytesFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
