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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.Customer_History_adapter;
import Model.Appointment_model;
import Model.Customer_History_Model;

public class Customer_Historyfragment extends Fragment {
    private ListView listView;
    private Customer_History_adapter adapter;
    private ArrayList<Customer_History_Model> HistoryList;
    private ArrayList<Customer_History_Model> filteredList;
    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking/history/";
    private String customerId = "";
    private TextView approve,Reject,Pending,Complete;
    private String currentTab = "pending";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.customer_historyfragment,container,false);
        listView = view.findViewById(R.id.listviewCustomerHistory);
        approve = view.findViewById(R.id.approvetxt_ch);
        Reject = view.findViewById(R.id.rejecttxt_ch);
        Pending = view.findViewById(R.id.pendingtxt_ch);
        Complete = view.findViewById(R.id.compeletetxt_ch);

        HistoryList = new ArrayList<>();
        filteredList = new ArrayList<>();


        adapter = new Customer_History_adapter(requireContext(), R.layout.custome_customer_history, filteredList);
        listView.setAdapter(adapter);
        if (getArguments() != null) {
            customerId = getArguments().getString("customerId", "");
            if (!customerId.isEmpty()) {
                GETHISTORYDATA(customerId);
            }
        }
        adapter.setOnItemClickListene(new Customer_History_adapter.OnItemClickListener() {
            @Override
            public void onFeedBack(Customer_History_Model model) {
                Customer_FeedBackFragment feedbackFragment = new Customer_FeedBackFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CustomerId", customerId); // pass the customer id from HistoryFragment
                bundle.putString("ShopID",model.getShopId());
                bundle.putString("ShopName",model.getShopName());

                feedbackFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.customermenucontainer, feedbackFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Pending.setOnClickListener(v -> switchTab("pending"));
        approve.setOnClickListener(v -> switchTab("approved"));
        Reject.setOnClickListener(v -> switchTab("rejected"));
        Complete.setOnClickListener(v -> switchTab("completed"));
        return view;
    }

    void GETHISTORYDATA(String CustomerId){
        try{
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,BASE_URL+CustomerId,null,
                    response ->{
                try{
                    JSONArray list = response.getJSONArray("List");
                    HistoryList.clear();

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.getJSONObject(i);

                        String shopName = obj.optString("ShopName", "Unknown");
                        String shopId = obj.optString("ShopId","");
                        String serviceName = obj.optString("ServiceName", "Unknown");
                        String subServiceName = obj.optString("SubServiceName", "Unknown");
                        String date = obj.optString("DateBook", "");
                        String time = obj.optString("TimeBook", "");
                        String title = obj.optString("Title", "");
                        String status = obj.optString("status", "");
                        HistoryList.add(new Customer_History_Model(shopName, serviceName+" "+subServiceName+" "+title, date, time, status,shopId));

                    }

                    filterByStatus(currentTab);

                } catch (Exception e) {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                    },
                    error ->{
                        Toast.makeText(requireContext(), "volley Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            Volley.newRequestQueue(requireContext()).add(request);
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void switchTab(String status) {
        currentTab = status;
        filterByStatus(status);
        updateTabBackgrounds();
    }

    private void filterByStatus(String status) {
        filteredList.clear();
        for (Customer_History_Model item : HistoryList) {
            if (item.getStatus().equalsIgnoreCase(status)) {
                filteredList.add(item);
            }
        }
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
}
