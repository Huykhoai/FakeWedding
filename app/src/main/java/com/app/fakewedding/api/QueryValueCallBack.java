package com.app.fakewedding.api;

public interface QueryValueCallBack {
    void onQueryValueReceived(String query);
    void onApiCallFailed(Throwable tb);
}
