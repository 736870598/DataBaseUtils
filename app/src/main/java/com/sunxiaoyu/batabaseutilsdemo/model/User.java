package com.sunxiaoyu.batabaseutilsdemo.model;

import com.sunxiaoyu.batabaseutilsdemo.sqlitecore.annotation.DBField;
import com.sunxiaoyu.batabaseutilsdemo.sqlitecore.annotation.DBTable;

/**
 * Created by sunxiaoyu on 2017/1/13.
 */
@DBTable("user")
public class User {
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
