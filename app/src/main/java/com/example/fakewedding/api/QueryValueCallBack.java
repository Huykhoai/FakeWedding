package com.example.fakewedding.api;

public interface QueryValueCallBack {
    void onQueryValueReceived(String query);
    void onApiCallFailed(Throwable tb);
}
