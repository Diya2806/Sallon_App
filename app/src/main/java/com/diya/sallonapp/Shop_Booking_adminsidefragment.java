package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.Shop_Reg_customerSummary_adapter;
import Model.Shop_Reg_customerSummary_model;

public class Shop_Booking_adminsidefragment extends Fragment {
    private ImageView profile;
    private TextView ownername,shopname,phone,address;
    private ListView listView;
    private Shop_Reg_customerSummary_adapter adapter;
    private ArrayList<Shop_Reg_customerSummary_model> CustomerList;
    private String count ="https://semicordate-wabbly-veda.ngrok-free.dev/Booking/customers/";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.shop_bookinglist_adminside,container,false);

        profile = view.findViewById(R.id.shopdetailownerimg);
        ownername = view.findViewById(R.id.shopdetailownerName);
        shopname = view.findViewById(R.id.shopdetailshopName);
        phone = view.findViewById(R.id.shopdetailownerPhone);
        address = view.findViewById(R.id.shopdetailownerAddress);
        listView = view.findViewById(R.id.customerList);

        Bundle bundle = getArguments();
        if (getArguments() != null) {
            String id = bundle.getString("Id");
            String OwnerName = bundle.getString("OwnerName");
            String ShopName = bundle.getString("ShopName");
            String Phone = bundle.getString("Number");
            String Address = bundle.getString("Address");
            String Image = bundle.getString("Img");
            ownername.setText(OwnerName);
            shopname.setText(ShopName);
            phone.setText(Phone);
            address.setText(Address);
            Glide.with(requireActivity()).load(Image).placeholder(R.drawable.person).circleCrop().into(profile);
            GetCount(id);
        }

        CustomerList = new ArrayList<>();
        adapter = new Shop_Reg_customerSummary_adapter(requireContext(),R.layout.custome_shop_reg_customer,CustomerList);
        listView.setAdapter(adapter);

        return view;
    }

    void GetCount(String shopid) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                count + shopid,
                null,
                response -> {
                    CustomerList.clear();
                    try {
                        if (response.has("List")) {
                            JSONArray dataArray = response.getJSONArray("List");
                            if (dataArray.length() == 0) {
                                Toast.makeText(requireActivity(), "No customers found", Toast.LENGTH_SHORT).show();
                            } else {
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
                            }
                        } else {
                            // No "List" key at all
                            Toast.makeText(requireActivity(), "No customers found", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(requireActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(requireActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }


}
