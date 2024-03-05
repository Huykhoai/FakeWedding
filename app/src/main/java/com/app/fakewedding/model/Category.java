package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id_cate")
    private int id_cate;
    @SerializedName("name_cate")
    private String name_cate;
    @SerializedName("number_image")
    private int number_image;
    @SerializedName("folder_name")
    private String folder_name;
    @SerializedName("selected_swap")
    private int selected_swap;
    @SerializedName("image_sample")
    private String image_sample;

    public Category(int id_cate, String name_cate, int number_image, String folder_name, int selected_swap, String image_sample) {
        this.id_cate = id_cate;
        this.name_cate = name_cate;
        this.number_image = number_image;
        this.folder_name = folder_name;
        this.selected_swap = selected_swap;
        this.image_sample = image_sample;
    }

    public Category() {
    }

    public int getId_cate() {
        return id_cate;
    }

    public void setId_cate(int id_cate) {
        this.id_cate = id_cate;
    }

    public String getName_cate() {
        return name_cate;
    }

    public void setName_cate(String name_cate) {
        this.name_cate = name_cate;
    }

    public int getNumber_image() {
        return number_image;
    }

    public void setNumber_image(int number_image) {
        this.number_image = number_image;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public int getSelected_swap() {
        return selected_swap;
    }

    public void setSelected_swap(int selected_swap) {
        this.selected_swap = selected_swap;
    }

    public String getImage_sample() {
        return image_sample;
    }

    public void setImage_sample(String image_sample) {
        this.image_sample = image_sample;
    }
}
