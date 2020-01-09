package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pengadaan {
    @SerializedName("KODE_PENGADAAN")
    @Expose
    private Integer kODEPENGADAAN;
    @SerializedName("KODE_SUPPLIER")
    @Expose
    private Integer kODESUPPLIER;
    @SerializedName("KODE_PEGAWAI")
    @Expose
    private Integer kODEPEGAWAI;
    @SerializedName("TANGGAL_PENGADAAN")
    @Expose
    private String tANGGALPENGADAAN;
    @SerializedName("PEMBAYARAN_PEMESANAN")
    @Expose
    private Integer pEMBAYARANPEMESANAN;

    public Integer getKODEPENGADAAN() {
        return kODEPENGADAAN;
    }

    public void setKODEPENGADAAN(Integer kODEPENGADAAN) {
        this.kODEPENGADAAN = kODEPENGADAAN;
    }

    public Integer getKODESUPPLIER() {
        return kODESUPPLIER;
    }

    public void setKODESUPPLIER(Integer kODESUPPLIER) {
        this.kODESUPPLIER = kODESUPPLIER;
    }

    public Integer getKODEPEGAWAI() {
        return kODEPEGAWAI;
    }

    public void setKODEPEGAWAI(Integer kODEPEGAWAI) {
        this.kODEPEGAWAI = kODEPEGAWAI;
    }

    public String getTANGGALPENGADAAN() {
        return tANGGALPENGADAAN;
    }

    public void setTANGGALPENGADAAN(String tANGGALPENGADAAN) {
        this.tANGGALPENGADAAN = tANGGALPENGADAAN;
    }

    public Integer getPEMBAYARANPEMESANAN() {
        return pEMBAYARANPEMESANAN;
    }

    public void setPEMBAYARANPEMESANAN(Integer pEMBAYARANPEMESANAN) {
        this.pEMBAYARANPEMESANAN = pEMBAYARANPEMESANAN;
    }
}
