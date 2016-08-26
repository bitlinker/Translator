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

    public static class TranslationsListAdapter extends RecyclerView.Adapter<TranslationsListAdapter.TranslationViewHolder> {
        List<TranslatedText> mItems = new ArrayList<>();

        public TranslationsListAdapter() {
            // TODO: dbg
            mItems.add(new TranslatedText("Test", "Тест", "ru"));
            mItems.add(new TranslatedText("Test", "Тест3333", "ru"));
            mItems.add(new TranslatedText("Test44444ffffffffffffffffffffffffffffffffffffffffff444", "Тест222ddddddddddddddddddddddddddddddddddddddddddddddddddd", "ru"));
        }

        // TODO: subscribe

        void updateList() {
            Subscriber<List<TranslatedText>> loadDataSubscriber = new Subscriber<List<TranslatedText>>() {

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "Error loading items", e);
                }

                @Override
                public void onNext(List<TranslatedText> translatedTexts) {
                    mItems = translatedTexts;
                    notifyDataSetChanged();
                }
            };
            // TODO: subscribe?
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

        static class TranslationViewHolder extends RecyclerView.ViewHolder {
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
                mMainPresenter.onSearchTextChanged(newText);
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
        TranslationsListAdapter adapter = new TranslationsListAdapter();
        mLstTranslations.setAdapter(adapter);
        mLstTranslations.setHasFixedSize(true);
    }

    @Override
    @OnClick(R.id.fab)
    public void onClick(View v) {
        mMainPresenter.onAddButtonPressed(mTxtSearch.getText().toString());
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
    public void updateTranslationsList() {
        // TODO
    }

    @Override
    public void showError() {
        Snackbar.make(mFab, getString(R.string.error_cant_translate), Snackbar.LENGTH_SHORT).show(); // TODO
        //.setAction("Action", null).show();
    }
}
