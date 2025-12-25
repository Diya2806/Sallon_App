package com.diya.sallonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import Adapter.Shop_Req_adapter;
import Model.ServiceSubType_model;
import Model.Shop_Req_model;

public class Shop_Requestfragment extends Fragment {

    private ListView listView;
    private Shop_Req_adapter adapter;
    private ArrayList<Shop_Req_model> ReqList;

    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev";
    private String URL = BASE_URL + "/shopowner";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.shop_requestfragment, container, false);
        listView = view.findViewById(R.id.listViewShopRequests);

        ReqList = new ArrayList<>();
        adapter = new Shop_Req_adapter(requireContext(),R.layout.custome_shoprequest,ReqList);
        adapter.setOnItemClickListener(new Shop_Req_adapter.OnItemClickListener() {
            @Override
            public void onApprove(Shop_Req_model model) {
                updateData(model.getId(), "approved");

            }

            @Override
            public void onIgnore(Shop_Req_model model) {
                updateData(model.getId(), "ignored");
            }
        });

        listView.setAdapter(adapter);
        GetData();

        return view;

    }
void GetData(){
        try{
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,URL,null,
                    response->{
                        try {
                            ReqList.clear();

                            JSONArray dataArray = response.getJSONArray("List");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jData = dataArray.getJSONObject(i);
                                String Status = jData.optString("status", "pending");
                                // Only add pending requests
                                if (Status.equalsIgnoreCase("pending")) {
                                    String id = jData.isNull("_id") ? "" : jData.optString("_id");
                                    String ShopName = jData.optString("shopName");
                                    String OwnerName = jData.optString("Ownername");
                                    String Email = jData.optString("email");
                                    String Phone = jData.optString("phone");
                                    String State = jData.optString("state");
                                    String City = jData.optString("city");
                                    String Area = jData.optString("area");
                                    String Address = jData.optString("address");
                                    String Add = Address + "," + Area + "," + City + "," + State;
                                    String IdProof = jData.optString("idProof");
                                    String ShopImg = jData.optString("shopPhoto");
                                    String OwnerImg = jData.optString("ownerPhoto");


                                    // Add into list
                                    ReqList.add(new Shop_Req_model(id, ShopName, OwnerName, Phone, Email, Add, Status, IdProof, ShopImg, OwnerImg));

                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error->{
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                    });
            Volley.newRequestQueue(requireContext()).add(request);

        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
}


    void updateData(String id, String Status){
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("status", Status);  // send only status

            JsonObjectRequest putRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    URL + "/approve/" + id,  // match backend route
                    jsonBody,
                    response -> {
                        Toast.makeText(requireContext(), "Status updated to " + Status, Toast.LENGTH_SHORT).show();


                        // Remove the item locally from the list
                        for (int i = 0; i < ReqList.size(); i++) {
                            if (ReqList.get(i).getId().equals(id)) {
                                ReqList.remove(i);
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }

                    },
                    error -> {
                        if (error.networkResponse != null) {
                            Toast.makeText(requireContext(), "Error: " + new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            Volley.newRequestQueue(requireContext()).add(putRequest);

        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




}
