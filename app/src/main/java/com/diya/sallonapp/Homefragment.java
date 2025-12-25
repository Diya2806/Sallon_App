package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Homefragment extends Fragment {
    private LinearLayout Shop,Customer,ServiceType,ServiceSubTye,ShopRequewst,totalbooking,Revenue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.homefragment,container,false);
        View view = inflater.inflate(R.layout.homefragment, container, false);

//        Initialization
        Shop = view.findViewById(R.id.shopowner);
        Customer = view.findViewById(R.id.customer);
        ServiceType = view.findViewById(R.id.ServiceType);
        ServiceSubTye = view.findViewById(R.id.ServiceSubType);
        ShopRequewst = view.findViewById(R.id.Shoprequest);
        totalbooking =view.findViewById(R.id.booking);
        Revenue = view.findViewById(R.id.Revenue);

        Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Shopfragment()).commit();
            }
        });
        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Customerfragment()).commit();

            }
        });
        ServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new ServiceList_fragment()).commit();

            }
        });
        ServiceSubTye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new ServiceSubList_fragment()).commit();

            }
        });
        ShopRequewst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Shop_Requestfragment()).commit();

            }
        });
        totalbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new TotalBookingfragment()).commit();

            }
        });
        Revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Revenuefragment()).commit();

            }
        });
        return view;

    }

}
