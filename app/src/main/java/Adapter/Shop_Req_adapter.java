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

import Model.ServiceType_model;
import Model.Shop_Req_model;

public class Shop_Req_adapter extends ArrayAdapter<Shop_Req_model> {
    private Context context;
    private int resource;
    private ArrayList<Shop_Req_model> RequestList;
    public interface OnItemClickListener {
        void onApprove(Shop_Req_model model);
        void onIgnore(Shop_Req_model model);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public Shop_Req_adapter(@NonNull Context context, int resource,  ArrayList<Shop_Req_model> requestList) {
        super(context, resource, requestList);
        this.context = context;
        this.resource = resource;
        RequestList = requestList;
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

        TextView shopname = rowView.findViewById(R.id.reqShopName);
        TextView ownername = rowView.findViewById(R.id.reqOwnerName);
        TextView contact = rowView.findViewById(R.id.reqcontect);
        TextView email = rowView.findViewById(R.id.reqemail);
        TextView address = rowView.findViewById(R.id.reqshopaddress);
        TextView status = rowView.findViewById(R.id.reqStatus);
        ImageView idproof = rowView.findViewById(R.id.reqIdProofImg);
        ImageView shopImg = rowView.findViewById(R.id.reqShopImg);
        ImageView OwnerImg = rowView.findViewById(R.id.reqOwnerImg);
        Button approve = rowView.findViewById(R.id.btnApprove);
        Button reject = rowView.findViewById(R.id.btnReject);

        Shop_Req_model model = RequestList.get(position);
        if (model == null) return rowView;

        shopname.setText(model.getShopname());
        ownername.setText(model.getOwnername());
        contact.setText(model.getContact());
        email.setText(model.getEmail());
        address.setText(model.getAddress());
        // Load images using Glide
        Glide.with(context).load(model.getIdProof()).into(idproof);
        Glide.with(context).load(model.getShopImg()).into(shopImg);
        Glide.with(context).load(model.getOwnerImg()).into(OwnerImg);
        status.setText(model.getStatus());


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onApprove(model);            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onIgnore(model);
            }
        });
        return rowView;
    }
}
