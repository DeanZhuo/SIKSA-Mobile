package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusService {
    @SerializedName("KODE_STATUS")
    @Expose
    private Integer kODESTATUS;
    @SerializedName("NAMA_STATUS")
    @Expose
    private String nAMASTATUS;

    public Integer getKODESTATUS() {
        return kODESTATUS;
    }

    public void setKODESTATUS(Integer kODESTATUS) {
        this.kODESTATUS = kODESTATUS;
    }

    public String getNAMASTATUS() {
        return nAMASTATUS;
    }

    public void setNAMASTATUS(String nAMASTATUS) {
        this.nAMASTATUS = nAMASTATUS;
    }
}
