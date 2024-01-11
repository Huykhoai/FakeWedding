package com.example.fakewedding.model;

import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel {
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}