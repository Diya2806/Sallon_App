package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import Adapter.Shop_Adapter;
import Adapter.State_adapter;
import Model.State_model;

public class Statefragment extends Fragment {
    private ListView listView;
    private State_adapter adapter;
    private ArrayList<State_model> StateList;
    private EditText name;
    private Button btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.statefragment, container, false);

        listView = view.findViewById(R.id.listViewState);
        name = view.findViewById(R.id.editStateName);
        btn = view.findViewById(R.id.btnSaveState);


        StateList = new ArrayList<>();
        adapter = new State_adapter(requireContext(),R.layout.custome_state,StateList);
        listView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();

                StateList.add(new State_model(Name));
                adapter.notifyDataSetChanged();
                name.setText("Enter State");
            }
        });

        return view;

    }
}
