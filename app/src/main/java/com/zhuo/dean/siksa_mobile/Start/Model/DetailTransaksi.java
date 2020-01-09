package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailTransaksi implements Serializable {
    @SerializedName("KODE_DETAIL_TRANSAKSI")
    @Expose
    private Integer kODEDETAILTRANSAKSI;
    @SerializedName("KODE_TRANSAKSI")
    @Expose
    private Integer kODETRANSAKSI;
    @SerializedName("TOTAL_BAYAR")
    @Expose
    private Integer tOTALBAYAR;

    public Integer getKODEDETAILTRANSAKSI() {
        return kODEDETAILTRANSAKSI;
    }

    public void setKODEDETAILTRANSAKSI(Integer kODEDETAILTRANSAKSI) {
        this.kODEDETAILTRANSAKSI = kODEDETAILTRANSAKSI;
    }

    public Integer getKODETRANSAKSI() {
        return kODETRANSAKSI;
    }

    public void setKODETRANSAKSI(Integer kODETRANSAKSI) {
        this.kODETRANSAKSI = kODETRANSAKSI;
    }

    public Integer getTOTALBAYAR() {
        return tOTALBAYAR;
    }

    public void setTOTALBAYAR(Integer tOTALBAYAR) {
        this.tOTALBAYAR = tOTALBAYAR;
    }
}
