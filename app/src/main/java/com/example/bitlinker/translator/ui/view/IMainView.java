package com.example.bitlinker.translator.ui.view;

import android.support.annotation.NonNull;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.model.TranslationError;

import java.util.List;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IMainView {
    void showAddButton(boolean isShow);
    void setSearchText(String text);
    void updateList(@NonNull List<TranslatedText> items);
    void onListItemRemoved(TranslatedText item);
    void showProgressBar(boolean isShow);
    void showError(@NonNull TranslationError e);

}