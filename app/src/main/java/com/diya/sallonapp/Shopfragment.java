package com.diya.sallonapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.Shop_Adapter;
import Model.Shop_model;

public class Shopfragment extends Fragment {
    private ListView listView;
    private AutoCompleteTextView shop;
    private Shop_Adapter adapter;
    private ArrayList<Shop_model> shopList;
    private ArrayList<Shop_model> filteredList;
    private ArrayList<String> shopNames;
    private final String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.shopfragment,container,false);
        View view = inflater.inflate(R.layout.shopfragment, container, false);


        listView = view.findViewById(R.id.listView);
        shop = view.findViewById(R.id.searchShopOwner);

        shopList = new ArrayList<>();
        filteredList = new ArrayList<>();
        shopNames = new ArrayList<>();


        adapter = new Shop_Adapter(requireContext(), R.layout.custome, filteredList);
        listView.setAdapter(adapter);
        fetchShopData();


        shop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                filterShopList(s.toString());

            }
        });

        shop.setOnItemClickListener(((parent, view1, position, id) -> {
            String selectshop = parent.getItemAtPosition(position).toString();
            shop.setText(selectshop);
            filterShopList(selectshop);

        }));
        return view;
    }
    private void fetchShopData() {
        String url = BASE_URL + "/";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray list = response.getJSONArray("List");
                        shopList.clear();
                        shopNames.clear();
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject obj = list.getJSONObject(i);
                            String status = obj.optString("status"); // get status first
                            if (!status.equalsIgnoreCase("approved")) continue;
                            String ownerName = obj.optString("Ownername");
                            String shopName = obj.optString("shopName");
                            String phone = obj.optString("phone");
                            String email = obj.optString("email");
                            String add = obj.optString("address");
                            String area = obj.optString("area");
                            String city = obj.optString("city");
                            String state = obj.optString("state");
                            String img = obj.optString("ownerPhoto");
                            String Address = add+","+area+","+city+","+state;

                            Shop_model shopModel = new Shop_model(ownerName, shopName, phone, email, Address, status, img);
                            shopList.add(shopModel);
                            shopNames.add(shopName);
                        }
                        // Set dropdown adapter for AutoCompleteTextView
                        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                shopNames
                        );
                        shop.setAdapter(dropdownAdapter);

                        // Initially show all approved shops
                        filteredList.clear();
                        filteredList.addAll(shopList);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void filterShopList(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(shopList);
        } else {
            for (Shop_model shop : shopList) {
                if (shop.getShopName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(shop);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
