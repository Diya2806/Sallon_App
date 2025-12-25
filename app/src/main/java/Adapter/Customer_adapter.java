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

import Model.Customer_model;


public class Customer_adapter extends ArrayAdapter<Customer_model> {
    private Context context;
    private int resource;
    private ArrayList<Customer_model> CustomerList;

    public Customer_adapter(@NonNull Context context, int resource,  ArrayList<Customer_model> CustomerList) {
        super(context, resource, CustomerList);
        this.context = context;
        this.resource = resource;
        this.CustomerList = CustomerList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);


        TextView Customername = rowView.findViewById(R.id.tvCustomerName);
        TextView Customerphone = rowView.findViewById(R.id.tvcustomerPhone);
        TextView CustomerEmail = rowView.findViewById(R.id.tvcustomerEmail);
        TextView CustomerAddress = rowView.findViewById(R.id.tvcustomerAddress);
        ImageView img = rowView.findViewById(R.id.ivCustomerImage);


        Customer_model model = CustomerList.get(position);


        Customername.setText(model.getCustomerName());
        Customerphone.setText("Phone: "+model.getPhone());
        CustomerEmail.setText("Email: "+model.getEmail());
        CustomerAddress.setText("Address: "+model.getAddress());
        Glide.with(context).load(model.getImg()).placeholder(R.drawable.person).circleCrop().into(img);


        return rowView;
    }
}
