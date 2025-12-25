package com.diya.sallonapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class Customer_Profilefragment extends Fragment {
    ImageView profileImage;
    ImageButton btnChangePhoto;
    TextView profileName, profileEmail, profileMobile;
    Button btnEditProfile, btnLogout;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String BASE_URL = "https://semicordate-wabbly-veda.ngrok-free.dev/customer";
    private String customerId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.customer_profilefragmnet,container,false);
        profileImage = view.findViewById(R.id.profileImage);
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileMobile = view.findViewById(R.id.profileMobile);
        btnEditProfile = view.findViewById(R.id.btncustomerEditProfile);
        btnLogout = view.findViewById(R.id.btncustomerLogout);
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        if (getArguments() != null) {
            customerId = getArguments().getString("customerId");
        }
        fetchCustomerData();
        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            if (customerId != null && imageUri != null) {
                uploadProfileImage(customerId, imageUri);
            } else {
                Toast.makeText(getContext(), "Customer ID not found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchCustomerData() {
        if (customerId == null) return;

        String url = BASE_URL + "/" + customerId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("Data");
                        profileName.setText(data.getString("Customername"));
                        profileEmail.setText(data.getString("email"));
                        profileMobile.setText(data.getString("phone"));

                        String imageUrl = data.optString("image", "");
                        if (!imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.person)
                                    .circleCrop()
                                    .into(profileImage);
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

    private void uploadProfileImage(String customerId, Uri imageUri) {
        String url = BASE_URL+"/profile/"+customerId+ "/image";

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                response -> {
                    try {
                        String resultResponse = new String(response.data);
                        JSONObject result = new JSONObject(resultResponse);
                        Toast.makeText(getContext(), "Image Updated Successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    InputStream iStream = getContext().getContentResolver().openInputStream(imageUri);
                    byte[] inputData = getBytes(iStream);
                    params.put("image", new DataPart("profile.jpg", inputData, "image/jpeg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(multipartRequest);
    }

    // Helper method to convert InputStream to byte[]
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
