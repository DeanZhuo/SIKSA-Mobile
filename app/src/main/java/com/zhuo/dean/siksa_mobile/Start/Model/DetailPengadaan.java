package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailPengadaan {
    @SerializedName("KODE_DETAIL_PENGADAAN")
    @Expose
    private Integer kODEDETAILPENGADAAN;
    @SerializedName("KODE_PENGADAAN")
    @Expose
    private Integer kODEPENGADAAN;
    @SerializedName("NAMA_BARANG")
    @Expose
    private String nAMABARANG;
    @SerializedName("JUMLAH_PESANAN")
    @Expose
    private Integer jUMLAHPESANAN;
    @SerializedName("HARGA_SATUAN")
    @Expose
    private Integer hARGASATUAN;

    public DetailPengadaan(String nAMABARANG, Integer jUMLAHPESANAN, Integer hARGASATUAN) {
        this.nAMABARANG = nAMABARANG;
        this.jUMLAHPESANAN = jUMLAHPESANAN;
        this.hARGASATUAN = hARGASATUAN;
    }

    public Integer getKODEDETAILPENGADAAN() {
        return kODEDETAILPENGADAAN;
    }

    public void setKODEDETAILPENGADAAN(Integer kODEDETAILPENGADAAN) {
        this.kODEDETAILPENGADAAN = kODEDETAILPENGADAAN;
    }

    public Integer getKODEPENGADAAN() {
        return kODEPENGADAAN;
    }

    public void setKODEPENGADAAN(Integer kODEPENGADAAN) {
        this.kODEPENGADAAN = kODEPENGADAAN;
    }

    public String getNAMABARANG() {
        return nAMABARANG;
    }

    public void setNAMABARANG(String nAMABARANG) {
        this.nAMABARANG = nAMABARANG;
    }

    public Integer getJUMLAHPESANAN() {
        return jUMLAHPESANAN;
    }

    public void setJUMLAHPESANAN(Integer jUMLAHPESANAN) {
        this.jUMLAHPESANAN = jUMLAHPESANAN;
    }

    public Integer getHARGASATUAN() {
        return hARGASATUAN;
    }

    public void setHARGASATUAN(Integer hARGASATUAN) {
        this.hARGASATUAN = hARGASATUAN;
    }
}
