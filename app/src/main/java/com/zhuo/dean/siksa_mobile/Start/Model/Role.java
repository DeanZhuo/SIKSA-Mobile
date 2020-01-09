package com.zhuo.dean.siksa_mobile.Start.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("KODE_ROLE")
    @Expose
    private Integer kODEROLE;
    @SerializedName("JENIS_ROLE")
    @Expose
    private String jENISROLE;

    public Integer getKODEROLE() {
        return kODEROLE;
    }

    public void setKODEROLE(Integer kODEROLE) {
        this.kODEROLE = kODEROLE;
    }

    public String getJENISROLE() {
        return jENISROLE;
    }

    public void setJENISROLE(String jENISROLE) {
        this.jENISROLE = jENISROLE;
    }
}
