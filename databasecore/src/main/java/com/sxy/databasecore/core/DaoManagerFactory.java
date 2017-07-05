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


    public static DaoManagerFactory getInstance(String dbPath, String dbName){
        DaoManagerFactory.dbPath = dbPath;
        DaoManagerFactory.dbName = dbName;
        return DaoManagerFactoryHolder.instance;
    }

    public static DaoManagerFactory getInstance(){
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
     * @throws Exception
     */
    public  <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClazz) throws Exception{
        return getDataHelper(clazz, entityClazz, "");
    }

    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClazz, String tableName) throws Exception{
        BaseDao baseDao = clazz.newInstance();
        baseDao.init(entityClazz, sqLiteDatabase, tableName);
        return (T) baseDao;
    }
}
