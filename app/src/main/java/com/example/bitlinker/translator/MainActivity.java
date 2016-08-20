package com.example.bitlinker.translator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.TranslateException;
import com.example.bitlinker.translator.translateapi.yandex.YandexTranslateApi;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexTranslateException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private YandexTranslateApi mTranslateApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTranslateApi = new YandexTranslateApi();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslatedText text;
                try {
                     text = mTranslateApi.translate("Hello", "ru");
                } catch (TranslateException e) {
                    e.printStackTrace();
                    return;
                }
                Snackbar.make(view, text.getTranslatedText(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
}
