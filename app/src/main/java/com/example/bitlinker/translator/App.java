package com.example.bitlinker.translator;

import android.app.Application;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class App extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .moduleProvider(new ModuleProvider())
                .build();

    }

    public static AppComponent getComponent() {
        return mAppComponent;
    }
}
