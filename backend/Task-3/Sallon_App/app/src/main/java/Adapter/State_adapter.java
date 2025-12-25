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

import Model.State_model;

public class State_adapter extends ArrayAdapter<State_model> {

    private Context context;
    private int resource;
    private ArrayList<State_model> StateList;
    public State_adapter(@NonNull Context context, int resource, ArrayList<State_model> stateList) {
        super(context, resource, stateList);
        this.context = context;
        this.resource = resource;
        StateList = stateList;
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

       TextView State = rowView.findViewById(R.id.tvStateName);

       State_model model = StateList.get(position);

       State.setText(model.getStateName());

        return rowView;
    }
}
