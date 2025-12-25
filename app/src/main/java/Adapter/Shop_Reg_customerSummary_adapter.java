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

import Model.Shop_Reg_customerSummary_model;

public class Shop_Reg_customerSummary_adapter extends ArrayAdapter<Shop_Reg_customerSummary_model> {

    private Context context;
    private int resource;
    private ArrayList<Shop_Reg_customerSummary_model> CustomerList;

    public Shop_Reg_customerSummary_adapter(@NonNull Context context, int resource,  ArrayList<Shop_Reg_customerSummary_model> customerList) {
        super(context, resource, customerList);
        this.context = context;
        this.resource = resource;
        CustomerList = customerList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater inflater=LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);
        TextView name = rowView.findViewById(R.id.ShopRegcustomerName);
        TextView number = rowView.findViewById(R.id.ShopRegcustomerPhone);
        TextView address = rowView.findViewById(R.id.ShopRegcustomerAddress);
        TextView service = rowView.findViewById(R.id.ShopRegcustomerTotalServices);
        ImageView profile = rowView.findViewById(R.id.ShopRegcustomerProfile);

        Shop_Reg_customerSummary_model model = CustomerList.get(position);

        name.setText(model.getName());
        number.setText(model.getPhone());
        address.setText(model.getAddress());
        service.setText("Service Taken : "+String.valueOf(model.getTotalServices()));
        Glide.with(context).load(model.getProfileImg()).placeholder(R.drawable.person).circleCrop().into(profile);



        return rowView;
    }
}
