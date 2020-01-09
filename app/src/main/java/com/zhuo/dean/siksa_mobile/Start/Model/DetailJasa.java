package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailJasa {
    @SerializedName("KODE_DETAIL_TRANSAKSI")
    @Expose
    private Integer kODEDETAILTRANSAKSI;
    @SerializedName("KODE_SERVICE")
    @Expose
    private Integer kODESERVICE;

    public Integer getKODEDETAILTRANSAKSI() {
        return kODEDETAILTRANSAKSI;
    }

    public void setKODEDETAILTRANSAKSI(Integer kODEDETAILTRANSAKSI) {
        this.kODEDETAILTRANSAKSI = kODEDETAILTRANSAKSI;
    }

    public Integer getKODESERVICE() {
        return kODESERVICE;
    }

    public void setKODESERVICE(Integer kODESERVICE) {
        this.kODESERVICE = kODESERVICE;
    }
}
