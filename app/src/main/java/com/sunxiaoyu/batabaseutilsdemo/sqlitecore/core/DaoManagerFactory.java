package com.sunxiaoyu.batabaseutilsdemo.sqlitecore.core;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * BaseDao工厂
 * Created by sunxiaoyu on 2017/1/13.
 */
public class DaoManagerFactory {

    private static DaoManagerFactory factory;

    private SQLiteDatabase sqLiteDatabase;
    //数据库保存路径及名字
    private String dbPath = Environment.getExternalStorageDirectory().getPath() + "/sxy";
    private String dbName = "mydb.db";

    /**
     * 单例模式
     */
    public static DaoManagerFactory getInstance(){
        if(factory == null){
            factory = new DaoManagerFactory();
        }
        return factory;
    }

    /**
     * 构造函数
     */
    private DaoManagerFactory(){
        try {
            openDataBase();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建或打开DataBase
     */
    private void openDataBase() throws IOException {
        File file = new File(dbPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String db_path = dbPath + "/" + dbName;
        file = new File(db_path);
        file.createNewFile();
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
    }

    /**
     * 工厂模式，创建BaseDao
     * @throws Exception
     */
    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entityClazz) throws Exception{
        BaseDao baseDao = clazz.newInstance();
        baseDao.init(entityClazz, sqLiteDatabase);
        return (T) baseDao;
    }
}
