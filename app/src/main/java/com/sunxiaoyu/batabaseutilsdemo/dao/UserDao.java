package com.sunxiaoyu.batabaseutilsdemo.dao;


import com.sxy.databasecore.core.BaseDao;

/**
 * Created by sunxiaoyu on 2017/1/13.
 */

public class UserDao extends BaseDao {

    @Override
    public Long insert(Object entity) throws Exception{
        return super.insert(entity);
    }

    @Override
    public int update(Object entity, Object where) throws Exception{
        return super.update(entity, where);
    }

    @Override
    public int delete(Object where) throws Exception {
        return super.delete(where);
    }

    public void getResult(){


    }
}
