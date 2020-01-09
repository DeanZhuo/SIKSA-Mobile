package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryMasuk {
    @SerializedName("KODE_PENGADAAN")
    @Expose
    private Integer kODEPENGADAAN;
    @SerializedName("KODE_SPAREPART")
    @Expose
    private Integer kODESPAREPART;
    @SerializedName("TANGGAL_MASUK")
    @Expose
    private String tANGGALMASUK;
    @SerializedName("STOK_BARANG_MASUK")
    @Expose
    private Integer sTOKBARANGMASUK;
    @SerializedName("HARGA_BARANG_MASUK")
    @Expose
    private Integer hARGABARANGMASUK;

    public Integer getKODEPENGADAAN() {
        return kODEPENGADAAN;
    }

    public void setKODEPENGADAAN(Integer kODEPENGADAAN) {
        this.kODEPENGADAAN = kODEPENGADAAN;
    }

    public Integer getKODESPAREPART() {
        return kODESPAREPART;
    }

    public void setKODESPAREPART(Integer kODESPAREPART) {
        this.kODESPAREPART = kODESPAREPART;
    }

    public String getTANGGALMASUK() {
        return tANGGALMASUK;
    }

    public void setTANGGALMASUK(String tANGGALMASUK) {
        this.tANGGALMASUK = tANGGALMASUK;
    }

    public Integer getSTOKBARANGMASUK() {
        return sTOKBARANGMASUK;
    }

    public void setSTOKBARANGMASUK(Integer sTOKBARANGMASUK) {
        this.sTOKBARANGMASUK = sTOKBARANGMASUK;
    }

    public Integer getHARGABARANGMASUK() {
        return hARGABARANGMASUK;
    }

    public void setHARGABARANGMASUK(Integer hARGABARANGMASUK) {
        this.hARGABARANGMASUK = hARGABARANGMASUK;
    }
}
