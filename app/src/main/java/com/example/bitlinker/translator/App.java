package com.example.bitlinker.translator;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.bitlinker.translator.di.app.DaggerAppComponent;
import com.example.bitlinker.translator.di.app.AppComponent;
import com.example.bitlinker.translator.di.app.AppModule;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class App extends Application {

    @NonNull
    private static AppComponent mAppComponent;

    @NonNull
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
    }

    @NonNull
    public static AppComponent getComponent() {
        return mAppComponent;
    }
}
