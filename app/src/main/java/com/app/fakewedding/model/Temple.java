package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Temple implements Serializable {
    @SerializedName("id")
    int id;
    @SerializedName("mask")
    String mask;
    @SerializedName("thongtin")
    String thongtin;
    @SerializedName("image")
    String image;
    @SerializedName("dotuoi")
    int dotuoi;
    @SerializedName("IDCategories")
    int id_category;

    public Temple(int id, String mask, String thongtin, String image, int dotuoi, int id_category) {
        this.id = id;
        this.mask = mask;
        this.thongtin = thongtin;
        this.image = image;
        this.dotuoi = dotuoi;
        this.id_category = id_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getThongtin() {
        return thongtin;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDotuoi() {
        return dotuoi;
    }

    public void setDotuoi(int dotuoi) {
        this.dotuoi = dotuoi;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }
}
