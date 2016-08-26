package com.example.bitlinker.translator.di.main;

import com.example.bitlinker.translator.ui.view.MainActivity;

import dagger.Subcomponent;

/**
 * Created by bitlinker on 22.08.2016.
 */

@Subcomponent(modules = {MainModule.class})
@MainScope
public interface MainComponent {

    void inject(MainActivity mainActivity);

}