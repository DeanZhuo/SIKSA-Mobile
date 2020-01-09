package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.util.List;

public class Value {
    List<Sparepart> resultSparepart;
    List<PegawaiDAO> resultPegawai;

    public List<Sparepart> getSparepart(){
        return resultSparepart;
    }

    public List<PegawaiDAO> getPegawai(){
        return resultPegawai;
    }
}
