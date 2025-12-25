package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import Adapter.Report_adapter;
import Model.Report_model;

public class Reportfragment extends Fragment {
    private ListView listView;
    private Report_adapter adapter;
    private ArrayList<Report_model> ReportList;
    private String url ="https://semicordate-wabbly-veda.ngrok-free.dev/Booking/revenue/monthly-report";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.reportfragment, container, false);
        listView = view.findViewById(R.id.listViewReport);

        ReportList = new ArrayList<>();
        adapter = new Report_adapter(requireContext(),R.layout.custom_report,ReportList);

        //ReportList.add(new Report_model("Hair Cut","25 jun","500"));
        listView.setAdapter(adapter);
fetchMonthlyReport();
        return view;

    }

    private void fetchMonthlyReport() {

        JsonObjectRequest request = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        ReportList.clear();
                        org.json.JSONArray list = response.getJSONArray("List");

                        for (int i = 0; i < list.length(); i++) {
                            org.json.JSONObject obj = list.getJSONObject(i);

                            String shopId = obj.getJSONObject("_id").getString("shopId");
                            String shopName = obj.getString("shopName");
                            String ownerName = obj.getString("ownerName");
                            String totalCustomers = obj.getString("totalCustomers");
                            String totalServices = obj.getString("totalServices");
                            String totalRevenue = obj.getString("totalRevenue");
                            String monthYear = obj.getJSONObject("_id").getString("monthYear");
                            String img = obj.optString("shopImg",null);

                            Report_model report = new Report_model(shopId, shopName, ownerName, totalCustomers, totalServices, totalRevenue, monthYear,img);
                            ReportList.add(report);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        android.widget.Toast.makeText(requireContext(), "Failed to parse data", android.widget.Toast.LENGTH_SHORT).show();
                    }
                },
                error -> android.widget.Toast.makeText(requireContext(), "API Error: " + error.getMessage(), android.widget.Toast.LENGTH_SHORT).show()
        );
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

}
