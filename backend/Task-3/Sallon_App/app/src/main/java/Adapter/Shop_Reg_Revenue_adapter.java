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

import Model.Shop_Reg_Revenue_model;

public class Shop_Reg_Revenue_adapter extends ArrayAdapter<Shop_Reg_Revenue_model> {

    private Context context;
    private int resource;
    private ArrayList<Shop_Reg_Revenue_model> RevenueList;

    public Shop_Reg_Revenue_adapter(@NonNull Context context, int resource,  ArrayList<Shop_Reg_Revenue_model> revenueList) {
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

        TextView name = rowView.findViewById(R.id.shoprevenuecustomername);
        TextView time = rowView.findViewById(R.id.shoprevenuetime);
        TextView amount = rowView.findViewById(R.id.shoprevenueAmount);
        ImageView proflie = rowView.findViewById(R.id.shoprevenueimg);

        Shop_Reg_Revenue_model model = RevenueList.get(position);
        name.setText(model.getCustomerName());
        time.setText(model.getDateTime());
        amount.setText("₹ "+model.getAmount());
        Glide.with(context).load(model.getProfile()).circleCrop().into(proflie);

        return rowView;
    }
}
