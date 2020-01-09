package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sparepart {

    @SerializedName("KODE_SPAREPART")
    @Expose
    private Integer kODESPAREPART;
    @SerializedName("KODE_BARANG")
    @Expose
    private String kODEBARANG;
    @SerializedName("TIPE_SPAREPART")
    @Expose
    private String tIPESPAREPART;
    @SerializedName("NAMA_SPAREPART")
    @Expose
    private String nAMASPAREPART;
    @SerializedName("HARGA_JUAL")
    @Expose
    private Integer hARGAJUAL;
    @SerializedName("HARGA_BELI")
    @Expose
    private Integer hARGABELI;
    @SerializedName("MERK_SPAREPART")
    @Expose
    private String mERKSPAREPART;
    @SerializedName("KODE_PELETAKAN")
    @Expose
    private String kODEPELETAKAN;
    @SerializedName("JUMLAH_STOK")
    @Expose
    private Integer jUMLAHSTOK;
    @SerializedName("GAMBAR")
    @Expose
    private String gAMBAR;

    public Sparepart(Integer kODESPAREPART, String kODEBARANG, String tIPESPAREPART, String nAMASPAREPART, Integer hARGAJUAL, Integer hARGABELI, String mERKSPAREPART, String kODEPELETAKAN, Integer jUMLAHSTOK, String gAMBAR) {
        this.kODESPAREPART = kODESPAREPART;
        this.kODEBARANG = kODEBARANG;
        this.tIPESPAREPART = tIPESPAREPART;
        this.nAMASPAREPART = nAMASPAREPART;
        this.hARGAJUAL = hARGAJUAL;
        this.hARGABELI = hARGABELI;
        this.mERKSPAREPART = mERKSPAREPART;
        this.kODEPELETAKAN = kODEPELETAKAN;
        this.jUMLAHSTOK = jUMLAHSTOK;
        this.gAMBAR = gAMBAR;
    }

    public Integer getkODESPAREPART() {
        return kODESPAREPART;
    }

    public void setkODESPAREPART(Integer kODESPAREPART) {
        this.kODESPAREPART = kODESPAREPART;
    }

    public String getkODEBARANG() {
        return kODEBARANG;
    }

    public void setkODEBARANG(String kODEBARANG) {
        this.kODEBARANG = kODEBARANG;
    }

    public String gettIPESPAREPART() {
        return tIPESPAREPART;
    }

    public void settIPESPAREPART(String tIPESPAREPART) {
        this.tIPESPAREPART = tIPESPAREPART;
    }

    public String getnAMASPAREPART() {
        return nAMASPAREPART;
    }

    public void setnAMASPAREPART(String nAMASPAREPART) {
        this.nAMASPAREPART = nAMASPAREPART;
    }

    public Integer gethARGAJUAL() {
        return hARGAJUAL;
    }

    public void sethARGAJUAL(Integer hARGAJUAL) {
        this.hARGAJUAL = hARGAJUAL;
    }

    public Integer gethARGABELI() {
        return hARGABELI;
    }

    public void sethARGABELI(Integer hARGABELI) {
        this.hARGABELI = hARGABELI;
    }

    public String getmERKSPAREPART() {
        return mERKSPAREPART;
    }

    public void setmERKSPAREPART(String mERKSPAREPART) {
        this.mERKSPAREPART = mERKSPAREPART;
    }

    public String getkODEPELETAKAN() {
        return kODEPELETAKAN;
    }

    public void setkODEPELETAKAN(String kODEPELETAKAN) {
        this.kODEPELETAKAN = kODEPELETAKAN;
    }

    public Integer getjUMLAHSTOK() {
        return jUMLAHSTOK;
    }

    public void setjUMLAHSTOK(Integer jUMLAHSTOK) {
        this.jUMLAHSTOK = jUMLAHSTOK;
    }

    public String getgAMBAR() {
        return gAMBAR;
    }

    public void setgAMBAR(String gAMBAR) {
        this.gAMBAR = gAMBAR;
    }

    @Override
    public String toString() {
        return "Sparepart{" +
                "Kode_sparepart=" + kODESPAREPART +
                ", Kode_barang='" + kODEBARANG + '\'' +
                ", Tipe_sparepart='" + tIPESPAREPART + '\'' +
                ", Nama_sparepart='" + nAMASPAREPART + '\'' +
                ", Harga_jual=" + hARGAJUAL +
                ", Harga_beli=" + hARGABELI +
                ", Merk_sparepart='" + mERKSPAREPART + '\'' +
                ", Kode_peletakan='" + kODEBARANG + '\'' +
                ", Jumlah_Stok=" + jUMLAHSTOK +
                ", Gambar=" + gAMBAR +
                '}';
    }
}
