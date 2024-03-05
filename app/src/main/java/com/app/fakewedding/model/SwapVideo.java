package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SwapVideo implements Serializable {
    @SerializedName("id_saved")
    private String id_saved;
    @SerializedName("link_video_goc")
    String link_video_goc;
    @SerializedName("link_image")
    String link_image;
    @SerializedName("link_vid_da_swap")
    String link_vid_da_swap;
    @SerializedName("link_video_da_swap")
    String link_video_da_swap;
    @SerializedName("thoigian_sukien")
    String thoigian_sukien;
    @SerializedName("device_tao_vid")
    String device_tao_vid;
    @SerializedName("ip_tao_vid")
    String ip_tao_vid;
    @SerializedName("id_user")
    int id_user;

    public String getLink_video_da_swap() {
        return link_video_da_swap;
    }

    public String getId_saved() {
        return id_saved;
    }

    public String getLink_video_goc() {
        return link_video_goc;
    }

    public String getLink_image() {
        return link_image;
    }

    public String getLink_vid_da_swap() {
        return link_vid_da_swap;
    }

    public String getThoigian_sukien() {
        return thoigian_sukien;
    }

    public String getDevice_tao_vid() {
        return device_tao_vid;
    }

    public String getIp_tao_vid() {
        return ip_tao_vid;
    }

    public int getId_user() {
        return id_user;
    }
}
