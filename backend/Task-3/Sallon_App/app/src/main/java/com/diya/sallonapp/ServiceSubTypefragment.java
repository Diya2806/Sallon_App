package com.diya.sallonapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class ServiceSubTypefragment extends Fragment {

    private Spinner Main, Status;
    private EditText Name;
    private Button btn;
    private String editId = null;
    private ArrayList<String> serviceNames = new ArrayList<>();
    private ArrayList<String> serviceIds = new ArrayList<>();

    private String ApiUrl = "https://semicordate-wabbly-veda.ngrok-free.dev/ServiceType";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.servicesubtypefragment, container, false);

        Main = view.findViewById(R.id.spinnerServiceMain);
        Status = view.findViewById(R.id.spinnerServiceSub);
        Name = view.findViewById(R.id.SubServiceName);
        btn = view.findViewById(R.id.btnSaveSubService);

        loadServiceNames();
        // Handle Edit operation if arguments exist
        if (getArguments() != null) {
            editId = getArguments().getString("editId");
            String subServiceName = getArguments().getString("editSubServiceName");
            String editServiceTypeId = getArguments().getString("editMainService");
            String status = getArguments().getString("editStatus");

            Name.setText(subServiceName);
            btn.setText("Update");


            Main.post(() -> {
                if (editServiceTypeId != null) {
                    int pos = serviceNames.indexOf(editServiceTypeId);
                    if (pos >= 0) Main.setSelection(pos);
                }
            });

            Status.post(() -> {
                ArrayAdapter adapter = (ArrayAdapter) Status.getAdapter();
                if (adapter != null) {
                    int pos = adapter.getPosition(status);
                    if (pos >= 0) Status.setSelection(pos);
                }
            });
        }


        btn.setOnClickListener(v -> {
            String subServiceName = Name.getText().toString().trim();
            String status = Status.getSelectedItem().toString();
            int selectedIndex = Main.getSelectedItemPosition();
            String selectedServiceId = serviceIds.get(selectedIndex);
            String selectedServiceName = serviceNames.get(selectedIndex);

            if (subServiceName.isEmpty()) {
                Toast.makeText(requireContext(), "Enter Sub Service Name", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle result = new Bundle();

            if (editId == null) {
                // ADD
                result.putString("subServiceName", subServiceName);
                result.putString("serviceTypeId", selectedServiceId);
                result.putString("mainService", selectedServiceName);
                result.putString("status", status);
                getParentFragmentManager().setFragmentResult("addSubService", result);
            } else {
                // UPDATE
                result.putString("updateId", editId);
                result.putString("updateSubServiceName", subServiceName);
                result.putString("updateServiceTypeId", selectedServiceId);
                result.putString("updateStatus", status);
                getParentFragmentManager().setFragmentResult("updateSubService", result);
            }


            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menucontainer, new ServiceSubList_fragment(), "ServiceSubListFragment")
                    .commit();
        });



        return view;
    }

    void loadServiceNames() {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl, null,
                    response -> {
                        try {
                            serviceNames.clear();
                            serviceIds.clear();

                            JSONArray dataArray = response.getJSONArray("List");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jData = dataArray.getJSONObject(i);
                                String serviceName = jData.optString("ServiceName", "");
                                String serviceId = jData.optString("_id", "");

                                if (!serviceNames.contains(serviceName) && !serviceName.isEmpty()) {
                                    serviceNames.add(serviceName);
                                    serviceIds.add(serviceId); // store corresponding ID
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    serviceNames
                            ) {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    ((TextView) view).setTextColor(Color.BLACK);
                                    return view;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getDropDownView(position, convertView, parent);
                                    ((TextView) view).setTextColor(Color.BLACK);
                                    return view;
                                }
                            };

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Main.setAdapter(adapter);

                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "Parse Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
