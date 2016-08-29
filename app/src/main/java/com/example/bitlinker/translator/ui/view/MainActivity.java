package com.example.bitlinker.translator.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bitlinker.translator.App;
import com.example.bitlinker.translator.R;
import com.example.bitlinker.translator.di.main.MainModule;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.model.TranslationError;
import com.example.bitlinker.translator.ui.presenter.IMainPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainView {

    public static final int PERMISSION_REQUEST_INTERNET = 1;

    @Inject
    IMainPresenter mMainPresenter;

    @BindView(R.id.txtSearch) EditText mTxtSearch;
    @BindView(R.id.lstTranslations) RecyclerView mLstTranslations;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.txtCopyright) TextView mCopyrightText;
    @BindView(R.id.prg_progress) ProgressBar mProgressBar;

    private TranslationsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        App.getComponent().plus(new MainModule()).inject(this);

        mMainPresenter.bindView(this);

        checkPermissions();

        // TODO: coordinator layout for search; custom scroll behaviours
        mCopyrightText.setMovementMethod(LinkMovementMethod.getInstance());

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

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mLstTranslations.setLayoutManager(llm);

        View.OnLongClickListener longClickListener = view -> {
            // TODO: in adapter!
            TranslatedText item = (TranslatedText) view.getTag();
            mMainPresenter.onDeleteItemPressed(item);
            return true;
        };
        mAdapter = new TranslationsListAdapter(longClickListener);
        mLstTranslations.setAdapter(mAdapter);
        mLstTranslations.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.unbindView();
        super.onDestroy();
    }

    private void checkPermissions() {
        String[] permissions = new String[]{Manifest.permission.INTERNET};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_INTERNET);
        } else {
            onRequestPermissionsResult(PERMISSION_REQUEST_INTERNET, permissions, new int[]{PackageManager.PERMISSION_GRANTED});
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_INTERNET) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // TODO: show some error...
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @OnClick(R.id.fab)
    public void onClick(View v) {
        mMainPresenter.onAddButtonPressed();
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
    public void showError(TranslationError e) {
        Snackbar.make(mFab, e.getMessage(this), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateList(List<TranslatedText> items) {
        mAdapter.updateList(items);
    }

    @Override
    public void showProgressBar(boolean isShow) {
        mProgressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mLstTranslations.setEnabled(!isShow);
        if (isShow) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void setSearchText(String text) {
        mTxtSearch.setText(text);
    }
}
