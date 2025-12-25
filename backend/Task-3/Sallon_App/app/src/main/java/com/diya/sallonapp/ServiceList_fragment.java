package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Locale;

import Adapter.ServiceType_adapter;
import Model.ServiceType_model;

public class ServiceList_fragment extends Fragment {
    private ListView listView;
    private ServiceType_adapter ServiceType;
    private ArrayList<ServiceType_model> ServiceList;
    private Button  btn;
    private String ApiUrl = "https://semicordate-wabbly-veda.ngrok-free.dev/serviceType";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.servicelist_fragment,container,false);
        listView = view.findViewById(R.id.listViewServices);
        btn = view.findViewById(R.id.btnservicelistadd);

//        Add Custome Page For Display List
        ServiceList = new ArrayList<>();
        ServiceType = new ServiceType_adapter(requireContext(),R.layout.custome_servicetype,ServiceList);
        listView.setAdapter(ServiceType);


        ServiceType.setOnItemClickListener(new ServiceType_adapter.OnItemClickListener() {
            @Override
            public void onEdit(ServiceType_model service) {
//                updateService(service.getId(), service.getName(), service.getStatus());
                Servicefragment fragment = new Servicefragment();

                Bundle bundle = new Bundle();
                bundle.putString("ServiceId", service.getId());
                bundle.putString("ServiceName", service.getName());
                bundle.putString("Status", service.getStatus());
                fragment.setArguments(bundle);

          requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, fragment).addToBackStack(null).commit();

            }

            @Override
            public void onDelete(ServiceType_model service) {
                deleteService(service.getId(), service);
            }
        });
//        Fatch All Data
        GetAllData();

//        Post Data from Fatch Data of Service PAge
        if (getArguments() != null) {
            String name = getArguments().getString("ServiceName");
            String status = getArguments().getString("Status");

            if (name != null && status != null) {
                AddData(name, status);
            }
        }
//        Buttom click Event
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Servicefragment()).commit();
            }
        });



        return view;
    }
    void GetAllData() {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ServiceList.clear();

                                JSONArray dataArray = response.getJSONArray("List");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject jData = dataArray.getJSONObject(i);
                                    String id = jData.getString("_id");
                                    String serviceName = jData.getString("ServiceName");
                                    String status = jData.getString("status");
                                    String entrey = jData.optString("EntryDate", "");
                                    ServiceList.add(new ServiceType_model(id,serviceName, status, entrey));


                                }
                                ServiceType.notifyDataSetChanged();


                            } catch (Exception e) {
                                Toast.makeText(requireContext(), "JSON Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(requireContext());
            queue.add(request);

        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    void AddData(String Servicename, String status){
        try {
            JSONObject object = new JSONObject();
            object.put("ServiceName", Servicename);
            object.put("status", status);


            String currentTime = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(new Date());
            object.put("EntryDate", currentTime);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl, object,
                    response -> {
                        try {
                            JSONObject data = response.getJSONObject("Data");
                            String id = data.getString("_id");
                            String serviceName = data.getString("ServiceName");
                            String Status = data.getString("status");
                            String entry = data.optString("EntryDate", "");

                            ServiceList.add(new ServiceType_model(id,serviceName, Status, entry));
                            ServiceType.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        GetAllData();
                    },
                    error -> {
                        Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (Exception ex) {
            Toast.makeText(requireContext(), "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void update(String id, String serviceName, String status) {
        try {
            JSONObject object = new JSONObject();
            object.put("ServiceName", serviceName);
            object.put("status", status);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    ApiUrl + "/" + id,
                    object,
                    response -> {
                        Toast.makeText(requireContext(), "Service updated successfully", Toast.LENGTH_SHORT).show();
                        GetAllData();
                    },
                    error -> Toast.makeText(requireContext(), "Update failed: " + error.toString(), Toast.LENGTH_LONG).show()
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteService(String id, ServiceType_model service) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    ApiUrl + "/" + id,
                    null,
                    response -> {
                        Toast.makeText(requireContext(), "Service deleted successfully", Toast.LENGTH_SHORT).show();

                        ServiceList.remove(service);
                        ServiceType.notifyDataSetChanged();
                    },
                    error -> Toast.makeText(requireContext(), "Delete failed: " + error.toString(), Toast.LENGTH_LONG).show()
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
