package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import Adapter.City_adapter;
import Model.City_model;

public class Cityfragment extends Fragment {
    private ListView listView;
    private Spinner spinner;
    private EditText cityname;
    private Button btn;
    private City_adapter adapter;
    private ArrayList<City_model> CityList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cityfragment, container, false);
        listView =view.findViewById(R.id.listViewCity);
        spinner = view.findViewById(R.id.spinnerState);
        cityname=view.findViewById(R.id.editCityName);
        btn=view.findViewById(R.id.btnSaveCity);


        CityList = new ArrayList<>();
        adapter = new City_adapter(requireContext(),R.layout.custome_city,CityList);
        listView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state= spinner.getSelectedItem().toString();
                String city= cityname.getText().toString();
                CityList.add(new City_model(state,city));
                adapter.notifyDataSetChanged();
                cityname.setText("");
            }
        });


        return view;
    }
}
