package com.sunxiaoyu.batabaseutilsdemo.dao;


import com.sunxiaoyu.batabaseutilsdemo.model.User;
import com.sxy.databasecore.core.BaseDao;

/**
 * Created by sunxiaoyu on 2017/1/13.
 */

public class UserDao extends BaseDao<User> {

    @Override
    public Long insert(User entity) throws Exception{
        return super.insert(entity);
    }

    @Override
    public int update(User entity, User where) throws Exception{
        return super.update(entity, where);
    }

    @Override
    public int delete(User where) throws Exception {
        return super.delete(where);
    }

    public void getResult(){


    }
}
