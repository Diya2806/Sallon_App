package com.diya.sallonapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
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

import Adapter.Customer_adapter;
import Model.Customer_model;
import Model.Shop_model;

public class Customerfragment extends Fragment {
    private ListView listView;
    private Customer_adapter adapter;
    private ArrayList<Customer_model> customerList;
    private AutoCompleteTextView customer;
    private ArrayList<Customer_model> filteredList;
    private ArrayList<String> customerName;
private String URL ="https://semicordate-wabbly-veda.ngrok-free.dev/customer";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.customerfragment,container,false);
        View view = inflater.inflate(R.layout.customerfragment, container, false);

        listView=view.findViewById(R.id.listViewCustomer);
        customer = view.findViewById(R.id.searchCustomer);

        customerList = new ArrayList<>();
        filteredList = new ArrayList<>();
        customerName = new ArrayList<>();

        adapter = new Customer_adapter(requireContext(), R.layout.custome_customer, filteredList);
        listView.setAdapter(adapter);
        GetAllCustomer();

        customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 filtercustomerList(s.toString());
            }
        });
        customer.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedcustomer = parent.getItemAtPosition(position).toString();
            customer.setText(selectedcustomer); // keep selected name visible
            filtercustomerList(selectedcustomer);
        });
        return view;
    }

    void GetAllCustomer(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,URL,null,
                response ->{
                try{
                    JSONArray List = response.getJSONArray("List");

                    customerList.clear();
                    for(int i=0; i<List.length(); i++){
                        JSONObject jData = List.getJSONObject(i);

                        String name = jData.optString("Customername","");
                        String Phone = jData.optString("phone","");
                        String Email = jData.optString("email","");
                        String address = jData.optString("address","");
                        String img = jData.optString("image","");
                      //  customerList.add(new Customer_model(name,Phone,Email,address,img));
                        Customer_model model =new Customer_model(name,Phone,Email,address,img);
                       customerList.add(model);
                       customerName.add(name);

                    }
                    ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            customerName
                    );
                    customer.setAdapter(dropdownAdapter);

                    filteredList.clear();
                    filteredList.addAll(customerList);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(requireContext(), "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                },
                error ->{
                    Toast.makeText(requireContext(), "volley Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        Volley.newRequestQueue(requireContext()).add(request);
    }
    private void filtercustomerList(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(customerList);
        } else {
            for (Customer_model shop : customerList) {
                if (shop.getCustomerName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(shop);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
