package com.sunxiaoyu.batabaseutilsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.sunxiaoyu.batabaseutilsdemo.dao.UserDao;
import com.sunxiaoyu.batabaseutilsdemo.model.User;
import com.sxy.databasecore.core.DaoManagerFactory;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sxysxysxy";
            String name = "sssxxxyyy.db";

            UserDao userDao = DaoManagerFactory.getInstance(path, name).getDataHelper(UserDao.class, User.class, "asda");

            User p = new User();
            p.setName("updata找三找三");
            p.setPassWord(123321123);


            userDao.delete(p);
        } catch (Exception e) {
            e.printStackTrace();
        }







    }
}
