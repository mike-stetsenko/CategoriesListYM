package com.mairos.categories;

import android.app.Application;

public class CategoriesApplication extends Application {

    private static CategoriesApplication sInstance;

    public static CategoriesApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
