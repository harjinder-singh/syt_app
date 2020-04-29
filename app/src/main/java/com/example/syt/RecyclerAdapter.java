package com.example.syt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syt.fetch.SearchImageResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {
    private List<SearchImageResponse> images;
    private LayoutInflater mInflater;

    public RecyclerAdapter(Context context, List<SearchImageResponse> images){
        this.mInflater = LayoutInflater.from(context);
        this.images = images;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.album_layout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        SearchImageResponse image_id = images.get(position);
        Picasso.get().load("http://showyourtalent.herokuapp.com/media/" + image_id.getPic()).into(holder.album);
        holder.albumTitle.setText(image_id.getDescription());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView album;
        TextView albumTitle;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.album);
            albumTitle = itemView.findViewById(R.id.album_title);
        }

    }
}
