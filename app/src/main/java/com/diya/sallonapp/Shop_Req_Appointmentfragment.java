package com.diya.sallonapp;

import static android.content.Context.MODE_PRIVATE;

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

import java.util.ArrayList;

import Adapter.Appointment_adapter;
import Model.Appointment_model;
import Model.Shop_Req_model;

public class Shop_Req_Appointmentfragment extends Fragment {
    private ListView listView;
    private Appointment_adapter adapter;
    private ArrayList<Appointment_model> AppointmentList;

    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.shop_req_appointmentfragment,container,false);
        listView = view.findViewById(R.id.appointmentListView);
        AppointmentList = new ArrayList<>();
        adapter = new Appointment_adapter(requireContext(),R.layout.custome_shop_appointment,AppointmentList);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Appointment_adapter.OnItemClickListener() {
            @Override
            public void onApprove(Appointment_model model) {
                updateBookingStatus(model.getId(), "approved");
                Toast.makeText(requireActivity(), "Approve", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIgnore(Appointment_model model) {
                Toast.makeText(requireActivity(), "Reject", Toast.LENGTH_SHORT).show();
                updateBookingStatus(model.getId(), "rejected");
            }
        });
        String shopId = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("shopId", "");
        GetAllData(shopId);
        return view;
    }

    void GetAllData(String shopId){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,BASE_URL+"/booking/" + shopId,null,
                response ->{
                AppointmentList.clear();
                    try {
                        JSONArray dataArray = response.getJSONArray("List");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            String status = jData.optString("status", "Pending");

                            // Only add pending bookings
                            if (!status.equalsIgnoreCase("approved") && !status.equalsIgnoreCase("rejected")) {
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


                            AppointmentList.add(new Appointment_model(id, customerName, serviceName, subServiceName, TimeDate, customerImg, status, customerPhone, title));

                        }
                    }
                        adapter.notifyDataSetChanged();
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
                            for (int i = 0; i < AppointmentList.size(); i++) {
                                if (AppointmentList.get(i).getId().equals(bookingId)) {
                                    AppointmentList.remove(i);
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }

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
