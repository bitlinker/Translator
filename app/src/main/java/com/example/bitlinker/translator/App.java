package com.example.bitlinker.translator;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.bitlinker.translator.di.AppComponent;
import com.example.bitlinker.translator.di.AppProviderModule;
import com.example.bitlinker.translator.di.DaggerAppComponent;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class App extends Application {

    private static AppComponent mAppComponent;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appProviderModule(new AppProviderModule())
                .build();
    }

    @NonNull
    public static AppComponent getComponent() {
        return mAppComponent;
    }

    @NonNull
    public static Context getContext() {
        return mContext;
    }
}
