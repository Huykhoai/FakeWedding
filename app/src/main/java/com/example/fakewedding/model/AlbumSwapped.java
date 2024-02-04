package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class AlbumSwapped {
    @SerializedName("loai_sukien")
    String loai_sukien;
    @SerializedName("id_sk_swap_album")
    String id_sk_album;
    @SerializedName("album")
    String album;
    @SerializedName("id_saved")
    String id_saved;
    @SerializedName("link_src_goc")
    String link_nam_goc;
    @SerializedName("link_tar_goc")
    String link_nu_goc;
    @SerializedName("link_da_swap")
    String link_da_swap;
    @SerializedName("id_user")
    int id_user;
    @SerializedName("thoigian_sukien")
    String thoigian_sukien;

    public String getThoigian_sukien() {
        return thoigian_sukien;
    }

    public String getLoai_sukien() {
        return loai_sukien;
    }

    public String getId_sk_album() {
        return id_sk_album;
    }

    public String getAlbum() {
        return album;
    }

    public String getId_saved() {
        return id_saved;
    }

    public String getLink_nam_goc() {
        return link_nam_goc;
    }

    public String getLink_nu_goc() {
        return link_nu_goc;
    }

    public String getLink_da_swap() {
        return link_da_swap;
    }

    public int getId_user() {
        return id_user;
    }
}
