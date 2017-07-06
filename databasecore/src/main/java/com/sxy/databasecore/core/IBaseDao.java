package com.sxy.databasecore.core;

import android.database.Cursor;

/**
 * BaseDao 增 删 改 查 四个方法
 * Created by sunxiaoyu on 2017/1/13.
 */
public interface IBaseDao<T> {

    /**
     * 增加
     */
    Long insert(T entity) throws Exception;

    /**
     * 修改
     */
    int update(T entity, T where) throws Exception;

    /**
     * 删除
     */
    int delete(T where) throws Exception;

    /**
     * 查找
     */
    Cursor quert(String sql, String[] strs) throws Exception;

    /**
     * 是否已经存在
     */
    Boolean exist(T where) throws Exception;

}
