package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailEventListParent {
    @SerializedName("list_sukien")
    private List<DetailEventList> listSukien;

    public DetailEventListParent(List<DetailEventList> listSukien) {
        this.listSukien = listSukien;
    }

    public List<DetailEventList> getListSukien() {
        return listSukien;
    }

    public void setListSukien(List<DetailEventList> listSukien) {
        this.listSukien = listSukien;
    }
}
