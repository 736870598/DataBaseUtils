package com.sunxiaoyu.batabaseutilsdemo.model;


import com.sxy.databasecore.annotation.DBField;
import com.sxy.databasecore.annotation.DBTable;

import java.io.Serializable;

/**
 * Created by sunxiaoyu on 2017/1/13.
 */
@DBTable("user")
public class User implements Serializable{

    @DBField("name")
    private String name;
    @DBField("password")
    private Integer passWord;
    private String other;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassWord() {
        return passWord;
    }

    public void setPassWord(int passWord) {
        this.passWord = passWord;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
