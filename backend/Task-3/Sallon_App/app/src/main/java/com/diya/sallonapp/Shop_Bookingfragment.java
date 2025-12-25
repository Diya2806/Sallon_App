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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.Appointment_Shop_History_adapter;
import Adapter.Shop_Booking_adapter;
import Model.Appointment_model;
import Model.Shop_Booking_model;

public class Shop_Bookingfragment extends Fragment {
    private ListView listView;
    private ArrayList<Appointment_model> allAppointments;
    private ArrayList<Appointment_model> filteredList;
    private Appointment_Shop_History_adapter adapter;

    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking";
    String currentdate="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shop_bookingfragment,container,false);

        listView = view.findViewById(R.id.todayBookingList);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", requireContext().MODE_PRIVATE);
        String shopId = sharedPref.getString("shopId", null);
        allAppointments = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new Appointment_Shop_History_adapter(getContext(), R.layout.custome_shop_appointment, filteredList);
        listView.setAdapter(adapter);


        adapter.setOnItemClickListener(new Appointment_Shop_History_adapter.OnItemClickListener() {
            @Override
            public void onApprove(Appointment_model model) {
//                model.setStatus("approved");

            }

            @Override
            public void onIgnore(Appointment_model model) {
//                model.setStatus("rejected");

            }

            @Override
            public void onComplete(Appointment_model model) {
                updateBookingStatus(model.getId(), "completed");
                // model.setStatus("completed");
                Toast.makeText(requireActivity(), "Complete", Toast.LENGTH_SHORT).show();
                refreshLists();

            }
        });
        Date current = new Date();
        SimpleDateFormat datestr = new SimpleDateFormat("dd/MM/yyyy");
        currentdate = datestr.format(current);
        Toast.makeText(requireContext(), currentdate, Toast.LENGTH_SHORT).show();


        if (shopId != null) {

            GetAllData(shopId);
            showTab("approved");
        } else {
            Toast.makeText(requireContext(), "Shop ID not found. Please login again.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void showTab(String status) {
        filteredList.clear();

        for (Appointment_model item : allAppointments) {
            if (item.getStatus().equalsIgnoreCase(status)) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void refreshLists() {

        adapter.notifyDataSetChanged();
    }
    void updateBookingStatus(String bookingId, String status) {
        try {
            JSONObject body = new JSONObject();
            body.put("status", status);

            String url = BASE_URL + "/approved/" + bookingId;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                    response -> {
                        try {
                            String message = response.optString("Message", "Status updated successfully");
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();


                            for (int i = 0; i < allAppointments.size(); i++) {
                                if (allAppointments.get(i).getId().equals(bookingId)) {
                                    allAppointments.get(i).setStatus(status);
                                    break;
                                }
                            }

                            // Refresh the current tab
                            refreshLists();

                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    });
            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                    10000, // 10 seconds timeout
                    0,     // no retry
                    1f     // default backoff multiplier
            ));

            RequestQueue queue = Volley.newRequestQueue(requireContext());
            queue.add(request);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void GetAllData(String shopId){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,BASE_URL+"/booking/" + shopId,null,
                response ->{
                    allAppointments.clear();
                    try {
                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            String status = jData.optString("status", "approve");
                            String date = jData.optString("DateBook", "");
                            if(currentdate.equals(date)) {
                                String id = jData.optString("_id", "");
                                String customerName = jData.optString("CustomerName", "Unknown");
                                String shopName = jData.optString("ShopName", "Unknown");
                                String customerPhone = jData.optString("CustomerPhone", "Unknown");
                                String customerImg = jData.optString("CustomerImg", "");
                                String serviceName = jData.optString("ServiceName", "Unknown");
                                String subServiceName = jData.optString("SubServiceName", "Unknown");

                                String title = jData.getString("Title");
                                String time = jData.optString("TimeBook", "");
                                String TimeDate = date + "  " + time;
                                allAppointments.add(new Appointment_model(id, customerName, serviceName, subServiceName, TimeDate, customerImg, status, customerPhone, title));

                            }
                        }
                        showTab("approved");
                        adapter.notifyDataSetChanged();
                        refreshLists();
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
