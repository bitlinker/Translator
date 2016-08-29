package com.example.bitlinker.translator.ui.presenter;

import android.text.TextUtils;

import com.example.bitlinker.translator.domain.IMainInteractor;
import com.example.bitlinker.translator.domain.MainInteractor;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.ui.view.IMainView;

import java.util.List;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bitlinker on 22.08.2016.
 */

public class MainPresenter implements IMainPresenter {

    private IMainView mMainView;
    private IMainInteractor mMainInteractor;

    public MainPresenter(IMainInteractor interactor) {
        mMainInteractor = interactor;
    }

    @Override
    public void bindView(IMainView iMainView) {
        mMainView = iMainView;
    }

    @Override
    public void unbindView() {
        mMainView = null;
    }

    @Override
    public Single<List<TranslatedText>> onSearchTextChanged(String text) {
        if (TextUtils.isEmpty(text)) {
            mMainView.showAddButton(false);
        } else {
            mMainView.showAddButton(true);
        }
        return mMainInteractor.getTranslatedItems(text);
    }

    @Override
    public Single<TranslatedText> onAddButtonPressed(String text) {
        return mMainInteractor.translateAndAddItem(text);
    }
}
