package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diya.sallonapp.R;

import java.util.ArrayList;

import Model.Poster_model;

public class poster_adapter extends RecyclerView.Adapter<poster_adapter.PosterViewHolder> {
    private Context context;
    private ArrayList<Poster_model> posterList;

    public poster_adapter(Context context, ArrayList<Poster_model> posterList) {
        this.context = context;
        this.posterList = posterList;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_poster, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Poster_model model = posterList.get(position);
        holder.posterImage.setImageResource(model.getPoster()); // Assuming Poster_model has `getPosterImage()`
    }

    @Override
    public int getItemCount() {
        // max 5 posters only
        return Math.min(posterList.size(), 5);
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;

        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.posterImage); // id from custome_poster.xml
        }
    }
}
