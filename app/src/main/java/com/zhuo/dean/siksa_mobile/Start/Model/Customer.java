package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {
    public Customer(Integer kODECUST, String nAMACUST, String nOTELPCUST) {
        this.kODECUST = kODECUST;
        this.nAMACUST = nAMACUST;
        this.nOTELPCUST = nOTELPCUST;
    }

    public Integer getkODECUST() {
        return kODECUST;
    }

    public void setkODECUST(Integer kODECUST) {
        this.kODECUST = kODECUST;
    }

    public String getnAMACUST() {
        return nAMACUST;
    }

    public void setnAMACUST(String nAMACUST) {
        this.nAMACUST = nAMACUST;
    }

    public String getnOTELPCUST() {
        return nOTELPCUST;
    }

    public void setnOTELPCUST(String nOTELPCUST) {
        this.nOTELPCUST = nOTELPCUST;
    }

    @SerializedName("KODE_CUST")
    @Expose
    private Integer kODECUST;
    @SerializedName("NAMA_CUST")
    @Expose
    private String nAMACUST;
    @SerializedName("NO_TELP_CUST")
    @Expose
    private String nOTELPCUST;
}
