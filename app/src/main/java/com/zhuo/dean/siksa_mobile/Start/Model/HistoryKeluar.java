package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryKeluar {
    @SerializedName("KODE_DETAIL_TRANSAKSI")
    @Expose
    private Integer kODEDETAILTRANSAKSI;
    @SerializedName("KODE_SPAREPART")
    @Expose
    private Integer kODESPAREPART;
    @SerializedName("TANGGAL_KELUAR")
    @Expose
    private String tANGGALKELUAR;
    @SerializedName("STOK_BARANG_KELUAR")
    @Expose
    private Integer sTOKBARANGKELUAR;
    @SerializedName("HARGA_BARANG_KELUAR")
    @Expose
    private Integer hARGABARANGKELUAR;

    public Integer getKODEDETAILTRANSAKSI() {
        return kODEDETAILTRANSAKSI;
    }

    public void setKODEDETAILTRANSAKSI(Integer kODEDETAILTRANSAKSI) {
        this.kODEDETAILTRANSAKSI = kODEDETAILTRANSAKSI;
    }

    public Integer getKODESPAREPART() {
        return kODESPAREPART;
    }

    public void setKODESPAREPART(Integer kODESPAREPART) {
        this.kODESPAREPART = kODESPAREPART;
    }

    public String getTANGGALKELUAR() {
        return tANGGALKELUAR;
    }

    public void setTANGGALKELUAR(String tANGGALKELUAR) {
        this.tANGGALKELUAR = tANGGALKELUAR;
    }

    public Integer getSTOKBARANGKELUAR() {
        return sTOKBARANGKELUAR;
    }

    public void setSTOKBARANGKELUAR(Integer sTOKBARANGKELUAR) {
        this.sTOKBARANGKELUAR = sTOKBARANGKELUAR;
    }

    public Integer getHARGABARANGKELUAR() {
        return hARGABARANGKELUAR;
    }

    public void setHARGABARANGKELUAR(Integer hARGABARANGKELUAR) {
        this.hARGABARANGKELUAR = hARGABARANGKELUAR;
    }

    public HistoryKeluar(Integer kODESPAREPART, Integer sTOKBARANGKELUAR, Integer hARGABARANGKELUAR) {
        this.kODESPAREPART = kODESPAREPART;
        this.sTOKBARANGKELUAR = sTOKBARANGKELUAR;
        this.hARGABARANGKELUAR = hARGABARANGKELUAR;
    }
}
