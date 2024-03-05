package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SwapEventData {
    @SerializedName("sukien_2_image")
    private SwapEventInfo swapEventInfo;

    @SerializedName("link_anh_swap")
    private List<String> swapImageLinks;

    public SwapEventInfo getSwapEventInfo() {
        return swapEventInfo;
    }

    public List<String> getSwapImageLinks() {
        return swapImageLinks;
    }
}

