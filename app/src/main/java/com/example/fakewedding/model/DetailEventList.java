package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailEventList {
    @SerializedName("sukien")
    private List<DetailEvent> sukien;

    public List<DetailEvent> getSukien() {
        return sukien;
    }

    public void setSukien(List<DetailEvent> sukien) {
        this.sukien = sukien;
    }

    public DetailEventList(List<DetailEvent> sukien) {
        this.sukien = sukien;
    }
}
