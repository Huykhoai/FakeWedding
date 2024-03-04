package com.example.fakewedding.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;

import com.example.fakewedding.R;
import com.example.fakewedding.databinding.ActivityResultVideoBinding;
import com.example.fakewedding.model.SwapVideo;
import com.example.fakewedding.until.DownloadImage;
import com.example.fakewedding.until.DownloadVideo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class ResultVideoActivity extends AppCompatActivity {
      ActivityResultVideoBinding binding;
      SwapVideo swapVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultVideoBinding.inflate(getLayoutInflater());
        getData();
        ViewData();
        initView();
        setContentView(binding.getRoot());
    }

    private void initView() {
        binding.videoResultMenu.setOnClickListener(v -> {onBackPressed();
        });
        binding.resultVideoDownload.setOnClickListener(v -> {
            DownloadVideo.downloadVideo(ResultVideoActivity.this, "download", swapVideo.getLink_vid_da_swap());
        });
    }

    private void ViewData() {
        String imagePlace = swapVideo.getLink_image().replace("/var/www/build_futurelove","https://futurelove.online");
        Picasso.get().load(imagePlace).into(binding.imageSwappedVideo);

        ExoPlayer exoPlayer = new ExoPlayer.Builder(getApplicationContext()).build();
        binding.videoResultVideo.setPlayer(exoPlayer);
        MediaItem mediaItem1 = MediaItem.fromUri(swapVideo.getLink_video_da_swap()== null? swapVideo.getLink_vid_da_swap(): swapVideo.getLink_video_da_swap());
        exoPlayer.addMediaItem(mediaItem1);
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if(playbackState == Player.STATE_READY){
                    exoPlayer.setPlayWhenReady(true);
                }
            }
        });
        exoPlayer.prepare();
        exoPlayer.play();
        //
        ExoPlayer exoPlayer1 = new ExoPlayer.Builder(getApplicationContext()).build();
        binding.videoGocVideo.setPlayer(exoPlayer1);
        MediaItem mediaItem = MediaItem.fromUri(swapVideo.getLink_video_goc());
        exoPlayer1.addMediaItem(mediaItem);
        exoPlayer1.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
               if(playbackState == Player.STATE_READY){
                   exoPlayer1.setPlayWhenReady(true);
               }
            }
        });
        exoPlayer1.prepare();
        exoPlayer1.play();
    }
    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        swapVideo = (SwapVideo) bundle.getSerializable("sukienVideo");
        Log.d("Huy", "getData: "+swapVideo.getLink_vid_da_swap());
    }
}