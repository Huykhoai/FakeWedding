package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
    public class Album {
        @SerializedName("album")
        private int album;
        @SerializedName("list_sukien_image")
        private List<AlbumSwapped> list_sukien_image;

        public int getAlbum() {
            return album;
        }

        public List<AlbumSwapped> getList_sukien_image() {
            return list_sukien_image;
        }
    }
