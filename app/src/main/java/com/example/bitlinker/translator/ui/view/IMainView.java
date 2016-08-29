package com.example.bitlinker.translator.ui.view;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.model.TranslationError;

import java.util.List;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IMainView {
    void showAddButton(boolean isShow);
    void setSearchText(String text);
    void updateList(List<TranslatedText> items);
    void showProgressBar(boolean isShow);
    void showError(TranslationError e);
}