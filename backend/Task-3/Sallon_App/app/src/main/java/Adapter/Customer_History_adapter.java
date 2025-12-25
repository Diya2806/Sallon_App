package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.Customer_History_Model;

public class Customer_History_adapter extends ArrayAdapter<Customer_History_Model> {
    private Context context;
    private int resource;
    private ArrayList<Customer_History_Model> HistoryList;
    public interface OnItemClickListener{
        void onFeedBack(Customer_History_Model model);
    }
    private Customer_History_adapter.OnItemClickListener listener;
    public void setOnItemClickListene(Customer_History_adapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public Customer_History_adapter(@NonNull Context context, int resource,  ArrayList<Customer_History_Model> historyList) {
        super(context, resource, historyList);
        this.context = context;
        this.resource = resource;
        HistoryList = historyList;
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


        TextView shopName = rowView.findViewById(R.id.txtShopName);
        TextView service = rowView.findViewById(R.id.txtService);
        TextView date = rowView.findViewById(R.id.txtDate);
        TextView time = rowView.findViewById(R.id.txtTime);
        TextView status = rowView.findViewById(R.id.txtStatus);
        Button feeedback = rowView.findViewById(R.id.btnFeedback);
        Customer_History_Model model = HistoryList.get(position);

        shopName.setText(model.getShopName());
        service.setText(model.getService());
        date.setText(model.getDate());
        time.setText(model.getTime());
        status.setText(model.getStatus());
        if (model.getStatus().equalsIgnoreCase("completed")) {
            feeedback.setVisibility(View.VISIBLE);
            feeedback.setOnClickListener(v -> {
                // Handle feedback click
                if(listener != null) listener.onFeedBack(model);

            });
        } else {
            feeedback.setVisibility(View.GONE);
        }

        switch (model.getStatus()) {
            case "completed":
                status.setTextColor(Color.parseColor("#4CAF50")); // Green
                break;
            case "pending":
                status.setTextColor(Color.parseColor("#FFC107")); // Yellow
                break;
            case "rejected":
                status.setTextColor(Color.parseColor("#F44336")); // Red
                break;
        }
        return rowView;

    }
}
