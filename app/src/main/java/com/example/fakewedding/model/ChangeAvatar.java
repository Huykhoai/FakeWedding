package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class ChangeAvatar {
    @SerializedName("link_img")
    String image;

    public String getImage() {
        return image;
    }
}
