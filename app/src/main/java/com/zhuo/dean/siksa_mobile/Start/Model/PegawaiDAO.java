package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PegawaiDAO {
    @SerializedName("KODE_PEGAWAI")
    @Expose
    private Integer kODEPEGAWAI;
    @SerializedName("KODE_ROLE")
    @Expose
    private Integer kODEROLE;
    @SerializedName("NAMA_PEGAWAI")
    @Expose
    private String nAMAPEGAWAI;
    @SerializedName("USERNAME")
    @Expose
    private String uSERNAME;
    @SerializedName("PASSWORD")
    @Expose
    private String pASSWORD;
    @SerializedName("ALAMAT_PEGAWAI")
    @Expose
    private String aLAMATPEGAWAI;
    @SerializedName("NO_TELP_PEGAWAI")
    @Expose
    private Object nOTELPPEGAWAI;
    @SerializedName("GAJI_PER_MINGGU")
    @Expose
    private Integer gAJIPERMINGGU;

    public Integer getKODEPEGAWAI() {
        return kODEPEGAWAI;
    }

    public void setKODEPEGAWAI(Integer kODEPEGAWAI) {
        this.kODEPEGAWAI = kODEPEGAWAI;
    }

    public Integer getKODEROLE() {
        return kODEROLE;
    }

    public void setKODEROLE(Integer kODEROLE) {
        this.kODEROLE = kODEROLE;
    }

    public String getNAMAPEGAWAI() {
        return nAMAPEGAWAI;
    }

    public void setNAMAPEGAWAI(String nAMAPEGAWAI) {
        this.nAMAPEGAWAI = nAMAPEGAWAI;
    }

    public String getUSERNAME() {
        return uSERNAME;
    }

    public void setUSERNAME(String uSERNAME) {
        this.uSERNAME = uSERNAME;
    }

    public String getPASSWORD() {
        return pASSWORD;
    }

    public void setPASSWORD(String pASSWORD) {
        this.pASSWORD = pASSWORD;
    }

    public String getALAMATPEGAWAI() {
        return aLAMATPEGAWAI;
    }

    public void setALAMATPEGAWAI(String aLAMATPEGAWAI) {
        this.aLAMATPEGAWAI = aLAMATPEGAWAI;
    }

    public Object getNOTELPPEGAWAI() {
        return nOTELPPEGAWAI;
    }

    public void setNOTELPPEGAWAI(Object nOTELPPEGAWAI) {
        this.nOTELPPEGAWAI = nOTELPPEGAWAI;
    }

    public Integer getGAJIPERMINGGU() {
        return gAJIPERMINGGU;
    }

    public void setGAJIPERMINGGU(Integer gAJIPERMINGGU) {
        this.gAJIPERMINGGU = gAJIPERMINGGU;
    }

    public PegawaiDAO(Integer kODEPEGAWAI, Integer kODEROLE, String nAMAPEGAWAI, String uSERNAME, String pASSWORD, String aLAMATPEGAWAI, Object nOTELPPEGAWAI, Integer gAJIPERMINGGU) {
        this.kODEPEGAWAI = kODEPEGAWAI;
        this.kODEROLE = kODEROLE;
        this.nAMAPEGAWAI = nAMAPEGAWAI;
        this.uSERNAME = uSERNAME;
        this.pASSWORD = pASSWORD;
        this.aLAMATPEGAWAI = aLAMATPEGAWAI;
        this.nOTELPPEGAWAI = nOTELPPEGAWAI;
        this.gAJIPERMINGGU = gAJIPERMINGGU;
    }

    @Override
    public String toString() {
        return nAMAPEGAWAI;
    }
}
