package com.sunxiaoyu.batabaseutilsdemo;

import android.app.Activity;
import android.os.Bundle;

import com.sunxiaoyu.batabaseutilsdemo.dao.UserDao;
import com.sunxiaoyu.batabaseutilsdemo.model.User;
import com.sunxiaoyu.batabaseutilsdemo.sqlitecore.core.DaoManagerFactory;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            UserDao userDao = DaoManagerFactory.getInstance().getDataHelper(UserDao.class, User.class);

            User p = new User();
            p.setName("找三");


            userDao.delete(p);
        } catch (Exception e) {
            e.printStackTrace();
        }







    }
}
