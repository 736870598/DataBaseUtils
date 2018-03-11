package com.sxy.databasecore.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * BaseDao
 * Created by sunxiaoyu on 2017/1/13.
 */
public interface IBaseDao<T> {

    /**
     * 获取getSqLiteDatabase
     */
    SQLiteDatabase getSqLiteDatabase();

    /**
     * 获取表名
     */
    String getTableName();

    /**
     * 增加
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    Long insert(T entity) throws Exception;

    /**
     * 修改
     * @return the number of rows affected
     */
    int update(T entity, T where) throws Exception;

    /**
     * 删除
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    int delete(T where) throws Exception;

    /**
     * 查找
     * @return Cursor
     */
    List<T> quert(String sql, String[] strs) throws Exception;

    /**
     *  执行
     */
    void execSQL(String string, Object[] objects) throws Exception;

    /**
     * 是否已经存在
     */
    Boolean exist(T where) throws Exception;

    /**
     * 获取表中所用的消息
     */
    List<T> getAllInfo() throws Exception;

    /**
     * Cursor 转 model
     */
    T cursor2Model(Cursor curosr) throws Exception;

}
