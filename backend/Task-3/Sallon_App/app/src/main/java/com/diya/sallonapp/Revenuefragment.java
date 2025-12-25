package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.Revenue_adapter;
import Model.Revenue_model;

public class Revenuefragment extends Fragment {
    private ListView listView;
    private Revenue_adapter adapter;
    private ArrayList<Revenue_model> RevenueList;
    private String BASE_URL ="https://semicordate-wabbly-veda.ngrok-free.dev/Booking/revenue/all";
    private int today,month,total;
    String currentdate ="";
    String currentMonth ="";
    private TextView todayrev,monthrev,totalrev;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.revenuefragment, container, false);

        listView =view.findViewById(R.id.listviewRevenue);
        todayrev = view.findViewById(R.id.todayRevenue);
        monthrev = view.findViewById(R.id.monthRevenue);
        totalrev = view.findViewById(R.id.totalRevenue);
        Date current = new Date();
        SimpleDateFormat datestr = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");

        currentdate = datestr.format(current);
        currentMonth = monthFormat.format(current);

        RevenueList = new ArrayList<>();
        adapter = new Revenue_adapter(requireContext(),R.layout.custome_revenue,RevenueList);
        listView.setAdapter(adapter);
        loadRevenueData();

        return view;

    }
    private void loadRevenueData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                response -> {
                    today=0;
                    month = 0;
                    total=0;
                    RevenueList.clear();
                    try {
                        JSONArray array = response.getJSONArray("List");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String ownerName = obj.optString("ownerName", "Unknown");
                            String shopName = obj.optString("shopName", "Unknown");
                            String todayRevenue = obj.optString("todayRevenue", "0");
                            String monthRevenue = obj.optString("monthRevenue", "0");
                            String totalRevenue = obj.optString("totalRevenue","0");
                            String img = obj.optString("shopImg",null);
                            today += Integer.parseInt(todayRevenue);
                            month += Integer.parseInt(monthRevenue);
                            total += Integer.parseInt(totalRevenue);
                            RevenueList.add(new Revenue_model(ownerName, shopName, todayRevenue, monthRevenue,img));
                        }

                        todayrev.setText("₹ "+String.valueOf(today));
                        monthrev.setText("₹ "+String.valueOf(month));
                        totalrev.setText("₹ "+String.valueOf(total));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}

