package com.example.bitlinker.translator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.TranslateApi;
import com.example.bitlinker.translator.translateapi.TranslateException;
import com.example.bitlinker.translator.translateapi.yandex.YandexTranslateApi;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexTranslateException;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.txtSearch) TextView mTxtSearch;
    @BindView(R.id.lstTranslations) RecyclerView mLstTranslations;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;

    @Inject
    TranslateApi mTranslateApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        App.getComponent().inject(this);

        try {
            mTranslateApi.translate("111", "ru");
        } catch (TranslateException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    @OnClick(R.id.fab)
    public void onClick(View v) {
        Snackbar.make(v, "Oops!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
