package com.example.bitlinker.translator.di;

import com.example.bitlinker.translator.MainActivity;

import dagger.Component;

/**
 * Created by bitlinker on 20.08.2016.
 */
@Component(modules = {AppProviderModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
