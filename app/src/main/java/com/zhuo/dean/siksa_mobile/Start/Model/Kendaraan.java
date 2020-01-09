package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kendaraan {
    @SerializedName("KODE_KENDARAAN")
    @Expose
    private Integer kODEKENDARAAN;
    @SerializedName("PLAT_KENDARAAN")
    @Expose
    private String pLATKENDARAAN;
    @SerializedName("MERK_KENDARAAN")
    @Expose
    private String mERKKENDARAAN;
    @SerializedName("TIPE_KENDARAAN")
    @Expose
    private String tIPEKENDARAAN;

    public Integer getKODEKENDARAAN() {
        return kODEKENDARAAN;
    }

    public void setKODEKENDARAAN(Integer kODEKENDARAAN) {
        this.kODEKENDARAAN = kODEKENDARAAN;
    }

    public String getPLATKENDARAAN() {
        return pLATKENDARAAN;
    }

    public void setPLATKENDARAAN(String pLATKENDARAAN) {
        this.pLATKENDARAAN = pLATKENDARAAN;
    }

    public String getMERKKENDARAAN() {
        return mERKKENDARAAN;
    }

    public void setMERKKENDARAAN(String mERKKENDARAAN) {
        this.mERKKENDARAAN = mERKKENDARAAN;
    }

    public String getTIPEKENDARAAN() {
        return tIPEKENDARAAN;
    }

    public void setTIPEKENDARAAN(String tIPEKENDARAAN) {
        this.tIPEKENDARAAN = tIPEKENDARAAN;
    }

    public Kendaraan(Integer kODEKENDARAAN, String pLATKENDARAAN, String mERKKENDARAAN, String tIPEKENDARAAN) {
        this.kODEKENDARAAN = kODEKENDARAAN;
        this.pLATKENDARAAN = pLATKENDARAAN;
        this.mERKKENDARAAN = mERKKENDARAAN;
        this.tIPEKENDARAAN = tIPEKENDARAAN;
    }

    @Override
    public String toString() {
        return pLATKENDARAAN;
    }
}
