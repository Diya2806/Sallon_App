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
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import Adapter.Appointment_adapter;
import Adapter.ShopFeedback_adapter;
import Adapter.Shop_Booking_adapter;
import Model.Appointment_model;
import Model.ShopFeedback_model;
import Model.Shop_Booking_model;
import Model.Shop_Reg_Revenue_model;

public class ShopHomefragment extends Fragment {
    private ListView appointment,feedback;
    private ArrayList<Appointment_model> allAppointments;
    private ArrayList<Appointment_model> filteredList;
    private Appointment_Shop_History_adapter adapter;

    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking";
    private ShopFeedback_adapter feedbackAdapter;
    private ArrayList<ShopFeedback_model> FeedbackList;
    private TextView viewallapointment,viewallfeedback,Booking,Revenue;

    private String apiUrl = "https://semicordate-wabbly-veda.ngrok-free.dev/FeedBack/shop/";
    String currentdate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shophomefragment, container, false);
        appointment = view.findViewById(R.id.listviewAppointments);
        feedback = view.findViewById(R.id.listviewFeedback);
        viewallapointment = view.findViewById(R.id.allapointment);
        viewallfeedback = view.findViewById(R.id.allfeedback);
        Booking = view.findViewById(R.id.tvTotalBookings);
        Revenue =view.findViewById(R.id.tvRevenue);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", requireContext().MODE_PRIVATE);
        String shopId = sharedPref.getString("shopId", null);
        allAppointments = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new Appointment_Shop_History_adapter(getContext(), R.layout.custome_shop_appointment, filteredList);
        appointment.setAdapter(adapter);

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


//        Feedback
        FeedbackList = new ArrayList<>();
        feedbackAdapter = new ShopFeedback_adapter(requireContext(),R.layout.custome_shop_feedback,FeedbackList);
        feedback.setAdapter(feedbackAdapter);
        Date current = new Date();
        SimpleDateFormat datestr = new SimpleDateFormat("dd/MM/yyyy");
        currentdate = datestr.format(current);


        Toast.makeText(requireContext(), currentdate, Toast.LENGTH_SHORT).show();


        if (shopId != null) {
            fetchFeedback(shopId);
            GetAllData(shopId);
            loadTodayRevenue(shopId);
            showTab("approved");
        } else {
            Toast.makeText(requireContext(), "Shop ID not found. Please login again.", Toast.LENGTH_SHORT).show();
        }


        viewallapointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer, new Shop_Bookingfragment()).commit();
            }
        });
        viewallfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer, new Shop_Reg_Feedbackfragment()).commit();

            }
        });
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer, new Shop_Bookingfragment()).commit();
                Toast.makeText(requireContext(), "Book All Date", Toast.LENGTH_SHORT).show();

            }
        });
        Revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer, new Shop_Reg_Revenuefragment()).commit();

            }
        });

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
                                    JSONObject customer = obj.getJSONObject("CustomerData");
                                    String name = customer.getString("Customername");
                                    String image = customer.optString("image", ""); // optional image

                                    ShopFeedback_model feedback = new ShopFeedback_model(name, comment,image,rating);
                                    FeedbackList.add(feedback);
                                }

                                feedbackAdapter.notifyDataSetChanged();

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
                    int totalBookings = 0;
                    allAppointments.clear();
                    try {
                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            String status = jData.optString("status", "approve");
                            String date = jData.optString("DateBook", "");
                            if(currentdate.equals(date)&&!status.equals("completed")&&!status.equals("pending")) {
                                totalBookings++;
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
                        Booking.setText("Bookings\n" + totalBookings);
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
    void loadTodayRevenue(String shopId) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL+"/booking/"+shopId, null,
                response -> {
                    int todaytotal = 0;
                    try {
                        JSONArray dataArray = response.getJSONArray("List");
                        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            String date = jData.optString("DateBook", "");
                            String status = jData.optString("status", "completed");
                            int price = jData.optInt("Price", 0);

                            if (currentDate.equals(date) && status.equals("completed")) {
                                todaytotal += price;
                            }
                        }
                        Revenue.setText("Revenue\n₹ " + todaytotal);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(requireContext()).add(request);
    }

}
