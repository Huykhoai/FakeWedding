package com.app.fakewedding.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

// Glossary.java
public class Glossary {
    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @NonNull
    @Override
    public String toString() {
        return link;
    }
}



