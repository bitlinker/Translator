package com.example.bitlinker.translator;

import dagger.Component;

/**
 * Created by bitlinker on 20.08.2016.
 */
@Component(modules = {ModuleProvider.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
