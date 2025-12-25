package Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.Shop_Reg_service_model;

public class Shop_Reg_Service_adapter extends ArrayAdapter<Shop_Reg_service_model> {
    private Context context;
    private int resource;
    private ArrayList<Shop_Reg_service_model> ServiceList;

    public interface OnDeleteClickListener {
        void onDeleteClick(Shop_Reg_service_model model, int position);
    }

    private OnDeleteClickListener deleteListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public Shop_Reg_Service_adapter(@NonNull Context context, int resource,  ArrayList<Shop_Reg_service_model> serviceList) {
        super(context, resource, serviceList);
        this.context = context;
        this.resource = resource;
        ServiceList = serviceList;
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


        ImageView img = rowView.findViewById(R.id.RegServiceimg);
        TextView Service = rowView.findViewById(R.id.RegserviceType);
        TextView ServiceSub = rowView.findViewById(R.id.RegserviceSubType);
        TextView Price = rowView.findViewById(R.id.RegservicePrice);
        TextView Title = rowView.findViewById(R.id.RegserviceTitle);
        Button delete = rowView.findViewById(R.id.RegServiceDeletebtn);

        Shop_Reg_service_model model = ServiceList.get(position);

        if (model.getImageResId() != null && !model.getImageResId().isEmpty()) {
            Glide.with(context)
                    .load(model.getImageResId())
                    .error(R.drawable.add)
                    .into(img);
        } else {
            img.setImageResource(R.drawable.add);
        }
        Service.setText(model.getServiceType());
        ServiceSub.setText(model.getServiceSubType());
        Price.setText(model.getPrice());
        Title.setText(model.getTitle());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(model, position);
                }
            }
        });

        return rowView;
    }
}
