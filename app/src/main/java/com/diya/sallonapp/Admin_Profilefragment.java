package com.diya.sallonapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Admin_Profilefragment extends Fragment {
    private Button logout;
    private ImageView adminImage;
    private ImageButton btnUpload;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private final String PREFS_NAME = "MyAdminPrefs";
    String BASE_URL ="https://semicordate-wabbly-veda.ngrok-free.dev/admin";
    String AdminID="";
    private TextView name,email,phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.admin_profilefragment, container, false);

        logout = view.findViewById(R.id.btnLogout);
        btnUpload = view.findViewById(R.id.btnUploadImage);
        adminImage = view.findViewById(R.id.adminImage);
        name =view.findViewById(R.id.adminName);
        email =view.findViewById(R.id.adminEmail);
        phone =view.findViewById(R.id.adminPhone);
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        AdminID = prefs.getString("AdminId", "");

        fetchCustomerData();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });
        return view;
    }
    private void fetchCustomerData() {
        if (AdminID == null) return;

        String url = BASE_URL + "/" + AdminID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("Data");
                        name.setText(data.getString("Name"));
                        email.setText(data.getString("Email"));
                        phone.setText(data.getString("Mobile"));

                        String imageUrl = data.optString("Image", "");
                        if (!imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.person)
                                    .circleCrop()
                                    .into(adminImage);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(getContext()).add(request);
    }
@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        imageUri = data.getData();

        // Preview selected image
        Glide.with(this)
                .load(imageUri)
                .circleCrop()
                .placeholder(R.drawable.person)
                .into(adminImage);

        if (AdminID != null && !AdminID.isEmpty() && imageUri != null) {
            uploadProfileImage(AdminID, imageUri);
        } else {
            Toast.makeText(getContext(), "Admin ID or Image missing", Toast.LENGTH_SHORT).show();
        }
    }
}

    private void uploadProfileImage(String adminId, Uri imageUri) {
        String url = BASE_URL + "/" + adminId + "/image";

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                response -> {
                    try {
                        String resultResponse = new String(response.data);
                        JSONObject result = new JSONObject(resultResponse);
                        JSONObject data = result.getJSONObject("Data");

                        if (data.has("Image")) {
                            String imageUrl = data.getString("Image");
                            Glide.with(this).load(imageUrl).circleCrop().into(adminImage);
                        }

                        Toast.makeText(requireContext(), "Image Updated Successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String err = new String(error.networkResponse.data);
                        Toast.makeText(requireContext(), "Server error: " + err, Toast.LENGTH_LONG).show();
                        android.util.Log.e("UPLOAD_ERROR", err);
                    } else {
                        Toast.makeText(requireContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (requireContext() != null && imageUri != null) {
                    try (InputStream iStream = requireContext().getContentResolver().openInputStream(imageUri)) {
                        if (iStream != null) {
                            byte[] inputData = getBytes(iStream);
                            params.put("image", new DataPart("profile.jpg", inputData, "image/jpeg")); // lowercase!
                        } else {
                            Toast.makeText(requireContext(), "Cannot open image stream", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(multipartRequest);
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
