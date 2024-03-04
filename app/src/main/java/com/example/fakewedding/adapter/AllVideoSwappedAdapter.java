package com.example.fakewedding.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakewedding.R;
import com.example.fakewedding.model.SwapVideo;
import com.example.fakewedding.model.TempleVideo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;

public class AllVideoSwappedAdapter extends RecyclerView.Adapter<AllVideoSwappedAdapter.ViewHolder> {
    Context context;
    ArrayList<SwapVideo> arrayList;
    onCickStart onCickStart;
    private ExoPlayer exoPlayer;
    public AllVideoSwappedAdapter(Context context, ArrayList<SwapVideo> arrayList, onCickStart onCickStart) {
        this.context = context;
        this.arrayList = arrayList;
        this.onCickStart = onCickStart;
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
        SwapVideo templeVideo = arrayList.get(position);
        exoPlayer = new ExoPlayer.Builder(context).build();
        holder.videoView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(templeVideo.getLink_video_da_swap()));
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.setPlayWhenReady(false); // Đặt playWhenReady thành false để ngăn chặn video tự động phát
        exoPlayer.prepare();

        holder.txtStart.setText("View Detail");
        holder.txtStart.setOnClickListener(v -> {
            if(onCickStart!= null){
                onCickStart.onClick(templeVideo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public interface onCickStart{
        void onClick(SwapVideo swapVideo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        StyledPlayerView videoView;
        TextView txtStart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_temple_video);
            txtStart = itemView.findViewById(R.id.item_start_video);
        }
    }
}
