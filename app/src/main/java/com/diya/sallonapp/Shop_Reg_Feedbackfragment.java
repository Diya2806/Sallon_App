package com.diya.sallonapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ShopFeedback_adapter;
import Model.ShopFeedback_model;

public class Shop_Reg_Feedbackfragment extends Fragment {
    private ListView listView;
    private ShopFeedback_adapter adapter;
    private ArrayList<ShopFeedback_model> FeedbackList;
    private String apiUrl = "https://semicordate-wabbly-veda.ngrok-free.dev/FeedBack/shop/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.sho_reg_feedbackfragment,container,false);
        listView = view.findViewById(R.id.feedbackListView);

        FeedbackList = new ArrayList<>();

        adapter = new ShopFeedback_adapter(requireContext(),R.layout.custome_shop_feedback,FeedbackList);
        listView.setAdapter(adapter);
        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", requireContext().MODE_PRIVATE);
        String shopId = sharedPref.getString("shopId", null);

        if (shopId != null) {
            fetchFeedback(shopId);
        } else {
            Toast.makeText(requireContext(), "Shop ID not found. Please login again.", Toast.LENGTH_SHORT).show();
        }


        return view;
    }
    private void fetchFeedback(String shopId) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl+shopId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray dataArray = response.getJSONArray("data");

                                FeedbackList.clear(); // Clear old data

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);

                                    float rating = (float) obj.getDouble("rating");
                                    String comment = obj.getString("comment");
                                    String entryDate = obj.getString("EntryDate");

                                    JSONObject customer = obj.getJSONObject("CustomerData");
                                    String name = customer.getString("Customername");
                                    String image = customer.optString("image", ""); // optional image

                                    ShopFeedback_model feedback = new ShopFeedback_model(name, comment,image,rating);
                                    FeedbackList.add(feedback);
                                }

                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(requireContext(), "No feedback found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "API Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }
}
