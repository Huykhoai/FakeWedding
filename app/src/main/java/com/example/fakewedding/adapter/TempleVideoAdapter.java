package com.example.fakewedding.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakewedding.R;
import com.example.fakewedding.model.TempleVideo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TempleVideoAdapter extends RecyclerView.Adapter<TempleVideoAdapter.ViewHolder> {
    Context context;
    ArrayList<TempleVideo> arrayList;

    public TempleVideoAdapter(Context context, ArrayList<TempleVideo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_temple_video, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          TempleVideo templeVideo = arrayList.get(position);
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.videoView);
        holder.videoView.setMediaController(mediaController);
        holder.videoView.setVideoURI(Uri.parse(templeVideo.getLink_video()));
        holder.videoView.start();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView txtStart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_temple_video);
            txtStart = itemView.findViewById(R.id.item_start_video);
        }
    }
}
