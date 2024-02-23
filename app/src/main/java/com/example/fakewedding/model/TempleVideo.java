package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class TempleVideo {
    @SerializedName("id")
    int id;
    @SerializedName("link_video")
    String link_video;
    @SerializedName("noidung")
    String noidung;
    @SerializedName("age_video")
    String age_video;
    @SerializedName("gioitinh")
    String gioitinh;
    @SerializedName("mau_da")
    String mau_da;
    @SerializedName("chung_toc")
    String chung_toc;

    public int getId() {
        return id;
    }

    public String getLink_video() {
        return link_video;
    }

    public String getNoidung() {
        return noidung;
    }

    public String getAge_video() {
        return age_video;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public String getMau_da() {
        return mau_da;
    }

    public String getChung_toc() {
        return chung_toc;
    }
}
