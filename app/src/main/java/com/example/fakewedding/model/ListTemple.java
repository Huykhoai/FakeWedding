package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListTemple {
    @SerializedName("list_sukien_video")
    ArrayList<Temple> getListTemple;

    public ArrayList<Temple> getGetListTemple() {
        return getListTemple;
    }

    public void setGetListTemple(ArrayList<Temple> getListTemple) {
        this.getListTemple = getListTemple;
    }
}
