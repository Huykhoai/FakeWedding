package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllVideoResponse {
    @SerializedName("list_sukien_video_wedding")
    ArrayList<SwapVideo> list;

    public AllVideoResponse(ArrayList<SwapVideo> list) {
        this.list = list;
    }

    public ArrayList<SwapVideo> getList() {
        return list;
    }
}
