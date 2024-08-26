package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
