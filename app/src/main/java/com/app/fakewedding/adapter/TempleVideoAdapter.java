package com.app.fakewedding.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fakewedding.R;
import com.app.fakewedding.model.TempleVideo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;

public class TempleVideoAdapter extends RecyclerView.Adapter<TempleVideoAdapter.ViewHolder> {
    Context context;
    ArrayList<TempleVideo> arrayList;
    onCickStart onCickStart;
    private ExoPlayer exoPlayer;
    public TempleVideoAdapter(Context context, ArrayList<TempleVideo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }
    public void setOnclickStart(onCickStart onCickStart){
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
          TempleVideo templeVideo = arrayList.get(position);
              exoPlayer = new ExoPlayer.Builder(context).build();
              holder.videoView.setPlayer(exoPlayer);
              MediaItem mediaItem = MediaItem.fromUri(Uri.parse(templeVideo.getLink_video()));
              exoPlayer.addMediaItem(mediaItem);
              exoPlayer.setPlayWhenReady(false); // Đặt playWhenReady thành false để ngăn chặn video tự động phát

              exoPlayer.prepare();
              holder.txtStart.setOnClickListener(v -> {
            if(onCickStart!= null){
                onCickStart.onClick(templeVideo.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public interface onCickStart{
        void onClick(int id);
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
