package com.lsu.joec.utils;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018-4-17
 * Brief Desc :
 * </pre>
 */
public class Singleton {

    public Handler handler;

    public Gson gson;

    public Gson pretty;

    private Singleton() {
        handler = new Handler();

        gson = new Gson();
        pretty = new GsonBuilder().setPrettyPrinting().create();
    }

    public static Singleton get() {
        return SingletonHolder.instance;
    }


    private static class SingletonHolder {
        private static final Singleton instance = new Singleton();
    }
}
