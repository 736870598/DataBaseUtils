package com.sxy.databasecore.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sxy.databasecore.annotation.SxyDBField;
import com.sxy.databasecore.annotation.SxyDBTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * BaseDao抽象类
 * Created by sunxiaoyu on 2017/1/13.
 */
public abstract class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase sqLiteDatabase;
    private boolean isInit;
    private String tableName;
    private Class<T> entityClass;
    private Map<String, String> cacheMap;

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * 初始化
     * @throws Exception 异常
     */
    public synchronized void init(Class<T> entity, SQLiteDatabase sqLiteDatabase, String tableName) throws Exception{
        if(!isInit){
            isInit = true;
            this.entityClass = entity;
            this.sqLiteDatabase = sqLiteDatabase;
            sqLiteDatabase.execSQL(getCreateTableStr(tableName));
        }
    }

    /**
     * 得到创建表的语句
     * 根据注解获取表名及列名
     * @throws Exception
     */
    private String getCreateTableStr(String _tableName) throws Exception{

        //得到表名
        if (_tableName == null || _tableName.isEmpty()){
            this.tableName = entityClass.getAnnotation(SxyDBTable.class).value();
        }else {
            this.tableName = _tableName;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ( _id INTEGER PRIMARY KEY AUTOINCREMENT");

        Field[] fields = entityClass.getDeclaredFields();
        int length = fields.length;
        for (int i = 0; i < length; i++) {

            Field field = fields[i];

            //如果没有设置DBField，直接跳过
            if (!field.isAnnotationPresent(SxyDBField.class)) {
                continue;
            }

            String lineName = field.getAnnotation(SxyDBField.class).value();
            sql.append(" , ").append(lineName).append(" ").append(getSqlLiteDBType(field.getType()));

            //将属性名 和 表列名对应关系保存在cacheMap中。
            if(cacheMap == null){
                cacheMap = new HashMap<>();
            }
            cacheMap.put(field.getName(), lineName);
        }
        sql.append(" ) ");
        return sql.toString();
    }

    /**
     * 根据属性的类型返回表字段类型
     * @param type  属性的类型
     * @return
     */
    private String getSqlLiteDBType(Class<?> type){
        if( type.equals(String.class) ){
            return "TEXT";
        }
        if( type.equals(char.class) ){
            return "CHAR";
        }
        if( type.equals(byte.class) || type.equals(Byte.class) ){
            return "BLOB";
        }
        if( type.equals(int.class) || type.equals(Integer.class) ){
            return "INTEGER";
        }
        if( type.equals(long.class) || type.equals(Long.class) ){
            return "INTEGER";
        }
        if( type.equals(short.class) || type.equals(Short.class) ){
            return "INTEGER";
        }
        if( type.equals(float.class) || type.equals(Float.class) ){
            return "FLOAT";
        }
        if( type.equals(double.class) || type.equals(Double.class) ){
            return "DOUBLE";
        }
        return "TEXT";
    }

    /**
     * 通过 T 返回ContentValues，用于操作表是使用
     * @param entity  model
     * @throws Exception 异常
     */
    private ContentValues getContentValues(T entity) throws Exception{
        ContentValues contentValues = new ContentValues();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            filed.setAccessible(true);
            Object value = filed.get(entity);
            if(value == null){
                continue;
            }
            String key = cacheMap.get(filed.getName());
            if(key == null){
                continue;
            }
            contentValues.put(key, value.toString());
        }

        return contentValues;

    }


    /**
     *  通过contentValues得到条件语句和条件参数
     */
    private class Condition{
        String whereClause;
        String[] whereArgs;

        Condition(ContentValues contentValues){
            Iterator iterator = contentValues.keySet().iterator();

            StringBuilder stringBuilder = new StringBuilder("1=1");
            List<String> list = new ArrayList<>();

            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String value = contentValues.getAsString(key);
                if(value != null && !value.isEmpty()){
                    stringBuilder.append(" and ");
                    stringBuilder.append(key);
                    stringBuilder.append("=?");
                    list.add(value);
                }
            }

            whereClause = stringBuilder.toString();
            whereArgs = list.toArray(new String[list.size()]);
        }
    }


    /**
     * 通过cursor返回model类
     * @throws Exception 异常
     */
    public T cursor2Model(Cursor curosr) throws Exception {

        T model = entityClass.newInstance();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            int columnIndex = curosr.getColumnIndex(cacheMap.get(field.getName()));
            Class type = field.getType();

            if (type.equals(String.class)) {
                field.set(model, curosr.getString(columnIndex));
            }else if( type.equals(boolean.class) || type.equals(Boolean.class) ){
                field.set(model, curosr.getString(columnIndex).equalsIgnoreCase("true"));
            }else if( type.equals(int.class) || type.equals(Integer.class) ){
                field.set(model, curosr.getInt(columnIndex));
            }else if( type.equals(long.class) || type.equals(Long.class) ){
                field.set(model, curosr.getLong(columnIndex));
            }else if( type.equals(float.class) || type.equals(Float.class) ){
                field.set(model, curosr.getFloat(columnIndex));
            }else if( type.equals(double.class) || type.equals(Double.class) ){
                field.set(model, curosr.getDouble(columnIndex));
            }else if( type.equals(short.class) || type.equals(Short.class) ){
                field.set(model, curosr.getShort(columnIndex));
            }
        }
        return model;
    }

    /**
     * 搜索表中所有字段
     * @return List
     * @throws Exception 异常
     */
    public List<T> getAllInfo() throws Exception{
        Cursor cursor = null;
        List<T> list = null;
        String sql = "SELECT * FROM " + tableName;
        try {
            cursor = quert(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                try {
                    T t = cursor2Model(cursor);
                    if(t != null){
                        if(list == null)
                            list = new ArrayList<>();

                        list.add(t);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(cursor != null)
                cursor.close();
        }
        return list;
    }


    /**
     * 插入 用户可以传入model实现自动插入，也可以重写该方法
     * @param entity  model
     * @return  插入状态
     * @throws Exception 异常
     */
    @Override
    public Long insert(T entity) throws Exception{
        synchronized (tableName){
            try {
                sqLiteDatabase.beginTransaction();
                Long result = sqLiteDatabase.insert(tableName, "_id", getContentValues(entity));
                sqLiteDatabase.setTransactionSuccessful();
                return result;
            }catch (Exception e){
                throw e;
            }finally {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    /**
     * 修改， 传入entity 和 where 将where的值修改为entity的值。
     * @param entity    目标model
     * @param where     修改的model（可以只写个别属性，作为修改时的查询条件）（注：int类型默认为0，使用Integer）
     * @return 修改状态
     * @throws Exception 异常
     */
    @Override
    public int update(T entity, T where) throws Exception {
        synchronized (tableName){
            try{
                sqLiteDatabase.beginTransaction();
                Condition condition = new Condition(getContentValues(where));
                int result = sqLiteDatabase.update(tableName, getContentValues(entity), condition.whereClause, condition.whereArgs);
                sqLiteDatabase.setTransactionSuccessful();
                return result;
            }catch (Exception e){
                throw e;
            }finally {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    /**
     * 删除
     * @param where  删除的model  （可以只写个别属性，作为删除时的查询条件）（注：int类型默认为0，使用Integer）
     * @return 删除状态
     * @throws Exception 异常
     */
    @Override
    public int delete(T where) throws Exception {
        synchronized (tableName){
            try{
                sqLiteDatabase.beginTransaction();
                Condition condition = new Condition(getContentValues(where));
                int result = sqLiteDatabase.delete(tableName, condition.whereClause, condition.whereArgs);
                sqLiteDatabase.setTransactionSuccessful();
                return result;
            }catch (Exception e){
                throw e;
            }finally {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    /**
     * 查找    （由于查找的要求及返回值各有不同，这里提供一个简单的查找方法，可以通过getAllInfo()方法查找表所有的字段）
     * @param sql   查找语句
     * @param args  查找值
     * @return  Cursor（使用完记得close）
     * @throws Exception 异常
     */
    @Override
    public Cursor quert(String sql, String[] args) throws Exception {
        return sqLiteDatabase.rawQuery(sql, args);
    }


    /**
     * 判断该信息是否已经存在
     */
    @Override
    public Boolean exist(T where) throws Exception {
        Condition condition = new Condition(getContentValues(where));
        Cursor cursor = sqLiteDatabase.query(tableName, null, condition.whereClause, condition.whereArgs, null, null, null);
        Boolean exist = ( cursor != null && cursor.getCount() > 0 );
        if (cursor != null)
            cursor.close();
        return exist;
    }
}
