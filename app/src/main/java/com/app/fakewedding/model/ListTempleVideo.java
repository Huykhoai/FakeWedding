package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListTempleVideo {
    @SerializedName("list_sukien_video_wedding")
    ArrayList<TempleVideo> list;

    public List<TempleVideo> getList() {
        return list;
    }

}
