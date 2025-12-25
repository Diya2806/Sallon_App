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

import Model.Report_model;

public class Report_adapter extends ArrayAdapter<Report_model> {
    private Context context;
    private int resource;
    private ArrayList<Report_model> ReportList;

    public Report_adapter(@NonNull Context context, int resource, ArrayList<Report_model> reportList) {
        super(context, resource, reportList);
        this.context = context;
        this.resource = resource;
        ReportList = reportList;
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

       TextView shopname = rowView.findViewById(R.id.reportShopName);
        TextView OwnerName = rowView.findViewById(R.id.reportOwnerName);
        TextView totalCustomer = rowView.findViewById(R.id.reportTotalCustomers);
        TextView totalService = rowView.findViewById(R.id.tvTotalServices);
        TextView revenue = rowView.findViewById(R.id.reportTotalRevenue);
        TextView month = rowView.findViewById(R.id.reportmonth);
        ImageView img = rowView.findViewById(R.id.reportShopImg);

        Report_model model = ReportList.get(position);
        shopname.setText(model.getShopName());
        OwnerName.setText("Owner Name"+model.getOwnerName());
        totalCustomer.setText("Customers: " + model.getTotalCustomers());
        totalService.setText("Services: " +model.getTotalServices());
        revenue.setText("Revenue: ₹ "+model.getTotalRevenue());
        month.setText("Month: "+model.getMonthYear());
        Glide.with(context).load(model.getImg()).circleCrop().placeholder(R.drawable.person).into(img);



        return rowView;
    }
}
