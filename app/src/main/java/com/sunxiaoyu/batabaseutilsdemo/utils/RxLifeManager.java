package com.sunxiaoyu.batabaseutilsdemo.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 *
 * Created by sunxiaoyu on 2017/7/18.
 */
public class RxLifeManager {

    private HashMap<Context,List<Disposable>> disHashMap;

    private static class RxLifeManagerHolder{
        static RxLifeManager instance = new RxLifeManager();
    }

    public static RxLifeManager getManager(){
        return RxLifeManagerHolder.instance;
    }

    private RxLifeManager(){
        disHashMap = new HashMap<>();
    }

    /**
     * 添加disposable用于统一管理
     */
    public void addDisposable(Context context, Disposable disposable){
        if (disHashMap == null){
            throw new RuntimeException("没有初始化HashMap");
        }

        if (context == null || disposable == null){
            return ;
        }

        List<Disposable> list = disHashMap.get(context);
        if (list == null){
            list = Collections.synchronizedList(new ArrayList<Disposable>());
        }
        list.add(disposable);

        disHashMap.put(context, list);
    }

    /**
     * 移除并结束disposable
     */
    public void endAndRemoveDisposable(Context context){

        if (disHashMap == null){
            throw new RuntimeException("没有初始化HashMap");
        }

        if (context == null){
            return ;
        }

        List<Disposable> list = disHashMap.get(context);

        if (list != null){
            for (Disposable disposable : list){
                if (disposable != null && !disposable.isDisposed()){
                    disposable.dispose();
                }
            }
        }

        disHashMap.remove(context);

    }
}
