package com.example.bitlinker.translator.ui.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bitlinker.translator.App;
import com.example.bitlinker.translator.R;
import com.example.bitlinker.translator.di.main.MainModule;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.ui.presenter.IMainPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainView {

    public static final String TAG = "MainActivity";

    @Inject
    IMainPresenter mMainPresenter;

    @BindView(R.id.txtSearch) EditText mTxtSearch;
    @BindView(R.id.lstTranslations) RecyclerView mLstTranslations;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.txtCopyright) TextView mCopyrightText;

    private TranslationsListAdapter mAdapter;

    public class TranslationsListAdapter extends RecyclerView.Adapter<TranslationsListAdapter.TranslationViewHolder> {
        List<TranslatedText> mItems = new ArrayList<>();

        public TranslationsListAdapter() {
            updateList("");
        }

        void updateList(String filter) {
            Subscriber<List<TranslatedText>> loadDataSubscriber = new Subscriber<List<TranslatedText>>() {

                @Override
                public void onCompleted() {
                    unsubscribe();
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "Error loading items", e);
                    showError(e); // TODO: error reporting in presenter
                    unsubscribe();
                }

                @Override
                public void onNext(List<TranslatedText> translatedTexts) {
                    mItems = translatedTexts;
                    notifyDataSetChanged();
                }
            };
            mMainPresenter.onSearchTextChanged(filter).subscribe(loadDataSubscriber);
        }

        @Override
        public TranslationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item, parent, false);
            TranslationViewHolder holder = new TranslationViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(TranslationViewHolder holder, int position) {
            TranslatedText text = mItems.get(position);
            holder.applyData(text);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class TranslationViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cardView) CardView mCardView;
            @BindView(R.id.txtOriginalText) TextView mOriginalText;
            @BindView(R.id.txtTranslatedText) TextView mTranslatedText;
            @BindView(R.id.txtLanguage) TextView mLanguage;

            public TranslationViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void applyData(TranslatedText text) {
                mOriginalText.setText(text.getOriginalText());
                mTranslatedText.setText(text.getTranslatedText());
                mLanguage.setText(text.getLanguage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        App.getComponent().plus(new MainModule()).inject(this);
        mMainPresenter.bindView(this);

        // TODO: coordinator layout for search; custom scroll behaviours
        mCopyrightText.setMovementMethod(LinkMovementMethod.getInstance());
        initList();
        mTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                //mMainPresenter.onSearchTextChanged(newText);
                mAdapter.updateList(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.unbindView();
        super.onDestroy();
    }

    void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mLstTranslations.setLayoutManager(llm);
        mAdapter = new TranslationsListAdapter();
        mLstTranslations.setAdapter(mAdapter);
        mLstTranslations.setHasFixedSize(true);
    }

    @Override
    @OnClick(R.id.fab)
    public void onClick(View v) {
        Subscriber<TranslatedText> subscriber = new Subscriber<TranslatedText>() {

            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error loading items", e);
                showError(e); // TODO: error reporting in presenter
                unsubscribe();
            }

            @Override
            public void onNext(TranslatedText translatedText) {
                mTxtSearch.getText().clear();
                mAdapter.updateList("");
            }
        };
        mMainPresenter.onAddButtonPressed(mTxtSearch.getText().toString()).subscribe(subscriber);
    }

    @Override
    public void showAddButton(boolean show) {
        if (show) {
            mFab.show();
        } else {
            mFab.hide();
        }
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(mFab, e.getMessage(), Snackbar.LENGTH_SHORT).show(); // TODO
        //Snackbar.make(mFab, getString(R.string.error_cant_translate), Snackbar.LENGTH_SHORT).show(); // TODO
    }
}
