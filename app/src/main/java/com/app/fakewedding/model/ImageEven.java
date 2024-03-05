package com.app.fakewedding.model;

import androidx.lifecycle.ViewModel;

import com.app.fakewedding.R;

public class ImageEven extends ViewModel {
    public  String image;
    public  int stt;

    public ImageEven(String image, int stt) {
        this.image = image;
        this.stt = stt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }
}
