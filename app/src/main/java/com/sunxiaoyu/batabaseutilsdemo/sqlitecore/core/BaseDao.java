package com.sunxiaoyu.batabaseutilsdemo.sqlitecore.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sunxiaoyu.batabaseutilsdemo.sqlitecore.annotation.DBField;
import com.sunxiaoyu.batabaseutilsdemo.sqlitecore.annotation.DBTable;

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

        protected SQLiteDatabase sqLiteDatabase;
        private boolean isInit;
        protected String tableName;
        private Class<T> entityClass;
        private Map<String, String> cacheMap;

        /**
         * 初始化
         * @throws Exception
         */
    public synchronized void init(Class<T> entity, SQLiteDatabase sqLiteDatabase) throws Exception{
        if(!isInit){
            isInit = true;
            this.entityClass = entity;
            this.sqLiteDatabase = sqLiteDatabase;
            this.sqLiteDatabase.execSQL(getCreateTableStr());
        }
    }

    /**
     * 得到创建表的语句
     * 根据注解获取表名及列名
     * @throws Exception
     */
    protected String getCreateTableStr() throws Exception{

        //如果设置了表名，就用设置了的表名，否则表名为类名。
        if (!entityClass.isAnnotationPresent(DBTable.class)) {
            this.tableName = entityClass.getSimpleName();
        }else{
            //得到表名
            this.tableName = entityClass.getAnnotation(DBTable.class).value();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("Create TABLE IF NOT EXISTS " + tableName + " ( _id INTEGER PRIMARY KEY autoincrement, ");

        Field[] fields = entityClass.getDeclaredFields();
        int length = fields.length;
        for (int i = 0; i < length; i++) {

            Field field = fields[i];
            String lineName;

            //如果设置了列名，就用设置了的列名，否则列名为属性名。
            if (field.isAnnotationPresent(DBField.class)) {
                lineName = field.getAnnotation(DBField.class).value();
            }else{
                lineName = field.getName();
            }

            sql.append( lineName + " " + getSqlLiteDBType(field.getType()) );
            if( i < length - 1 ){
                sql.append(" , ");
            }

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
    protected String getSqlLiteDBType(Class<?> type){
        if( type.equals(String.class) ){
            return "TEXT";
        }
        if( type.equals(int.class) || type.equals(Integer.class) ){
            return "INTEGER";
        }
        if( type.equals(float.class) || type.equals(Float.class) ){
            return "FLOAT";
        }
        if( type.equals(double.class) || type.equals(Double.class) ){
            return "DOUBLE";
        }
        if( type.equals(Long.class) || type.equals(Double.class) || type.equals(Float.class)){
            return "LONG";
        }
        return "TEXT";
    }


    /**
     * 通过 T 返回ContentValues，用于操作表是使用
     * @param entity  model
     * @throws Exception
     */
    protected ContentValues getContentValues(T entity) throws Exception{

        ContentValues contentValues = new ContentValues();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            filed.setAccessible(true);
            String key = cacheMap.get(filed.getName());
            if(key == null){
                continue;
            }
            Object value = filed.get(entity);
            if(value == null){
                continue;
            }
            contentValues.put(cacheMap.get(filed.getName()), value.toString());
        }

        return contentValues;

    }


    /**
     *  通过contentValues得到条件语句和条件参数
     */
    class Condition{
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
     * @throws Exception
     */
    protected <T> T cursor2Model(Cursor curosr) throws Exception {

        T model = (T) entityClass.newInstance();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            int columnIndex = curosr.getColumnIndex(cacheMap.get(field.getName()));
            Class type = field.getType();

            if (type.equals(String.class)) {
                field.set(model, curosr.getString(columnIndex));
            }else if( type.equals(int.class) || type.equals(Integer.class) ){
                field.set(model, curosr.getInt(columnIndex));
            }else if( type.equals(float.class) || type.equals(Float.class) ){
                field.set(model, curosr.getFloat(columnIndex));
            }
            else if( type.equals(double.class) || type.equals(Double.class) ){
                field.set(model, curosr.getFloat(columnIndex));
            }else if( type.equals(short.class) || type.equals(Short.class) ){
                field.set(model, curosr.getFloat(columnIndex));
            }
        }
        return model;
    }

    /**
     * 搜索表中所有字段
     * @return List
     * @throws Exception
     */
    public List<T> getAllInfo() throws Exception{
        Cursor cursor = null;
        List<T> list = null;
        String sql = "SELECT * FROM " + tableName;

        cursor = quert(sql, null);

        if(cursor == null){
            return null;
        }

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
                continue;
            }finally {
                cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

    /**
     * 插入 用户可以传入model实现自动插入，也可以重写该方法
     * @param entity  model
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Long insert(T entity) throws Exception{
        return sqLiteDatabase.insert(tableName, "_id", getContentValues(entity));
    }

    /**
     * 修改， 传入entity 和 where 将where的值修改为entity的值。
     * @param entity    目标model
     * @param where     修改的model（可以只写个别属性，作为修改时的查询条件）（注：int类型默认为0，使用Integer）
     * @return
     * @throws Exception
     */
    @Override
    public synchronized int update(T entity, T where) throws Exception {
        Condition condition = new Condition(getContentValues(where));
        return sqLiteDatabase.update(tableName, getContentValues(entity), condition.whereClause, condition.whereArgs);
    }

    /**
     * 删除
     * @param where  删除的model  （可以只写个别属性，作为删除时的查询条件）（注：int类型默认为0，使用Integer）
     * @return
     * @throws Exception
     */
    @Override
    public synchronized int delete(T where) throws Exception {
        Condition condition = new Condition(getContentValues(where));
        return sqLiteDatabase.delete(tableName, condition.whereClause, condition.whereArgs);
    }

    /**
     * 查找    （由于查找的房子及要求的返回值各有不同，这里提供一个简单的查找方法，可以通过getAllInfo()方法查找表所有的字段）
     * @param sql   查找语句
     * @param args  查找值
     * @return  Cursor（使用完记得close）
     * @throws Exception
     */
    @Override
    public Cursor quert(String sql, String[] args) throws Exception {
        return sqLiteDatabase.rawQuery(sql, args);
    }
}
