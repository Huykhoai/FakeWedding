package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class SwapEventInfo {
    @SerializedName("id_saved")
    private String idSaved;

    @SerializedName("link_src_goc")
    private String linkSrcGoc;

    @SerializedName("link_tar_goc")
    private String linkTarGoc;

    @SerializedName("link_da_swap")
    private String linkDaSwap;

    @SerializedName("id_toan_bo_su_kien")
    private String idToanBoSuKien;

    @SerializedName("thoigian_sukien")
    private String thoigianSukien;

    @SerializedName("device_them_su_kien")
    private String deviceThemSuKien;

    @SerializedName("ip_them_su_kien")
    private String ipThemSuKien;

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("count_comment")
    private int countComment;

    @SerializedName("count_view")
    private int countView;

    @SerializedName("id_template")
    private int idTemplate;

    @SerializedName("loai_sukien")
    private String loaiSukien;

    public String getIdSaved() {
        return idSaved;
    }

    public String getLinkSrcGoc() {
        return linkSrcGoc;
    }

    public String getLinkTarGoc() {
        return linkTarGoc;
    }

    public String getLinkDaSwap() {
        return linkDaSwap;
    }

    public String getIdToanBoSuKien() {
        return idToanBoSuKien;
    }

    public String getThoigianSukien() {
        return thoigianSukien;
    }

    public String getDeviceThemSuKien() {
        return deviceThemSuKien;
    }

    public String getIpThemSuKien() {
        return ipThemSuKien;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getCountComment() {
        return countComment;
    }

    public int getCountView() {
        return countView;
    }

    public int getIdTemplate() {
        return idTemplate;
    }

    public String getLoaiSukien() {
        return loaiSukien;
    }
}
