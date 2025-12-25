package Adapter;

import android.content.Context;
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

import Model.ServiceType_model;

public class ServiceType_adapter extends ArrayAdapter<ServiceType_model> {
    private Context context;
    private int resource;
    private ArrayList<ServiceType_model> serviceList;
    public interface OnItemClickListener {
        void onEdit(ServiceType_model service);
        void onDelete(ServiceType_model service);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ServiceType_adapter(@NonNull Context context, int resource,  ArrayList<ServiceType_model> serviceList) {
        super(context, resource, serviceList);
        this.context = context;
        this.resource = resource;
        this.serviceList = serviceList;
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

        TextView ServiceName = rowView.findViewById(R.id.tvServiceName);
        TextView ServiceStatus = rowView.findViewById(R.id.tvServiceStatus);
        Button edit = rowView.findViewById(R.id.btnserviceEdit);
        Button delete = rowView.findViewById(R.id.btnserviceDelete);

        ServiceType_model model = serviceList.get(position);

        ServiceName.setText(model.getName());
        ServiceStatus.setText("Status: "+model.getStatus());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onEdit(model);            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onDelete(model);
            }
        });

        return rowView;
    }
}
