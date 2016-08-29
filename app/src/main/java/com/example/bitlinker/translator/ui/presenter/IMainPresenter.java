package com.example.bitlinker.translator.ui.presenter;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.ui.view.IMainView;

import java.util.List;

import rx.Single;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IMainPresenter {
    void bindView(IMainView iMainView);
    void unbindView();

    Single<List<TranslatedText>> onSearchTextChanged(String text);
    Single<TranslatedText> onAddButtonPressed(String text);
}