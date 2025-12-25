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
    import com.android.volley.toolbox.JsonObjectRequest;
    import com.android.volley.toolbox.Volley;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;

    import Adapter.ServiceSubType_adapter;
    import Model.ServiceSubType_model;

    public class ServiceSubList_fragment extends Fragment {
        private ServiceSubType_adapter adapter;
        private ArrayList<ServiceSubType_model> ServiceSubLIst;
        private ListView listView;
        private Button btn;
        private String ApiUrl = "https://semicordate-wabbly-veda.ngrok-free.dev/ServiceSubType";

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //        return super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.servicesublistfragment,container,false);
//            initilization
            listView = view.findViewById(R.id.listViewServiceSub);
            btn =view.findViewById(R.id.btnservicesublistadd);
            ServiceSubLIst = new ArrayList<>();
            adapter = new ServiceSubType_adapter(requireContext(),R.layout.custome_servicesubtype,ServiceSubLIst);
            listView.setAdapter(adapter);
            getParentFragmentManager().setFragmentResultListener("addSubService", this, (requestKey, bundle) -> {
                String subServiceName = bundle.getString("subServiceName");
                String serviceTypeId = bundle.getString("serviceTypeId"); // ID for backend
                String mainService = bundle.getString("mainService"); // name for display
                String status = bundle.getString("status");

                PostData(subServiceName, serviceTypeId, mainService, status);
            });

            getParentFragmentManager().setFragmentResultListener("updateSubService", this, (requestKey, bundle) -> {
                String id = bundle.getString("updateId");
                String subServiceName = bundle.getString("updateSubServiceName");
                String serviceTypeId = bundle.getString("updateServiceTypeId");
                String status = bundle.getString("updateStatus");

                updateSubService(id, subServiceName, serviceTypeId, status);
            });


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.menucontainer, new ServiceSubTypefragment(), "ServiceSubTypeFragment")
                            .addToBackStack(null)
                            .commit();
                }
            });


            adapter.setOnItemClickListener(new ServiceSubType_adapter.OnItemClickListener() {
                @Override
                public void onEdit(ServiceSubType_model service) {
                    Bundle bundle = new Bundle();
                    bundle.putString("editId", service.getId());
                    bundle.putString("editMainService", service.getMainService());
                    bundle.putString("editSubServiceName", service.getSubServiceName());
                    bundle.putString("editStatus", service.getStatus());

                    ServiceSubTypefragment fragment = new ServiceSubTypefragment();
                    fragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.menucontainer, fragment, "ServiceSubTypeFragment")
                            .addToBackStack(null)
                            .commit();
                }

                @Override
                public void onDelete(ServiceSubType_model service) {
                    Toast.makeText(requireContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                    deleteSubService(service.getId());
                }
            });
            GetAllData();

            return view;
        }


void GetAllData() {
    try {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiUrl, null,
                response -> {
                    try {
                        ServiceSubLIst.clear();
                        JSONArray dataArray = response.getJSONArray("List");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jData = dataArray.getJSONObject(i);

                            String id = jData.getString("_id");
                            String subServiceName = jData.getString("SubServiceName");
                            String status = jData.getString("Status");
                            String entryDate = jData.optString("EntryDate", "");
                            String  mainService =jData.optString("ServiceTypeName", "");




                            // Add into list
                            ServiceSubLIst.add(new ServiceSubType_model(id, mainService, subServiceName, status, entryDate));

                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "JSON Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);

    } catch (Exception e) {
        Toast.makeText(requireContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
void PostData(String subServiceName, String serviceTypeId, String mainService, String status) {
            try {
                JSONObject object = new JSONObject();
                object.put("SubServiceName", subServiceName);
                object.put("ServiceTypeId", serviceTypeId);
                object.put("Status", status);


                String currentTime = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
                object.put("EntryDate", currentTime);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiUrl, object,
                        response -> {
                            try {
                                JSONObject data = response.getJSONObject("Data");
                                String id = data.getString("_id");
                                String subName = data.getString("SubServiceName");
                                String Status = data.getString("Status");
                                String entry = data.optString("EntryDate", "");


                                ServiceSubLIst.add(new ServiceSubType_model(id, mainService, subName, Status, entry));
                                adapter.notifyDataSetChanged();


                                Toast.makeText(requireContext(), "Sub Service Added Successfully", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
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

        public void updateSubService(String id, String subServiceName, String serviceTypeId, String status) {
            try {
                JSONObject object = new JSONObject();
                object.put("SubServiceName", subServiceName);
                object.put("ServiceTypeId", serviceTypeId);
                object.put("Status", status);

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.PUT,
                        ApiUrl + "/" + id, // PUT to specific sub-service ID
                        object,
                        response -> {
                            Toast.makeText(requireContext(), "Sub Service updated successfully", Toast.LENGTH_SHORT).show();
                            GetAllData(); // refresh the ListView
                        },
                        error -> Toast.makeText(requireContext(), "Update failed: " + error.toString(), Toast.LENGTH_LONG).show()
                );

                Volley.newRequestQueue(requireContext()).add(request);

            } catch (Exception e) {
                Toast.makeText(requireContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        private void deleteSubService(String id) {
            try {
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.DELETE,
                        ApiUrl + "/" + id,
                        null,
                        response -> {
                            Toast.makeText(requireContext(), "Sub Service deleted successfully", Toast.LENGTH_SHORT).show();
                            GetAllData(); // refresh the ListView
                        },
                        error -> Toast.makeText(requireContext(), "Delete failed: " + error.toString(), Toast.LENGTH_LONG).show()
                );

                Volley.newRequestQueue(requireContext()).add(request);

            } catch (Exception e) {
                Toast.makeText(requireContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }
