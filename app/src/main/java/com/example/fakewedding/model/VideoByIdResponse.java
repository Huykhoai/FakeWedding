package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VideoByIdResponse {
    @SerializedName("list_sukien_video")
    ArrayList<SwapVideo> arrayList;

    public VideoByIdResponse(ArrayList<SwapVideo> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<SwapVideo> getArrayList() {
        return arrayList;
    }
}
