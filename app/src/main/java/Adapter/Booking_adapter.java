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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.Booking_model;

public class Booking_adapter extends ArrayAdapter<Booking_model> {
    private Context context;
    private int resource;
    private ArrayList<Booking_model> BookingList;

    public Booking_adapter(@NonNull Context context, int resource, ArrayList<Booking_model> bookingList) {
        super(context, resource, bookingList);
        this.context = context;
        this.resource = resource;
        BookingList = bookingList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);


        TextView customer = rowView.findViewById(R.id.bookingShopownerName);
        TextView shop= rowView.findViewById(R.id.bookingShopName);
        TextView status = rowView.findViewById(R.id.BookingStatus);
        ImageView img = rowView.findViewById(R.id.shopvBookingImage);
        TextView number = rowView.findViewById(R.id.bookingShopownerNumber);



        Booking_model model= BookingList.get(position);

        customer.setText(model.getOwnerName());
        shop.setText(model.getShopName());
        status.setText(model.getStatus());
        number.setText(model.getNumber());

        if (model.getStatus().equalsIgnoreCase("approved")) {
         status.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            status.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        Glide.with(context).load(model.getImg()).placeholder(R.drawable.person).circleCrop().into(img);



        return rowView;
    }
}
