package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailAlbum {
    @SerializedName("id_su_kien_swap_image")
    List<AlbumSwapped> detailAlbum;

    public List<AlbumSwapped> getDetailAlbum() {
        return detailAlbum;
    }
}
