package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListCategory {
    @SerializedName("categories_all")
    private ArrayList<Category> categories;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
