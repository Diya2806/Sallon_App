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

import Model.Revenue_model;

public class Revenue_adapter extends ArrayAdapter<Revenue_model> {
    private Context context;
    private int resource;
    private ArrayList<Revenue_model> RevenueList;

    public Revenue_adapter(@NonNull Context context, int resource, ArrayList<Revenue_model> revenueList) {
        super(context, resource, revenueList);
        this.context = context;
        this.resource = resource;
        RevenueList = revenueList;
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

        TextView revCustomer = rowView.findViewById(R.id.revCustomer);
        TextView revShop = rowView.findViewById(R.id.revShop);
        TextView revtoday = rowView.findViewById(R.id.revService);
        TextView revmonth = rowView.findViewById(R.id.revTime);
        ImageView img = rowView.findViewById(R.id.revShopImg);


        Revenue_model model = RevenueList.get(position);

        revCustomer.setText(model.getOwnerName());
        revShop.setText("Shop Name: " + model.getShopName());
        revtoday.setText("Today Revenue: ₹ " + model.getTodayRevenue());
        revmonth.setText("Monthly Revenue: ₹ " + model.getMonthlyRevenue());
        Glide.with(context).load(model.getImg()).placeholder(R.drawable.person).circleCrop().into(img);



        return rowView;
    }
}
