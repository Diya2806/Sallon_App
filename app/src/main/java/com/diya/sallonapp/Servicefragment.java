package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapter.ServiceType_adapter;
import Model.ServiceType_model;

public class Servicefragment extends Fragment {

    private EditText name;
    private Spinner spinner;
    private Button btn;
    private String editId = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.servicefragment,container,false);
        View view = inflater.inflate(R.layout.servicefragment, container, false);


        name = view.findViewById(R.id.editServiceName);
        spinner =view.findViewById(R.id.spinnerStatus);
        btn = view.findViewById(R.id.btnSaveService);

        if (getArguments() != null) {
            editId = getArguments().getString("ServiceId");
            String serviceName = getArguments().getString("ServiceName");
            String status = getArguments().getString("Status");

            name.setText(serviceName);

            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            int pos = adapter.getPosition(status);
            spinner.setSelection(pos);

            btn.setText("Update");
        }




        btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String Name = name.getText().toString();
              String status = spinner.getSelectedItem().toString();
              if (Name.isEmpty()) {
                  Toast.makeText(requireContext(), "Enter service name", Toast.LENGTH_SHORT).show();
                  return;
              }
           if(editId==null){
               Bundle bundle = new Bundle();
               bundle.putString("ServiceName", Name);
               bundle.putString("Status", status);

               ServiceList_fragment listFragment = new ServiceList_fragment();
               listFragment.setArguments(bundle);
               requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, listFragment, "ServiceListFragment").commit();

           }
           else{
               ServiceList_fragment listFragment = new ServiceList_fragment();
               requireActivity().getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.menucontainer, listFragment, "ServiceListFragment")
                       .commit();

               requireActivity().getSupportFragmentManager().executePendingTransactions();
               listFragment.update(editId, Name, status);


           }


              name.setText("");
//              requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new ServiceList_fragment()).commit();
          }
      });

        return view;
    }






}
