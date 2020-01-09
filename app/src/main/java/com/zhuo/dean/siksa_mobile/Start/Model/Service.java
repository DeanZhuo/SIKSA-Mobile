package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("KODE_JASA")
    @Expose
    private Integer kODEJASA;
    @SerializedName("NAMA_SERVICE")
    @Expose
    private String nAMASERVICE;
    @SerializedName("HARGA_SERVICE")
    @Expose
    private Integer hARGASERVICE;

    public Service(Integer kODEJASA, String nAMASERVICE, Integer hARGASERVICE) {
        this.kODEJASA = kODEJASA;
        this.nAMASERVICE = nAMASERVICE;
        this.hARGASERVICE = hARGASERVICE;
    }

    public Integer getKODEJASA() {
        return kODEJASA;
    }

    public void setKODEJASA(Integer kODEJASA) {
        this.kODEJASA = kODEJASA;
    }

    public String getNAMASERVICE() {
        return nAMASERVICE;
    }

    public void setNAMASERVICE(String nAMASERVICE) {
        this.nAMASERVICE = nAMASERVICE;
    }

    public Integer getHARGASERVICE() {
        return hARGASERVICE;
    }

    public void setHARGASERVICE(Integer hARGASERVICE) {
        this.hARGASERVICE = hARGASERVICE;
    }
}
