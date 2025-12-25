package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import com.diya.sallonapp.R;

import java.util.ArrayList;

import Model.Appointment_model;

public class Appointment_Shop_History_adapter extends ArrayAdapter<Appointment_model> {

    private Context context;
    private ArrayList<Appointment_model> historyList;
    private int resource;

    public interface OnItemClickListener {
        void onApprove(Appointment_model model);
        void onIgnore(Appointment_model model);
        void onComplete(Appointment_model model);
    }
    private Appointment_Shop_History_adapter.OnItemClickListener listener;
    public void setOnItemClickListener(Appointment_Shop_History_adapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public Appointment_Shop_History_adapter(@NonNull Context context, int resource, ArrayList<Appointment_model> historyList) {
        super(context, resource, historyList);
        this.context = context;
        this.historyList = historyList;
        this.resource = resource;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);
        TextView customerName = rowView.findViewById(R.id.apCustomerName);
        TextView service = rowView.findViewById(R.id.apServiceName);
        TextView time = rowView.findViewById(R.id.apTime);
        TextView status = rowView.findViewById(R.id.apStatus);
        ImageView profile = rowView.findViewById(R.id.apimgCustomer);
        Button btnAction = rowView.findViewById(R.id.btnapAccept);
        Button btnReject = rowView.findViewById(R.id.btnapReject);
        TextView number = rowView.findViewById(R.id.apcustomernumber);


        Appointment_model model= historyList.get(position);
        String services ="Service : "+ model.getServiceName()+" \nSubservice : "+ model.getServicesub() +" \nTitle : "+ model.getTitle();

        customerName.setText(model.getCustomerName());
        service.setText(services);
        time.setText(model.getTime());
        status.setText(model.getStatus());
        number.setText(model.getNumber());
        Glide.with(context)
                .load(model.getImageUrl()) // URL from server
                .placeholder(R.drawable.person) // fallback while loading
                .circleCrop()
                .into(profile);



//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) listener.onApprove(model);
//            }
//        });
//        reject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) listener.onIgnore(model);
//            }
//        });
        // Handle buttons based on status
        switch (model.getStatus().toLowerCase()) {
            case "pending":
                btnAction.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                btnAction.setText("Approve");

                btnAction.setOnClickListener(v -> {
                    if(listener != null) listener.onApprove(model);
                });
                btnReject.setOnClickListener(v -> {
                    if(listener != null) listener.onIgnore(model);
                });
                break;

            case "approved":
                btnAction.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.GONE);
                btnAction.setText("Complete");

                btnAction.setOnClickListener(v -> {
                    if(listener != null) listener.onComplete(model);
                    Toast.makeText(context, "Complete clicked", Toast.LENGTH_SHORT).show();
                });
                break;

            case "rejected":
            case "completed":
                btnAction.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                break;
        }
        return rowView;
    }
}
