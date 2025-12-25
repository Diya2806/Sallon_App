package com.diya.sallonapp;

import static android.app.Activity.RESULT_OK;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.Shop_Reg_Service_adapter;
import Model.Shop_Reg_service_model;

public class Shop_Reg_Servicefragment extends Fragment {

    private Spinner service, servicesub;
    private Button addservice;
    private ListView listView;
    private EditText Price, Title;
    private ImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private String userId;
    private String shopId = "";

    private final String url = "https://semicordate-wabbly-veda.ngrok-free.dev/shopservice/service";
    private final String ServiceType_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/serviceType";
    private final String ServiceSubType_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/ServiceSubType";
    private final String fetchUrl = "https://semicordate-wabbly-veda.ngrok-free.dev/shopservice";

    private Shop_Reg_Service_adapter adapter;
    private ArrayList<Shop_Reg_service_model> ServiceList;

    private String selectedServiceId = "";
    private String selectedSubServiceId = "";
    private String selectedServiceName = "";
    private String selectedSubServiceName = "";
    private boolean isPosting = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shop_reg_servicefragment, container, false);

        service = view.findViewById(R.id.spinnerRegServiceType);
        servicesub = view.findViewById(R.id.spinnerRegServiceSubType);
        img = view.findViewById(R.id.RegimgService);
        addservice = view.findViewById(R.id.btnRegAddService);
        listView = view.findViewById(R.id.listViewRegServices);
        Price = view.findViewById(R.id.addprice);
        Title = view.findViewById(R.id.addTitle);


        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        shopId = sharedPref.getString("shopId", "");

        // Image click listener
        img.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        // Setup ListView and Adapter
        ServiceList = new ArrayList<>();
        adapter = new Shop_Reg_Service_adapter(requireContext(), R.layout.custome_shop_reg_service, ServiceList);
        listView.setAdapter(adapter);


        GetAllData();

        resetForm();
        fetchAllShopServices();

        // Delete listener
        adapter.setOnDeleteClickListener((model, position) -> deleteShopService(model.getId(), position));

        // Add service button click
        addservice.setOnClickListener(v -> {
            if (isPosting) return; // Ignore multiple taps
            isPosting = true;
            String price = Price.getText().toString().trim();
            String title = Title.getText().toString().trim();

            if (imageUri == null) {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedServiceId.isEmpty() || selectedSubServiceId.isEmpty()) {
                Toast.makeText(getContext(), "Please select both Service and Sub-Service", Toast.LENGTH_SHORT).show();
                return;
            }
            postService(selectedServiceId, selectedSubServiceId, title, price, imageUri);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }



    // Convert Uri to byte[]
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    // Reset form
    private void resetForm() {
        img.setImageResource(R.drawable.add);
        imageUri = null;
        service.setSelection(0);
        servicesub.setSelection(0);
        Title.setText("");
        Price.setText("");
    }

    // Fetch all service types
    private void GetAllData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ServiceType_URL, null,
                response -> {
                    try {
                        ArrayList<String> names = new ArrayList<>();
                        ArrayList<String> ids = new ArrayList<>();

                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            ids.add(jData.getString("_id"));
                            names.add(jData.getString("ServiceName"));
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(),
                                android.R.layout.simple_spinner_item, names) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                ((TextView) view).setTextColor(Color.BLACK);
                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                ((TextView) view).setTextColor(Color.BLACK);
                                return view;
                            }
                        };
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service.setAdapter(spinnerAdapter);

                        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedServiceId = ids.get(position);
                                selectedServiceName = names.get(position);
                                GetAllSubServiceData(selectedServiceId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "JSON Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    // Fetch sub-services
    private void GetAllSubServiceData(String serviceTypeId) {
        String url = ServiceSubType_URL + "/" + serviceTypeId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ArrayList<String> names = new ArrayList<>();
                        ArrayList<String> ids = new ArrayList<>();
                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);
                            ids.add(jData.getString("_id"));
                            names.add(jData.getString("SubServiceName"));
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(),
                                android.R.layout.simple_spinner_item, names) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                ((TextView) view).setTextColor(Color.BLACK);
                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                ((TextView) view).setTextColor(Color.BLACK);
                                return view;
                            }
                        };

                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        servicesub.setAdapter(spinnerAdapter);

                        servicesub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedSubServiceId = ids.get(position);
                                selectedSubServiceName = names.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "JSON Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    // Fetch all shop services
    private void fetchAllShopServices() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fetchUrl, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("List");
                        ServiceList.clear();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            String id = obj.optString("_id");
                            String sShopId = obj.optString("ShopId", ""); // get ShopId of this service

                            // ✅ Only add services that belong to this shop
                            if (!sShopId.equals(shopId)) continue;
                            String serviceName = obj.optString("ServiceTypeName", "");
                            String subServiceName = obj.optString("ServiceSubTypeName", "");
                            String title = obj.optString("Title", "");
                            String price = obj.optString("Price", "");
                            String photoUrl = obj.optString("ServiceSubPhoto", "");

                            ServiceList.add(new Shop_Reg_service_model(id, serviceName, subServiceName, photoUrl, price, title));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing services: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Failed to load services: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    // Delete shop service
    private void deleteShopService(String serviceId, int position) {
        String deleteUrl = fetchUrl + "/" + serviceId;
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                response -> {
                    ServiceList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Service deleted successfully", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(getContext(), "Failed to delete: " + error.toString(), Toast.LENGTH_LONG).show()
        );
        Volley.newRequestQueue(requireContext()).add(deleteRequest);
    }

    // POST service with image
    private void postService(String serviceTypeID, String subTypeID, String title, String price, Uri imageUri) {
        try {
            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url,
                    response -> {
                        isPosting = false;
                        try {
                            String respStr = new String(response.data);
                            JSONObject respObj = new JSONObject(respStr);
                            String newId = respObj.optString("_id", "");
                            String message = respObj.optString("Message", "No response message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                            if (message.equals("Service added successfully")) {
                                ServiceList.add(new Shop_Reg_service_model(newId, selectedServiceName, selectedSubServiceName, imageUri.toString(), price, title));
                                adapter.notifyDataSetChanged();
                                resetForm();
                                fetchAllShopServices();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Response parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        isPosting = false;
                        Toast.makeText(getContext(), "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ShopId", shopId);
                    params.put("ServiceTypeID", serviceTypeID);
                    params.put("ServiceSubTypeId", subTypeID);
                    params.put("Title", title);
                    params.put("Price", price);
                    params.put("EntryDate", new java.text.SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> files = new HashMap<>();
                    try {
                        files.put("ServiceSubPhoto", new DataPart("service.jpg", getBytesFromUri(imageUri), "image/jpeg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return files;
                }
            };

            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                    10000,
                    0,
                    1f
            ));

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (Exception e) {
            e.printStackTrace();
            isPosting = false;
            Toast.makeText(getContext(), "AddData error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
