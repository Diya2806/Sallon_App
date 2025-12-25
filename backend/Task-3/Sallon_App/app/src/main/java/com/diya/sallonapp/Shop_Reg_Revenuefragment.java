package com.diya.sallonapp;

import android.content.SharedPreferences;
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

import Adapter.Appointment_Shop_History_adapter;
import Adapter.Shop_Reg_Revenue_adapter;
import Model.Appointment_model;
import Model.Shop_Reg_Revenue_model;

public class Shop_Reg_Revenuefragment extends Fragment {
    private ListView listView;
    private Shop_Reg_Revenue_adapter adapter;
    private ArrayList<Shop_Reg_Revenue_model> RevenueList;
    private String BSE_URl = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking/booking/";
    String currentdate ="";
    String currentMonth ="";
    private int todaytotalamount ;
    private int monthtotalamount;
    private int total;
    private TextView todayrev,monthrev,totalrevenue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shop_reg_revenuefragment,container,false);
        listView = view.findViewById(R.id.listviewshopRevenue);
        todayrev = view.findViewById(R.id.tvTodayRevenue);
        monthrev = view.findViewById(R.id.tvMonthRevenue);
        totalrevenue = view.findViewById(R.id.tvTotalRevenue);


        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", requireContext().MODE_PRIVATE);
        String shopId = sharedPref.getString("shopId", null);
        RevenueList = new ArrayList<>();
        adapter = new Shop_Reg_Revenue_adapter(getContext(), R.layout.custome_shop_reg_revenuetarn, RevenueList);
        listView.setAdapter(adapter);
        Date current = new Date();
        SimpleDateFormat datestr = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");

        currentdate = datestr.format(current);
        currentMonth = monthFormat.format(current);

        if (shopId != null) {
            GetAllData(shopId);
        } else {
            Toast.makeText(requireContext(), "Shop ID not found. Please login again.", Toast.LENGTH_SHORT).show();
        }





        return view;
    }
    void GetAllData(String shopId){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,BSE_URl+ shopId,null,
                response ->{
                    todaytotalamount = 0;
                    monthtotalamount = 0;
                    total=0;
                    RevenueList.clear();
                    try {
                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            String status = jData.optString("status", "completed");
                            String date = jData.optString("DateBook", "");
                            String price =jData.optString("Price","");
                            total+=Integer.parseInt(price);

                            if (status.equals("completed") && date.endsWith(currentMonth)) {
                                monthtotalamount += Integer.parseInt(price);
                            }
                            if(currentdate.equals(date)&&status.equals("completed")) {
                                String id = jData.optString("_id", "");
                                String customerName = jData.optString("CustomerName", "Unknown");
                                String customerImg = jData.optString("CustomerImg", "");
                                todaytotalamount +=Integer.parseInt(price);
                                String time = jData.optString("TimeBook", "");
                                String TimeDate = date + "  " + time;
                              RevenueList.add(new Shop_Reg_Revenue_model(customerImg,customerName,TimeDate,price,status));

                            }
                        }

                        adapter.notifyDataSetChanged();
                        todayrev.setText("₹ " +String.valueOf(todaytotalamount));
                        monthrev.setText("₹ " +String.valueOf(monthtotalamount));
                        totalrevenue.setText("₹ "+String.valueOf(total));


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error->{
                    Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                });
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

}
