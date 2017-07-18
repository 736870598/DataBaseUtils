package com.sunxiaoyu.batabaseutilsdemo.model;


import com.sxy.databasecore.annotation.SxyDBField;
import com.sxy.databasecore.annotation.SxyDBTable;

import java.io.Serializable;

/**
 * Created by sunxiaoyu on 2017/1/13.
 */
@SxyDBTable("myuser")
public class User implements Serializable{

    @SxyDBField("name")
    private String name;
    @SxyDBField("password")
    private Integer passWord;
    @SxyDBField("flag")
    private Boolean flag;
    @SxyDBField("temp")
    private Long temp;
    @SxyDBField("grend")
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
