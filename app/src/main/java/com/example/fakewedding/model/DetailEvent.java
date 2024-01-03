package com.example.fakewedding.model;

import com.google.gson.annotations.SerializedName;

public class DetailEvent {

    @SerializedName("count_comment")
    private int countComment;
    @SerializedName("count_view")
    private int countView;
    @SerializedName("id")
    private long id;
    @SerializedName("id_template")
    private int idTemplate;
    @SerializedName("id_toan_bo_su_kien")
    private long idToanBoSuKien;
    @SerializedName("id_user")
    private int idUser;
    @SerializedName("link_da_swap")
    private String linkDaSwap;
    @SerializedName("link_nam_chua_swap")
    private String linkNamChuaSwap;
    @SerializedName("link_nam_goc")
    private String linkNamGoc;
    @SerializedName("link_nu_chua_swap")
    private String linkNuChuaSwap;
    @SerializedName("link_nu_goc")
    private String linkNuGoc;
    @SerializedName("noi_dung_su_kien")
    private String noiDungSuKien;
    @SerializedName("phantram_loading")
    private int phantramLoading;
    @SerializedName("real_time")
    private String realTime;
    @SerializedName("so_thu_tu_su_kien")
    private double soThuTuSuKien;
    @SerializedName("ten_nam")
    private String tenNam;
    @SerializedName("ten_nu")
    private String tenNu;
    @SerializedName("ten_su_kien")
    private String tenSuKien;

    public DetailEvent(int countComment, int countView, long id, int idTemplate, int idToanBoSuKien, int idUser, String linkDaSwap, String linkNamChuaSwap, String linkNamGoc, String linkNuChuaSwap, String linkNuGoc, String noiDungSuKien, int phantramLoading, String realTime, double soThuTuSuKien, String tenNam, String tenNu, String tenSuKien) {
        this.countComment = countComment;
        this.countView = countView;
        this.id = id;
        this.idTemplate = idTemplate;
        this.idToanBoSuKien = idToanBoSuKien;
        this.idUser = idUser;
        this.linkDaSwap = linkDaSwap;
        this.linkNamChuaSwap = linkNamChuaSwap;
        this.linkNamGoc = linkNamGoc;
        this.linkNuChuaSwap = linkNuChuaSwap;
        this.linkNuGoc = linkNuGoc;
        this.noiDungSuKien = noiDungSuKien;
        this.phantramLoading = phantramLoading;
        this.realTime = realTime;
        this.soThuTuSuKien = soThuTuSuKien;
        this.tenNam = tenNam;
        this.tenNu = tenNu;
        this.tenSuKien = tenSuKien;
    }

    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdTemplate() {
        return idTemplate;
    }

    public void setIdTemplate(int idTemplate) {
        this.idTemplate = idTemplate;
    }

    public long getIdToanBoSuKien() {
        return idToanBoSuKien ;
    }

    public void setIdToanBoSuKien(int idToanBoSuKien) {
        this.idToanBoSuKien = idToanBoSuKien;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLinkDaSwap() {
        return linkDaSwap;
    }

    public void setLinkDaSwap(String linkDaSwap) {
        this.linkDaSwap = linkDaSwap;
    }

    public String getLinkNamChuaSwap() {
        return linkNamChuaSwap;
    }

    public void setLinkNamChuaSwap(String linkNamChuaSwap) {
        this.linkNamChuaSwap = linkNamChuaSwap;
    }

    public String getLinkNamGoc() {
        return linkNamGoc;
    }

    public void setLinkNamGoc(String linkNamGoc) {
        this.linkNamGoc = linkNamGoc;
    }

    public String getLinkNuChuaSwap() {
        return linkNuChuaSwap;
    }

    public void setLinkNuChuaSwap(String linkNuChuaSwap) {
        this.linkNuChuaSwap = linkNuChuaSwap;
    }

    public String getLinkNuGoc() {
        return linkNuGoc;
    }

    public void setLinkNuGoc(String linkNuGoc) {
        this.linkNuGoc = linkNuGoc;
    }

    public String getNoiDungSuKien() {
        return noiDungSuKien;
    }

    public void setNoiDungSuKien(String noiDungSuKien) {
        this.noiDungSuKien = noiDungSuKien;
    }

    public int getPhantramLoading() {
        return phantramLoading;
    }

    public void setPhantramLoading(int phantramLoading) {
        this.phantramLoading = phantramLoading;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public double getSoThuTuSuKien() {
        return soThuTuSuKien;
    }

    public void setSoThuTuSuKien(double soThuTuSuKien) {
        this.soThuTuSuKien = soThuTuSuKien;
    }

    public String getTenNam() {
        return tenNam;
    }

    public void setTenNam(String tenNam) {
        this.tenNam = tenNam;
    }

    public String getTenNu() {
        return tenNu;
    }

    public void setTenNu(String tenNu) {
        this.tenNu = tenNu;
    }

    public String getTenSuKien() {
        return tenSuKien;
    }

    public void setTenSuKien(String tenSuKien) {
        this.tenSuKien = tenSuKien;
    }
}
