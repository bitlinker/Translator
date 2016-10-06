package com.example.bitlinker.translator.ui.presenter;

import android.text.TextUtils;

import com.example.bitlinker.translator.domain.IMainInteractor;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.model.TranslationError;
import com.example.bitlinker.translator.ui.view.IMainView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bitlinker on 22.08.2016.
 */

public class MainPresenter implements IMainPresenter {

    private IMainView mMainView;
    private IMainInteractor mMainInteractor;
    private String mCurText = "";

    public MainPresenter(IMainInteractor interactor) {
        mMainInteractor = interactor;
    }

    @Override
    public void bindView(IMainView iMainView) {
        mMainView = iMainView;
        updateList(mCurText);
    }

    @Override
    public void unbindView() {
        mMainView = null;
    }

    @Override
    public void onSearchTextChanged(String text) {
        mCurText = text;
        updateList(mCurText);
    }

    @Override
    public void onAddButtonPressed() {
        Subscriber<TranslatedText> translateSubscriber = new Subscriber<TranslatedText>() {

            @Override
            public void onCompleted() {
                if (mMainView != null) {
                    updateList(mCurText);
                }
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                if (mMainView != null) {
                    mMainView.showError(translateError(e));
                    mMainView.showProgressBar(false);
                }
                unsubscribe();
            }

            @Override
            public void onNext(TranslatedText translatedText) {
                if (mMainView != null) {
                    mCurText = "";
                    mMainView.setSearchText(mCurText);
                }
            }
        };
        if (mMainView != null) {
            mMainView.showProgressBar(true);
        }
        mMainInteractor.translateAndAddItem(mCurText)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translateSubscriber);
    }

    private void updateList(String filter) {
        Subscriber<List<TranslatedText>> updateListSubscriber = new Subscriber<List<TranslatedText>>() {

            @Override
            public void onCompleted() {
                if (mMainView != null) {
                    mMainView.showProgressBar(false);
                }
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                if (mMainView != null) {
                    mMainView.showError(translateError(e));
                    mMainView.showProgressBar(false);
                }
                unsubscribe();
            }

            @Override
            public void onNext(List<TranslatedText> items) {
                if (mMainView != null) {
                    mMainView.updateList(items);
                    mMainView.showAddButton(items.size() == 0 && !TextUtils.isEmpty(filter));
                }
            }
        };
        mMainView.showProgressBar(true);
        mMainInteractor.getTranslatedItems(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateListSubscriber);
    }

    @Override
    public void onDeleteItemPressed(TranslatedText item) {
        Subscriber<Boolean> deleteSubscriber = new Subscriber<Boolean>() {

            @Override
            public void onCompleted() {
                if (mMainView != null) {
                    mMainView.onListItemRemoved(item);
                }
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                if (mMainView != null) {
                    mMainView.showError(translateError(e));
                    mMainView.showProgressBar(false);
                }
                unsubscribe();
            }

            @Override
            public void onNext(Boolean result) {
                // Do nothing
            }
        };

        mMainInteractor.deleteItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteSubscriber);
    }

    private TranslationError translateError(Throwable e) {
        return new TranslationError(); // TODO
    }
}
