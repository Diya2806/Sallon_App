package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Customer_FeedBackFragment extends Fragment {
    private AutoCompleteTextView search;
    private EditText feedBack;
    private RatingBar ratingBar;
    private Button btn;
    private String customerId = "";
    private String shopId ="";

    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/FeedBack";
    private String Shop_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner/shoplist";
    private String selectedShopId = "";

    private String selectedShopName = "";
    private boolean isFromHistory = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_feedbackfragment,container,false);
        search = view.findViewById(R.id.shopSearch_feedback);
        feedBack = view.findViewById(R.id.edtComments_feedback);
        ratingBar = view.findViewById(R.id.ratingBar_feedback);
        btn = view.findViewById(R.id.btnSubmit_feedback);

        if (getArguments() != null) {
            customerId = getArguments().getString("CustomerId", "");
            shopId = getArguments().getString("ShopID","");
            selectedShopName = getArguments().getString("ShopName","");
            if (shopId != null && !shopId.isEmpty()) {
                isFromHistory = true;
            }

        }



        if (isFromHistory) {
        search.setText(selectedShopName);
        search.setEnabled(false);
        selectedShopId = shopId;
    } else {

        GetAllShops();
    }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = feedBack.getText().toString().trim();
                float rating = ratingBar.getRating();

                if (selectedShopId.isEmpty()) {
                    Toast.makeText(requireContext(), "Please select a shop", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rating<0.5){
                    Toast.makeText(requireContext(), "Please rating", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (comment.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter feedback", Toast.LENGTH_SHORT).show();
                    return;
                }

                FeedBackPost(customerId, selectedShopId, rating, comment);
            }
        });
        return view;
    }

    void GetAllShops() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Shop_URL, null,
                response -> {
                    try {
                        ArrayList<String> shopNames = new ArrayList<>();
                        ArrayList<String> shopIds = new ArrayList<>();

                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject shop = dataArray.getJSONObject(i);
                            shopIds.add(shop.getString("_id"));
                            shopNames.add(shop.getString("shopName"));
                        }

                        // ✅ Setup dropdown adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                shopNames
                        );
                        search.setAdapter(adapter);

                        // ✅ If opened from Shop Details page
                        if (selectedShopName != null && !selectedShopName.isEmpty()) {
                            for (int i = 0; i < shopNames.size(); i++) {
                                if (shopNames.get(i).equalsIgnoreCase(selectedShopName)) {
                                    selectedShopId = shopIds.get(i);
                                    search.setText(selectedShopName, false);
                                    break;
                                }
                            }

                            if (selectedShopId != null && !selectedShopId.isEmpty()) {
                                Toast.makeText(requireContext(), "Pre-selected shop: " + selectedShopName, Toast.LENGTH_SHORT).show();

                            }
                        }

                        // ✅ When user selects from dropdown
                        search.setOnItemClickListener((parent, view, position, id) -> {
                            selectedShopId = shopIds.get(position);
                            selectedShopName = shopNames.get(position);

                            Toast.makeText(requireContext(),
                                    "Selected shop: " + selectedShopName + "\nID: " + selectedShopId,
                                    Toast.LENGTH_SHORT).show();


                        });

                        // ✅ When user types manually and leaves the box
                        search.setOnFocusChangeListener((v, hasFocus) -> {
                            if (!hasFocus) {
                                String typedName = search.getText().toString().trim();
                                boolean found = false;
                                for (int i = 0; i < shopNames.size(); i++) {
                                    if (shopNames.get(i).equalsIgnoreCase(typedName)) {
                                        selectedShopId = shopIds.get(i);
                                        selectedShopName = shopNames.get(i);
                                        found = true;
                                        break;
                                    }
                                }

                                if (found) {
                                    Toast.makeText(requireContext(),
                                            "Matched shop: " + selectedShopName,
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    selectedShopId = "";
                                    selectedShopName = "";
                                    Toast.makeText(requireContext(),
                                            "Please select a valid shop name!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(requireContext(),
                                "JSON Parse Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(),
                        "Volley Error: " + error.toString(),
                        Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }
    void FeedBackPost(String CustomerId, String ShopId, float rating, String Comment) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("CustomerId", CustomerId);
            jsonBody.put("ShopId", ShopId);
            jsonBody.put("rating", rating);
            jsonBody.put("comment", Comment);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL, jsonBody,
                    response -> {
                        try {
                            String message = response.optString("Message", "Feedback Submitted Successfully!");
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();

                            // Clear fields after submit
                            feedBack.setText("");
                            ratingBar.setRating(0);

                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "Response Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        String errMsg = error.networkResponse != null ?
                                "Error Code: " + error.networkResponse.statusCode :
                                error.toString();
                        Toast.makeText(requireContext(), "Failed to submit feedback: " + errMsg, Toast.LENGTH_LONG).show();
                    });

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (JSONException e) {
            Toast.makeText(requireContext(), "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
