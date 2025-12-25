package com.diya.sallonapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class Shop_Reg_profilefragment extends Fragment {

private ImageView  imgShopPhoto;
private TextView tvShopName, tvOwnerName,tvPhone,tvEmail,tvAddress;
private Button edit,logout;
private EditText tvShopBio;
    private final String PREFS_NAME = "MyAppPrefs";
    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shop_reg_profilefragment,container,false);


        imgShopPhoto = view.findViewById(R.id.imgShopOwner);
        tvOwnerName = view.findViewById(R.id.shopprofilename);
        tvPhone = view.findViewById(R.id.shopprofilenumber);
        tvEmail = view.findViewById(R.id.shopprofileEmail);
        tvAddress = view.findViewById(R.id.shopprofileAddress);
        tvShopName = view.findViewById(R.id.tvprofileShopName);
        edit = view.findViewById(R.id.btnEditProfile);
        logout = view.findViewById(R.id.btnLogout);
        tvShopBio = view.findViewById(R.id.tvShopBio);


        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        String shopId = prefs.getString("shopId", "");

        if (!shopId.isEmpty()) {
            fetchShopData(shopId);
        } else {
            Toast.makeText(requireContext(), "Shop ID not found. Please log in again.", Toast.LENGTH_LONG).show();
        }


        edit.setOnClickListener(v -> {
            if (edit.getText().toString().equals("Edit Profile")) {
                // Enable bio editing
                tvShopBio.setEnabled(true);
                tvShopBio.setFocusable(true);
                tvShopBio.setFocusableInTouchMode(true);
                tvShopBio.requestFocus();
                tvShopBio.setBackgroundResource(R.drawable.input);
                edit.setText("Save");
            } else { // Save action
                String updatedBio = tvShopBio.getText().toString().trim();
                saveBio(updatedBio);
            }
        });
        logout.setOnClickListener(v -> {
            // Clear SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Optional: show a logout message
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to login activity and clear back stack
            Intent intent = new Intent(requireContext(), Shop_login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });




        return view;
    }


    private void fetchShopData(String shopId) {
        String url = BASE_URL + "/" + shopId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("Data");

                        tvOwnerName.setText(data.getString("Ownername"));
                        tvShopName.setText(data.getString("shopName"));
                        tvPhone.setText(data.getString("phone"));
                        tvEmail.setText(data.getString("email"));
                        String bio = data.optString("bio", "");
                        tvShopBio.setText(bio);

                        String fullAddress = data.optString("address", "") + ", " +
                                data.optString("area", "") + ", " +
                                data.optString("city", "") + ", " +
                                data.optString("state", "");
                        tvAddress.setText(fullAddress);

                        // Load shop image
                        String shopImg = data.optString("shopPhoto", "");
                        if (!shopImg.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(shopImg)
                                    .placeholder(R.drawable.person) // fallback image
                                    .circleCrop() // ✅ ensures the image fits the circle
                                    .into(imgShopPhoto);
                        }

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Data parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Volley error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }


    private void saveBio(String bio) {
        String shopId = requireContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE)
                .getString("shopId", "");

        if (shopId.isEmpty()) return;

        String url = BASE_URL + "/update-bio/" + shopId;

        JSONObject body = new JSONObject();
        try {
            body.put("bio", bio);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                response -> {
                    Toast.makeText(requireContext(), "Bio updated successfully!", Toast.LENGTH_SHORT).show();
                    tvShopBio.setEnabled(false);
                    tvShopBio.setBackgroundResource(android.R.color.transparent);// make it read-only again
                    edit.setText("Edit Profile");
                },
                error -> Toast.makeText(requireContext(), "Failed to update bio", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

}
