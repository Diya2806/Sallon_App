package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import Adapter.Booking_adapter;
import Model.Booking_model;
import Model.Shop_model;

public class TotalBookingfragment extends Fragment {
    private ListView listView;
    private Booking_adapter adapter;
    private ArrayList<Booking_model> BookingList;
    private final String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.totalbookingfragment, container, false);
        listView = view.findViewById(R.id.listBookings);

        BookingList = new ArrayList<>();
        adapter=new Booking_adapter(requireContext(),R.layout.custome_totalbooking,BookingList);
        listView.setAdapter(adapter);
        fetchShopData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking_model books = BookingList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("Id", books.getId());
                bundle.putString("Img",books.getImg());
                bundle.putString("OwnerName",books.getOwnerName());
                bundle.putString("ShopName",books.getShopName());
                bundle.putString("Number",books.getNumber());
                bundle.putString("Address",books.getAddress());

                Shop_Booking_adminsidefragment shop = new Shop_Booking_adminsidefragment();
                shop.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, shop).commit();


            }
        });
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
                        BookingList.clear();

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject obj = list.getJSONObject(i);
                            String status = obj.optString("status"); // get status first
                            if (!status.equalsIgnoreCase("approved")) continue;
                            String id = obj.optString("_id");
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
                            BookingList.add(new Booking_model(id,ownerName,shopName,img,status,phone,Address));

                        }

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

}
