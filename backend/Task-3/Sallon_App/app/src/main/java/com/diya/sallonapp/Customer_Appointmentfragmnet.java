    package com.diya.sallonapp;

    import android.app.DatePickerDialog;
    import android.app.TimePickerDialog;
    import android.graphics.Color;
    import android.icu.util.Calendar;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.TextView;
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
    import com.bumptech.glide.Glide;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;

    public class Customer_Appointmentfragmnet extends Fragment {
        private AutoCompleteTextView Shop;
        private  String ServiceType_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shop/";
        private  String ServiceSubType_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/ServiceSubType";
        private EditText date,Time;
        private Button btn;
        String Price="";
        private Spinner service,servicesub;
        private String selectedServiceId = "";
        private String selectedSubServiceId = "";
        private String selectedServiceName = "";
        private String selectedSubServiceName = "";
        private String Shop_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner/shoplist";
        private String selectedShopId = "";
        private String Serviceurl = "https://semicordate-wabbly-veda.ngrok-free.dev/shopowner";

        private String selectedShopName = "";
        private TextView price,title;
        private ImageView img;
        private String customerId = "";
        private String GetPrice_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/shopservice/getPrice";
        private String Booking = "https://semicordate-wabbly-veda.ngrok-free.dev/Booking";

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //        return super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.customer_bookingfragment,container,false);
            date = view.findViewById(R.id.customerDate);
            Time =view.findViewById(R.id.customerTime);
            Shop = view.findViewById(R.id.shopSearch);
            service = view.findViewById(R.id.spinnerService);
            servicesub =view.findViewById(R.id.spinnerSubService);
            price = view.findViewById(R.id.tvPrice);
            img = view.findViewById(R.id.serviceImage);
            title = view.findViewById(R.id.tvTitle);
            btn = view.findViewById(R.id.btnBook);


            Bundle bundle1 = getArguments();
            if (bundle1 != null) {
                customerId = bundle1.getString("customerId", "");
                selectedShopName = bundle1.getString("ShopName");
                selectedShopId = bundle1.getString("shopId");
            }

            GetAllShops();



            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {

                                String dates = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                date.setText(dates);
                            }, year, month, day);


                    datePickerDialog.getDatePicker().setCalendarViewShown(true);
                    datePickerDialog.show();
                }
            });

            Time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            (view12, selectedHour, selectedMinute) -> {
                                String amPm = (selectedHour >= 12) ? "PM" : "AM";
                                int hour12 = selectedHour % 12;
                                if (hour12 == 0) hour12 = 12;
                                String times = String.format("%02d:%02d %s", hour12, selectedMinute, amPm);
                                Time.setText(times);
                            }, hour, minute, false); // true = 24hr, false = 12hr AM/PM

                    timePickerDialog.show();
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customerId.isEmpty() || selectedShopId.isEmpty() || selectedServiceId.isEmpty() ||
                            selectedSubServiceId.isEmpty() || date.getText().toString().isEmpty() ||
                            Time.getText().toString().isEmpty()) {
                        Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BookoingData(customerId,  selectedShopId, selectedServiceId, selectedSubServiceId, date.getText().toString(), Time.getText().toString());
                }
            });
            GetAllData(selectedShopId);
            return view;
        }

        void GetAllData(String shopid) {
            try {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ServiceType_URL+shopid, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    ArrayList<String> names = new ArrayList<>();
                                    ArrayList<String> ids = new ArrayList<>();

                                    JSONArray dataArray = response.getJSONArray("List");

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject jData = dataArray.getJSONObject(i);
                                        String id = jData.getString("_id");
                                        String serviceName = jData.getString("ServiceTypeName");
                                        ids.add(id);
                                        names.add(serviceName);
                                    }
                                    // Custom ArrayAdapter to enforce black text
                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, names) {
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            View view = super.getView(position, convertView, parent);
                                            ((TextView) view).setTextColor(Color.BLACK); // selected item text color
                                            return view;
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            ((TextView) view).setTextColor(Color.BLACK); // dropdown items text color
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

        void GetAllSubServiceData(String serviceTypeId) {
            try {
                String url = ServiceSubType_URL + "/" + serviceTypeId;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            try {
                                ArrayList<String> names = new ArrayList<>();
                                ArrayList<String> ids = new ArrayList<>();

                                JSONArray dataArray = response.getJSONArray("List");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject jData = dataArray.getJSONObject(i);
                                    String id = jData.getString("_id");
                                    String subServiceName = jData.getString("SubServiceName");
                                    names.add(subServiceName);
                                    ids.add(id);
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

                                        GetPriceAndTitle(selectedShopId,selectedServiceId,selectedSubServiceId);


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

                RequestQueue queue = Volley.newRequestQueue(requireContext());
                queue.add(request);

            } catch (Exception e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

void GetAllShops() {
    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Shop_URL, null,
            response -> {
                try {
                    ArrayList<String> shopNames = new ArrayList<>();
                    ArrayList<String> shopIds = new ArrayList<>();

                    JSONArray dataArray = response.getJSONArray("List");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject shop = dataArray.getJSONObject(i);
                        shopIds.add(shop.getString("_id"));
                        shopNames.add(shop.getString("shopName"));
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            shopNames
                    );
                    Shop.setAdapter(adapter);


                    if (selectedShopName != null && !selectedShopName.isEmpty()) {
                        for (int i = 0; i < shopNames.size(); i++) {
                            if (shopNames.get(i).equalsIgnoreCase(selectedShopName)) {
                                selectedShopId = shopIds.get(i);
                                Shop.setText(selectedShopName, false);
                                break;
                            }
                        }

                        if (selectedShopId != null && !selectedShopId.isEmpty()) {
                            Toast.makeText(requireContext(), "Pre-selected shop: " + selectedShopName, Toast.LENGTH_SHORT).show();
                            GetAllData(selectedShopId); // Load services for that shop
                        }
                    }

                    // ✅ When user selects from dropdown
                    Shop.setOnItemClickListener((parent, view, position, id) -> {
                        selectedShopId = shopIds.get(position);
                        selectedShopName = shopNames.get(position);

                        Toast.makeText(requireContext(),
                                "Selected shop: " + selectedShopName + "\nID: " + selectedShopId,
                                Toast.LENGTH_SHORT).show();

                        GetAllData(selectedShopId); // Fetch services/subservices now
                    });

                    // ✅ When user types manually and leaves the box
                    Shop.setOnFocusChangeListener((v, hasFocus) -> {
                        if (!hasFocus) {
                            String typedName = Shop.getText().toString().trim();
                            boolean found = false;
                            for (int i = 0; i < shopNames.size(); i++) {
                                if (shopNames.get(i).equalsIgnoreCase(typedName)) {
                                    selectedShopId = shopIds.get(i);
                                    selectedShopName = shopNames.get(i);
                                    found = true;
                                    break;
                                }
                            }

                            if (found) {
                                Toast.makeText(requireContext(),
                                        "Matched shop: " + selectedShopName,
                                        Toast.LENGTH_SHORT).show();
                                GetAllData(selectedShopId); // fetch data after typing
                            } else {
                                selectedShopId = "";
                                selectedShopName = "";
                                Toast.makeText(requireContext(),
                                        "Please select a valid shop name!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(requireContext(),
                            "JSON Parse Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            },
            error -> Toast.makeText(requireContext(),
                    "Volley Error: " + error.toString(),
                    Toast.LENGTH_LONG).show()
    );

    Volley.newRequestQueue(requireContext()).add(request);
}

        void GetPriceAndTitle(String shopId, String serviceId, String subServiceId) {
            try{

                shopId = shopId.trim();
                serviceId = serviceId.trim();
                subServiceId = subServiceId.trim();


                String url = GetPrice_URL + "/" + shopId + "/" + serviceId + "/" + subServiceId;


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            try {
                                String t = response.getString("Title");
                                String p = response.getString("Price");
                                String imgUrl = response.optString("ServiceSubPhoto", "");

                                title.setText(t);
                                price.setText("Price: ₹" + p);
                                Price= p;

                                if (!imgUrl.isEmpty()) {
                                    img.setVisibility(View.VISIBLE);
                                    Glide.with(requireContext())
                                            .load(imgUrl)
                                            .into(img);
                                } else {
                                    img.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                Toast.makeText(requireContext(), "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        },
                        error -> {

                            Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                );

                Volley.newRequestQueue(requireContext()).add(request);
            } catch (Exception e) {
                Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        }

        void BookoingData(String CustomerId, String ShopID,String ServiceId,String ServicesubId,String Date,String Time){
            try{
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("CustomerId",CustomerId);
                jsonBody.put("ShopId",ShopID);
                jsonBody.put("ServiceId",ServiceId);
                jsonBody.put("ServiceSubID",ServicesubId);
                jsonBody.put("DateBook",Date);
                jsonBody.put("TimeBook",Time);
                jsonBody.put("Title", title.getText().toString());
                jsonBody.put("Price", Price);
                jsonBody.put("status","pending");


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,Booking+"/Book",jsonBody,
                        response->{
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer, new Customer_Homefragment()).commit();
                            Toast.makeText(requireActivity(), "Registered Successfully!", Toast.LENGTH_LONG).show();
                        },
                        error->{
                            Toast.makeText(requireActivity(), "error", Toast.LENGTH_LONG).show();

                        });
                RequestQueue queue = Volley.newRequestQueue(requireActivity());
                queue.add(request);
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
