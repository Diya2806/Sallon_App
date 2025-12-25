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

import Adapter.Reg_customerDetails_adapter;

public class Shop_Reg_CustomerDetails extends Fragment {
    private ImageView profile;
    private TextView name, phone, address;
    private ListView serviceList;
    private Reg_customerDetails_adapter adapter;
    private ArrayList<Model.Shop_Reg_CustomerDetails> CustomerList;
    private String Url = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking/history/";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shop_reg_customerdetailsfragment,container,false);

        profile = view.findViewById(R.id.customerProfile);
        name = view.findViewById(R.id.customerName);
        phone = view.findViewById(R.id.customerPhone);
        address = view.findViewById(R.id.customerAddress);
        serviceList = view.findViewById(R.id.serviceList);

        // Get arguments from summary fragment
        Bundle bundle = getArguments();

        if (getArguments() != null) {
            String id = bundle.getString("customerId");
            String Name = bundle.getString("name");
            String Phone = bundle.getString("number");
            String Address = bundle.getString("address");
            String Image = bundle.getString("image");
            name.setText(Name);
            phone.setText(Phone);
            address.setText(Address);
            Glide.with(requireContext()).load(Image).circleCrop().into(profile);
            fetchCustomerServices(id);

        }
        CustomerList = new ArrayList<>();
        adapter = new Reg_customerDetails_adapter(requireContext(),R.layout.custome_shop_reg_customerdetail,CustomerList);
        serviceList.setAdapter(adapter);
        return view;
    }

    void fetchCustomerServices(String customerId){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url+customerId, null,
                response -> {
                    try {
                        CustomerList.clear();
                        JSONArray list = response.getJSONArray("List");
                        for(int i=0; i<list.length(); i++){
                            JSONObject obj = list.getJSONObject(i);

                            String date = obj.optString("DateBook","");
                            String serviceName = obj.optString("ServiceName","");
                            int price = obj.optInt("Price", 0);
                            CustomerList.add(new Model.Shop_Reg_CustomerDetails(date, serviceName, price));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e){
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

}
