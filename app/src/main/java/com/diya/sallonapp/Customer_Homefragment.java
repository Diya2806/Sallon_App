package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.Customer_Shop_Details_adapter;
import Adapter.poster_adapter;
import Model.Customer_Shop_Details_model;
import Model.Poster_model;

public class Customer_Homefragment extends Fragment {

    private LinearLayout book, history, profile;
    private ListView listView;
    private ViewPager2 poster;
    private String customerId;
    private poster_adapter posteradapter;
    private ArrayList<Poster_model> posterModels;
    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner";


    private final ArrayList<Customer_Shop_Details_model> ShopList = new ArrayList<>();
    private Customer_Shop_Details_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_homefragment, container, false);

        listView = view.findViewById(R.id.listviewshopDetails);
        poster = view.findViewById(R.id.headerSlider);
        book = view.findViewById(R.id.CustomerBooking);
        history = view.findViewById(R.id.CustomerHistory);
        profile = view.findViewById(R.id.CustomerProfileHome);

        // Header Slider
        posterModels = new ArrayList<>();
        posterModels.add(new Poster_model(R.drawable.sallon));
        posterModels.add(new Poster_model(R.drawable.sallon));
        posterModels.add(new Poster_model(R.drawable.sallon));
        posterModels.add(new Poster_model(R.drawable.sallon));
        posterModels.add(new Poster_model(R.drawable.sallon));

        posteradapter = new poster_adapter(requireContext(), posterModels);
        poster.setAdapter(posteradapter);

        // Shop List Adapter
        adapter = new Customer_Shop_Details_adapter(requireContext(), R.layout.custome_customer_dashbord, ShopList);

        listView.setAdapter(adapter);
        fatchlistData();
        adapter.notifyDataSetChanged();
        if (getArguments() != null) {
            customerId = getArguments().getString("customerId", "");
        }

        try {
            book.setOnClickListener(v -> {
                Customer_Appointmentfragmnet appointmentFragment = new Customer_Appointmentfragmnet();
                Bundle bundle = new Bundle();
                bundle.putString("customerId", customerId);
                appointmentFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.customermenucontainer, appointmentFragment)
                        .addToBackStack(null)
                        .commit();
            });
        } catch (Exception e) {
            Toast.makeText(requireContext(),e.getMessage() , Toast.LENGTH_SHORT).show();
        }

        history.setOnClickListener(v ->{
            Customer_Historyfragment history = new Customer_Historyfragment();
            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            history.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.customermenucontainer, history)
                    .addToBackStack(null)
                    .commit();
        });



        profile.setOnClickListener(v -> {
            Customer_Profilefragment profileFragment = new Customer_Profilefragment();

            // Pass customerId
            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            profileFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.customermenucontainer, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });

listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Customer_Shop_Details_model selectedShop = ShopList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("customerId", customerId);
        bundle.putString("shopId", selectedShop.getShopId());
        bundle.putString("shopName", selectedShop.getShopName());
        bundle.putString("shopDetails", selectedShop.getShopDetails());
        bundle.putString("shopImage", selectedShop.getShopImg());
        bundle.putFloat("shopRating", selectedShop.getRating());
        bundle.putString("OwnerName",selectedShop.getOwnerName());

        // Create fragment and set arguments
        customer_Shop_Detailsfragment detailFragment = new customer_Shop_Detailsfragment();
        detailFragment.setArguments(bundle);

        // Replace fragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.customermenucontainer, detailFragment)
                .addToBackStack(null) // so back button works
                .commit();
    }
});


        return view;
    }
    private void fatchlistData() {
        String url = BASE_URL + "/allshops";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ShopList.clear();

                        JSONArray jsonArray = response.optJSONArray("List");
                        if (jsonArray == null) jsonArray = new JSONArray();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject shop = jsonArray.getJSONObject(i);
                            String id = shop.optString("shopId","");
                            String name = shop.optString("shopName", "Unnamed Shop");
                            String bio = shop.optString("bio", "");
                            String OwnerNamw = shop.optString("Ownername","");
                            float rating = 0f;
                            try { rating = (float) shop.optDouble("rating", 0.0); } catch (Exception ignored) {}
                            String shopPhoto = shop.optString("shopPhoto", "");

                            ShopList.add(new Customer_Shop_Details_model(id,OwnerNamw,name, bio, rating, shopPhoto));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {

                        Toast.makeText(requireContext(), "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {

                    Toast.makeText(requireContext(), "Network error: " , Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}