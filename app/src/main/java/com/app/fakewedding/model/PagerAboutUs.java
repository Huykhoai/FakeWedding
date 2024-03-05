package com.app.fakewedding.model;

public class PagerAboutUs {
    private int image;
    private String title;
    private String messege;
    private String url;

    public PagerAboutUs(int image, String title, String messege,String url) {
        this.image = image;
        this.title = title;
        this.messege = messege;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }
}
