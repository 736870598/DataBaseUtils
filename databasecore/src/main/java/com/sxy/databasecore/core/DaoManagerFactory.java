package com.sxy.databasecore.core;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * BaseDao工厂
 * Created by sunxiaoyu on 2017/1/13.
 */
public class DaoManagerFactory {

    private SQLiteDatabase sqLiteDatabase;
    //数据库保存路径及名字
    private static String dbPath = Environment.getExternalStorageDirectory().getPath() + "/database";
    private static String dbName = "baseDB.db";


    private static class DaoManagerFactoryHolder{
        static DaoManagerFactory instance = new DaoManagerFactory();
    }


    public static DaoManagerFactory getFactory(String dbPath, String dbName){
        DaoManagerFactory.dbPath = dbPath;
        DaoManagerFactory.dbName = dbName;
        return DaoManagerFactoryHolder.instance;
    }

    public static DaoManagerFactory getFactory(){
        return DaoManagerFactoryHolder.instance;
    }

    /**
     * 构造函数
     */
    private DaoManagerFactory(){
        try {
            openDataBase(DaoManagerFactory.dbPath, DaoManagerFactory.dbName);
        }catch (IOException e){
            try {
                throw e;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    /**
     * 创建或打开DataBase
     */
    private void openDataBase(String path, String name) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        String db_path = path + "/" + name;
        file = new File(db_path);
        file.createNewFile();
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
    }

    /**
     * 工厂模式，创建BaseDao
     * @param clazz           子创建BaseDao
     * @param entityClazz     数据类
     */
    public  <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClazz) throws Exception{
        return getDataHelper(clazz, entityClazz, null);
    }

    /**
     * 工厂模式，创建BaseDao
     * @param clazz           子创建BaseDao
     * @param entityClazz     数据类
     * @param tableName       表名（如果设置该值，则注解表名不生效, 如果没有设置该字段而且没有注解表名，则可能报错）
     */
    public <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClazz, String tableName) throws Exception{
        return getDataHelper(clazz, entityClazz, tableName, true);
    }

    /**
     * 工厂模式，创建BaseDao
     * @param clazz           子创建BaseDao
     * @param entityClazz     数据类
     * @param tableName       表名（如果设置该值，则注解表名不生效，如果没有设置该字段而且没有注解表名，则可能报错）
     * @param useAnnotation   是否使用注解（如果不使用注解，字段名则使用“ _属性名 ”）
     */
    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClazz, String tableName, boolean useAnnotation) throws Exception{
        BaseDao baseDao = clazz.newInstance();
        baseDao.init(entityClazz, sqLiteDatabase, tableName, useAnnotation);
        return (T) baseDao;
    }
}
