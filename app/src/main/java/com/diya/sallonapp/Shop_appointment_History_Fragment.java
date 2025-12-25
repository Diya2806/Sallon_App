package com.diya.sallonapp;

import static android.content.Context.MODE_PRIVATE;

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
import java.util.Locale;

import Adapter.Appointment_Shop_History_adapter;
import Model.Appointment_model;

public class Shop_appointment_History_Fragment extends Fragment {
    private TextView approve,Reject,Pending,Complete;
    private ListView listView;
    private ArrayList<Appointment_model> allAppointments;
    private ArrayList<Appointment_model> filteredList;
    private Appointment_Shop_History_adapter adapter;
    private String currentTab = "pending";
    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shop_appointment_history_fragment,container,false);
        approve = view.findViewById(R.id.approvetxt);
        Reject = view.findViewById(R.id.rejecttxt);
        Pending = view.findViewById(R.id.pendingtxt);
        Complete = view.findViewById(R.id.compeletetxt);
        listView = view.findViewById(R.id.shopappointmenthistory);
        allAppointments = new ArrayList<>();


        filteredList = new ArrayList<>();
        adapter = new Appointment_Shop_History_adapter(getContext(), R.layout.custome_shop_appointment, filteredList);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Appointment_Shop_History_adapter.OnItemClickListener() {
            @Override
            public void onApprove(Appointment_model model) {
//                model.setStatus("approved");
                updateBookingStatus(model.getId(), "approved");
                Toast.makeText(requireActivity(), "Approve", Toast.LENGTH_SHORT).show();
                refreshLists();
            }

            @Override
            public void onIgnore(Appointment_model model) {
//                model.setStatus("rejected");
                updateBookingStatus(model.getId(), "rejected");
                Toast.makeText(requireActivity(), "Reject", Toast.LENGTH_SHORT).show();
                refreshLists();
            }

            @Override
            public void onComplete(Appointment_model model) {
                updateBookingStatus(model.getId(), "completed");
               // model.setStatus("completed");
                Toast.makeText(requireActivity(), "Complete", Toast.LENGTH_SHORT).show();
                refreshLists();

            }
        });


        // Tab clicks
        approve.setOnClickListener(v -> {
            currentTab = "approved";
            showTab("approved");
            updateTabBackgrounds();
        });
        Reject.setOnClickListener(v -> {
            currentTab = "rejected";
            showTab("rejected");
            updateTabBackgrounds();
        });
        Pending.setOnClickListener(v -> {
            currentTab = "pending";
            showTab("pending");
            updateTabBackgrounds();
        });
        Complete.setOnClickListener(v -> {
            currentTab = "completed";
            showTab("completed");
            updateTabBackgrounds();
        });


        currentTab = "pending";
        showTab(currentTab);
        updateTabBackgrounds();
        String shopId = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("shopId", "");
        GetAllData(shopId);

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
            showTab(currentTab);
        adapter.notifyDataSetChanged();
    }

    private void updateTabBackgrounds() {
        // Reset all tabs to default
        Pending.setBackgroundResource(R.drawable.bg_history);
        approve.setBackgroundResource(R.drawable.bg_history);
        Reject.setBackgroundResource(R.drawable.bg_history);
        Complete.setBackgroundResource(R.drawable.bg_history);

        // Highlight current tab
        switch (currentTab.toLowerCase()) {
            case "pending":
                Pending.setBackgroundResource(R.drawable.bg_history_highlight);
                break;
            case "approved":
                approve.setBackgroundResource(R.drawable.bg_history_highlight);
                break;
            case "rejected":
                Reject.setBackgroundResource(R.drawable.bg_history_highlight);
                break;
            case "completed":
                Complete.setBackgroundResource(R.drawable.bg_history_highlight);
                break;
        }
    }
    void GetAllData(String shopId){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,BASE_URL+"/booking/" + shopId,null,
                response ->{
                    allAppointments.clear();
                    try {
                        JSONArray dataArray = response.getJSONArray("List");
                        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);

                            String status = jData.optString("status", "Pending");

                            // Only add pending bookings
                                String id = jData.optString("_id", "");
                                String customerName = jData.optString("CustomerName", "Unknown");
                                String shopName = jData.optString("ShopName", "Unknown");
                                String customerPhone = jData.optString("CustomerPhone", "Unknown");
                                String customerImg = jData.optString("CustomerImg", "");
                                String serviceName = jData.optString("ServiceName", "Unknown");
                                String subServiceName = jData.optString("SubServiceName", "Unknown");
                                String date = jData.optString("DateBook", "");
                                String title = jData.getString("Title");
                                String time = jData.optString("TimeBook", "");
                                String TimeDate = date + "  " + time;


                                allAppointments.add(new Appointment_model(id, customerName, serviceName, subServiceName, TimeDate, customerImg, status, customerPhone, title));


                        }
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

                            // Refresh the list after update
                            //String shopId = requireActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("shopId", "");
                            //    GetAllData(shopId);
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
}
