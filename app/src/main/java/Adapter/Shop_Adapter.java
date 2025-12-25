package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.Shop_model;

public class Shop_Adapter extends ArrayAdapter<Shop_model> {
    private Context context;
    private int resource;
    private ArrayList<Shop_model> ShopList;



    public Shop_Adapter(@NonNull Context context, int resource, @NonNull ArrayList<Shop_model> ShopList) {
        super(context, resource, ShopList);
        this.context = context;
        this.resource = resource;
        this.ShopList = ShopList;

    }


    @Nullable
    @Override
    public Shop_model getItem(int position) {
        return ShopList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);

        // Get UI references
        TextView tvOwnerName = rowView.findViewById(R.id.tvOwnerName);
        TextView tvShopName = rowView.findViewById(R.id.tvShopName);
        TextView tvPhone = rowView.findViewById(R.id.tvPhone);
        TextView tvEmail = rowView.findViewById(R.id.tvEmail);
        TextView tvAddress = rowView.findViewById(R.id.tvAddress);
        TextView tvStatus = rowView.findViewById(R.id.tvStatus);
        ImageView tvprofile = rowView.findViewById(R.id.ivOwnerImage);

        // Get current item
        Shop_model model = ShopList.get(position);

        // Bind data
        tvOwnerName.setText( model.getOwnerName());
        tvShopName.setText("Shop Name: " +model.getShopName());
        tvPhone.setText("phone: " + model.getPhone());
        tvEmail.setText("Email: " + model.getEmail());
        tvAddress.setText("Address: " + model.getAddress());
        tvStatus.setText("Status: "+model.getStatus());
        Glide.with(context).load(model.getImg()).circleCrop().into(tvprofile);
        return rowView;
    }
}
