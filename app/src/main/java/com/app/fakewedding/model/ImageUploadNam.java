package com.app.fakewedding.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageUploadNam {
     @SerializedName("image_links_nam")
    List<String> links_nam ;
     @SerializedName("image_links_nu")
     List<String> links_nu;

    public ImageUploadNam(List<String> links_nam, List<String> links_nu) {
        this.links_nam = links_nam;
        this.links_nu = links_nu;
    }

    public List<String> getLinks_nu() {
        return links_nu;
    }

    public void setLinks_nu(List<String> links_nu) {
        this.links_nu = links_nu;
    }

    public List<String> getLinks_nam() {
        return links_nam;
    }

    public void setLinks_nam(List<String> links_nam) {
        this.links_nam = links_nam;
    }
}
