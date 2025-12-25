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

import Model.ServiceSubType_model;
import Model.ServiceType_model;

public class ServiceSubType_adapter extends ArrayAdapter<ServiceSubType_model> {

    private Context context;
    private int resource;
    private ArrayList<ServiceSubType_model> ServiceSubTypeList;
    public interface OnItemClickListener {
        void onEdit(ServiceSubType_model service);
        void onDelete(ServiceSubType_model service);
    }

    private ServiceSubType_adapter.OnItemClickListener listener;

    public void setOnItemClickListener(ServiceSubType_adapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ServiceSubType_adapter(@NonNull Context context, int resource, ArrayList<ServiceSubType_model> serviceSubTypeList) {
        super(context, resource, serviceSubTypeList);
        this.context = context;
        this.resource = resource;
        ServiceSubTypeList = serviceSubTypeList;
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

        TextView MainService = rowView.findViewById(R.id.MainService);
        TextView SubService = rowView.findViewById(R.id.SubServiceName);
        TextView Status = rowView.findViewById(R.id.SubServiceStatus);
        Button edit = rowView.findViewById(R.id.btnservicesubEdit);
        Button delete = rowView.findViewById(R.id.btnservicesubDelete);

        ServiceSubType_model model= ServiceSubTypeList.get(position);

        MainService.setText(model.getMainService());
        SubService.setText(model.getSubServiceName());
        Status.setText("Status: "+ model.getStatus());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onEdit(model);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onDelete(model);
            }
        });
        return rowView;
    }

}
