package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.diya.sallonapp.R;

import java.util.ArrayList;
import java.util.List;

import Model.ShopFeedback_model;
import Model.Shop_Req_model;

public class ShopFeedback_adapter extends ArrayAdapter<ShopFeedback_model> {
    private Context context;
    private int resource;
    private ArrayList<ShopFeedback_model> FeedbackList;

    public ShopFeedback_adapter(@NonNull Context context, int resource,ArrayList<ShopFeedback_model> feedbackList) {
        super(context, resource, feedbackList);
        this.context = context;
        this.resource = resource;
        FeedbackList = feedbackList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//           return super.getView(position, convertView, parent);
        LayoutInflater inflater=LayoutInflater.from(context);
        View rowView = inflater.inflate(resource, parent, false);

        ImageView profile = rowView.findViewById(R.id.feedimgCustomer);
        TextView customer = rowView.findViewById(R.id.feedCustomerName);
        TextView feed = rowView.findViewById(R.id.FeedbackMessage);
        RatingBar rate =rowView.findViewById(R.id.feedratingBar);
        Button reply = rowView.findViewById(R.id.btnfeedReply);


        ShopFeedback_model model=FeedbackList.get(position);
        Glide.with(context).load(model.getImageResId()).circleCrop().into(profile);
        customer.setText(model.getCustomerName());
        feed.setText(model.getFeedback());
        rate.setRating(model.getRating());
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rowView;
    }
}
