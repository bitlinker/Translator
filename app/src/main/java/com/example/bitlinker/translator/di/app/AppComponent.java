package com.example.bitlinker.translator.di.app;

import com.example.bitlinker.translator.di.main.MainComponent;
import com.example.bitlinker.translator.di.main.MainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bitlinker on 20.08.2016.
 */
@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    MainComponent plus(MainModule mainModule);
}
