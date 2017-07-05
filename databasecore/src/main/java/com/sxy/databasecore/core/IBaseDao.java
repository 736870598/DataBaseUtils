package com.sxy.databasecore.core;

import android.database.Cursor;

/**
 * BaseDao 增 删 改 查 四个方法
 * Created by sunxiaoyu on 2017/1/13.
 */
public interface IBaseDao<T> {

    Long insert(T entity) throws Exception;

    int update(T entity, T where) throws Exception;

    int delete(T where) throws Exception;

    Cursor quert(String sql, String[] strs) throws Exception;

}
