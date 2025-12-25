package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.Shop_Reg_CustomerDetails;

public class Reg_customerDetails_adapter extends ArrayAdapter<Shop_Reg_CustomerDetails> {
    private Context context;
    private int resource;
    private ArrayList<Shop_Reg_CustomerDetails> HistoryList;

    public Reg_customerDetails_adapter(@NonNull Context context, int resource, ArrayList<Shop_Reg_CustomerDetails> historyList) {
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
        TextView name = rowView.findViewById(R.id.ReqserviceName);
        TextView Price = rowView.findViewById(R.id.ReqservicePrice);
        TextView time = rowView.findViewById(R.id.ReqserviceTime);
        Shop_Reg_CustomerDetails model = HistoryList.get(position);
        name.setText(model.getServiceName());
        Price.setText("Price :₹"+String.valueOf(model.getPrice()));
        time.setText("Date :"+model.getDate());
        return rowView;
    }
}
