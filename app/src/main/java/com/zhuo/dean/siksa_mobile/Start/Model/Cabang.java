package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cabang {
    @SerializedName("KODE_CABANG")
    @Expose
    private Integer kODECABANG;
    @SerializedName("NAMA_CABANG")
    @Expose
    private String nAMACABANG;
    @SerializedName("ALAMAT_CABANG")
    @Expose
    private String aLAMATCABANG;
    @SerializedName("NO_TELP_CABANG")
    @Expose
    private String nOTELPCABANG;

    public Integer getKODECABANG() {
        return kODECABANG;
    }

    public void setKODECABANG(Integer kODECABANG) {
        this.kODECABANG = kODECABANG;
    }

    public String getNAMACABANG() {
        return nAMACABANG;
    }

    public void setNAMACABANG(String nAMACABANG) {
        this.nAMACABANG = nAMACABANG;
    }

    public String getALAMATCABANG() {
        return aLAMATCABANG;
    }

    public void setALAMATCABANG(String aLAMATCABANG) {
        this.aLAMATCABANG = aLAMATCABANG;
    }

    public String getNOTELPCABANG() {
        return nOTELPCABANG;
    }

    public void setNOTELPCABANG(String nOTELPCABANG) {
        this.nOTELPCABANG = nOTELPCABANG;
    }

    public Cabang(Integer kODECABANG, String nAMACABANG, String aLAMATCABANG, String nOTELPCABANG) {
        this.kODECABANG = kODECABANG;
        this.nAMACABANG = nAMACABANG;
        this.aLAMATCABANG = aLAMATCABANG;
        this.nOTELPCABANG = nOTELPCABANG;
    }

    @Override
    public String toString() {
        return nAMACABANG;
    }
}
