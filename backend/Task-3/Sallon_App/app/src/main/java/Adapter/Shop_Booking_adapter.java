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

import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.Shop_Booking_model;

public class Shop_Booking_adapter extends ArrayAdapter<Shop_Booking_model> {
    private Context context;
    private int resource;
    private ArrayList<Shop_Booking_model> BookingList;

    public Shop_Booking_adapter(@NonNull Context context, int resource,  ArrayList<Shop_Booking_model> bookingList) {
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
        LayoutInflater inflater=LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);

        TextView name = rowView.findViewById(R.id.bookingName);
        ImageView profile = rowView.findViewById(R.id.bookingimgProfile);
        TextView number = rowView.findViewById(R.id.bookingNumber);
        TextView time = rowView.findViewById(R.id.bookingTime);
        TextView status = rowView.findViewById(R.id.bookingStatus);

        Shop_Booking_model model = BookingList.get(position);

        name.setText(model.getName());
        profile.setImageResource(model.getProfile());
        number.setText(model.getNumber());
        time.setText(model.getTime());
        status.setText(model.getStatus());

        return rowView;
    }
}
