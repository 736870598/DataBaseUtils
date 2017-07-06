package com.sunxiaoyu.batabaseutilsdemo.model;


import android.util.Log;

import com.sxy.databasecore.annotation.DBField;
import com.sxy.databasecore.annotation.DBTable;

import java.io.Serializable;

/**
 * Created by sunxiaoyu on 2017/1/13.
 */
@DBTable("myuser")
public class User implements Serializable{

    @DBField("name")
    private String name;
    @DBField("password")
    private Integer passWord;
    @DBField("flag")
    private Boolean flag;
    @DBField("temp")
    private Long temp;
    @DBField("grend")
    private float grend;

    private String other;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPassWord() {
        return passWord;
    }

    public void setPassWord(Integer passWord) {
        this.passWord = passWord;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Long getTemp() {
        return temp;
    }

    public void setTemp(Long temp) {
        this.temp = temp;
    }

    public float getGrend() {
        return grend;
    }

    public void setGrend(float grend) {
        this.grend = grend;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
