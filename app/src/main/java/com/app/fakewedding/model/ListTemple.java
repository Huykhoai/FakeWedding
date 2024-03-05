package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
