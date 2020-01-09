package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supplier {
    @SerializedName("KODE_SUPPLIER")
    @Expose
    private Integer kODESUPPLIER;
    @SerializedName("NAMA_SUPPLIER")
    @Expose
    private String nAMASUPPLIER;
    @SerializedName("ALAMAT_SUPPLIER")
    @Expose
    private String aLAMATSUPPLIER;
    @SerializedName("NAMA_SALES")
    @Expose
    private String nAMASALES;
    @SerializedName("NO_TELP_SALES")
    @Expose
    private String nOTELPSALES;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;

    public Integer getKODESUPPLIER() {
        return kODESUPPLIER;
    }

    public void setKODESUPPLIER(Integer kODESUPPLIER) {
        this.kODESUPPLIER = kODESUPPLIER;
    }

    public String getNAMASUPPLIER() {
        return nAMASUPPLIER;
    }

    public void setNAMASUPPLIER(String nAMASUPPLIER) {
        this.nAMASUPPLIER = nAMASUPPLIER;
    }

    public String getALAMATSUPPLIER() {
        return aLAMATSUPPLIER;
    }

    public void setALAMATSUPPLIER(String aLAMATSUPPLIER) {
        this.aLAMATSUPPLIER = aLAMATSUPPLIER;
    }

    public String getNAMASALES() {
        return nAMASALES;
    }

    public void setNAMASALES(String nAMASALES) {
        this.nAMASALES = nAMASALES;
    }

    public String getNOTELPSALES() {
        return nOTELPSALES;
    }

    public void setNOTELPSALES(String nOTELPSALES) {
        this.nOTELPSALES = nOTELPSALES;
    }

    public Supplier(Integer kODESUPPLIER, String nAMASUPPLIER, String aLAMATSUPPLIER, String nAMASALES, String nOTELPSALES) {
        this.kODESUPPLIER = kODESUPPLIER;
        this.nAMASUPPLIER = nAMASUPPLIER;
        this.aLAMATSUPPLIER = aLAMATSUPPLIER;
        this.nAMASALES = nAMASALES;
        this.nOTELPSALES = nOTELPSALES;
    }

    @Override
    public String toString() {
        return nAMASUPPLIER;
    }
}
