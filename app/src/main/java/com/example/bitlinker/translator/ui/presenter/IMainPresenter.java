package com.example.bitlinker.translator.ui.presenter;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.ui.view.IMainView;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IMainPresenter {
    void bindView(IMainView iMainView);
    void unbindView();

    void onSearchTextChanged(String text);
    void onAddButtonPressed();
    void onDeleteItemPressed(TranslatedText item);
}