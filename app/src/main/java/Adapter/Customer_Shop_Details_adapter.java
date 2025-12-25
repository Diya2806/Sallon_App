package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.diya.sallonapp.R;
import com.diya.sallonapp.customer_Shop_Detailsfragment;

import java.util.ArrayList;
import java.util.List;

import Model.Customer_Shop_Details_model;

public class Customer_Shop_Details_adapter extends ArrayAdapter<Customer_Shop_Details_model> {

    private  Context context;
    private int resource;
    private ArrayList<Customer_Shop_Details_model> shopList;

    public Customer_Shop_Details_adapter(@NonNull Context context, int resource, ArrayList<Customer_Shop_Details_model> shopList) {
        super(context, resource, shopList);
        this.context = context;
        this.resource = resource;
        this.shopList = shopList;
    }

    @Nullable
    @Override
    public Customer_Shop_Details_model getItem(int position) {
        return super.getItem(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            // return super.getView(position, convertView, parent);
            LayoutInflater inflater=LayoutInflater.from(context);
            View rowView = inflater.inflate(resource, null);

            TextView shopName = rowView.findViewById(R.id.CustomershopName);
            TextView ShopDetails = rowView.findViewById(R.id.CustomerShopDetails);
            TextView OwnerName = rowView.findViewById(R.id.CustomerOwnerName);
            AppCompatRatingBar ratingBar = rowView.findViewById(R.id.Customershoprating);
            ImageView img = rowView.findViewById(R.id.shopImage);

            Customer_Shop_Details_model model = shopList.get(position);
            OwnerName.setText(model.getOwnerName());

            shopName.setText(model.getShopName());
            ShopDetails.setText(model.getShopDetails());
              ratingBar.setRating(model.getRating());
             Glide.with(context).load(model.getShopImg()).into(img);



            return rowView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
