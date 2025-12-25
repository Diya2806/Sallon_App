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

import Model.City_model;

public class City_adapter extends ArrayAdapter<City_model> {
    private Context context;
    private int resource;
    private ArrayList<City_model> CityList;

    public City_adapter(@NonNull Context context, int resource, ArrayList<City_model> cityList) {
        super(context, resource, cityList);
        this.context = context;
        this.resource = resource;
        CityList = cityList;
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

        TextView city = rowView.findViewById(R.id.tvCityName);
        TextView state = rowView.findViewById(R.id.tvStateName);

        City_model model = CityList.get(position);

        city.setText(model.getCityname());
        state.setText(model.getStatename());
        return rowView;
    }
}
