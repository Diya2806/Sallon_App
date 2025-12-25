package com.diya.sallonapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.Shop_Reg_customerSummary_adapter;
import Model.Shop_Reg_customerSummary_model;

public class Shop_Reg_Customerfragment extends Fragment {
    private ListView listView;
    private Shop_Reg_customerSummary_adapter adapter;
    private ArrayList<Shop_Reg_customerSummary_model> CustomerList;
    private String count ="https://semicordate-wabbly-veda.ngrok-free.dev/Booking/customers/";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.shop_reg_customerfragment,container,false);
        listView = view.findViewById(R.id.listViewRegCustomer);


        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", requireContext().MODE_PRIVATE);
        String shopId = sharedPref.getString("shopId", null);

        if (shopId != null) {
            GetCount(shopId);
        } else {
            Toast.makeText(requireContext(), "Shop ID not found. Please login again.", Toast.LENGTH_SHORT).show();
        }
        CustomerList = new ArrayList<>();
        adapter = new Shop_Reg_customerSummary_adapter(requireContext(),R.layout.custome_shop_reg_customer,CustomerList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Shop_Reg_customerSummary_model selected = CustomerList.get(position);

            Bundle bundle = new Bundle();
            bundle.putString("customerId", selected.getId());
            bundle.putString("image", selected.getProfileImg());
            bundle.putString("name", selected.getName());
            bundle.putString("number", selected.getPhone());
            bundle.putString("address", selected.getAddress());
            bundle.putInt("completedBookings", selected.getTotalServices());

            Shop_Reg_CustomerDetails detailsFragment = new Shop_Reg_CustomerDetails();
            detailsFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer, detailsFragment).addToBackStack(null).commit();
        });
        
        return view;
    }




    void GetCount(String shopid){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,count+shopid,null,
                response->{
                    CustomerList.clear();
                    try{
                        JSONArray dataArray = response.getJSONArray("List");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            String id = jData.optString("CustomerId", "");
                            String img= jData.optString("CustomerImg","");
                            String customerName = jData.optString("CustomerName", "Unknown");
                            String phone = jData.optString("CustomerPhone", "Unknown");
                            String address = jData.optString("CustomerAddress","");
                            int count = jData.optInt("totalBookings");
                            CustomerList.add(new Shop_Reg_customerSummary_model(id,img,customerName,phone,address,count));
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error->{
                    Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
