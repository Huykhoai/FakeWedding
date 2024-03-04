package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class SukienVideoResponse {
    @SerializedName("sukien_video")
    private SwapVideo sukienVideo;

    public SwapVideo getSukienVideo() {
        return sukienVideo;
    }

    public void setSukienVideo(SwapVideo sukienVideo) {
        this.sukienVideo = sukienVideo;
    }
}
