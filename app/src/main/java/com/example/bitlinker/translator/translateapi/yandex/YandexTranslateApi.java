package com.example.bitlinker.translator.translateapi.yandex;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.ITranslateApi;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexApiKeyBannedException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexIOException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexQuotaExceededException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexTextTooLongException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexTranslateException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexTranslateFailedException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexUnsupportedLanguageException;
import com.example.bitlinker.translator.translateapi.yandex.exceptions.YandexWrongApiKeyException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class YandexTranslateApi implements ITranslateApi {
    private static final String API_KEY = "trnsl.1.1.20160820T104258Z.c741ab87aec05bf7.27231b3235e6f1769495219ae9ad484b57d2fca6";
    private static final String BASE_URL = "https://translate.yandex.net/";

    public static final int HTTP_CODE_OK = 200;
    public static final int HTTP_CODE_WRONG_API_KEY = 401;
    public static final int HTTP_CODE_API_KEY_BANNED = 402;
    public static final int HTTP_CODE_QUOTA_EXCEEDED = 404;
    public static final int HTTP_CODE_TEXT_TOO_LONG = 413;
    public static final int HTTP_CODE_TRANSLATE_FAILED = 422;
    public static final int HTTP_CODE_UNSUPPORTED_LANG = 501;

    private static class TranslateResponse {
        @SerializedName("code")
        @Expose
        Integer mCode;
        @SerializedName("lang")
        @Expose
        String mLang;
        @SerializedName("text")
        @Expose
        List<String> mText = new ArrayList<>();
    }

    private interface TranslateService {
        @POST("api/v1.5/tr.json/translate")
        @FormUrlEncoded
        Observable<Response<TranslateResponse>> translateText(@Field("text") String text,
                                                              @Query("lang") String lang);
    }

    private TranslateService mTranslationService;

    public YandexTranslateApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder().addQueryParameter("key", API_KEY).build();
                        request = request.newBuilder().url(url).build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        mTranslationService = retrofit.create(TranslateService.class);
    }

    @Override
    public Single<TranslatedText> translate(String text, String destLang) {
        Observable<Response<TranslateResponse>> translated = mTranslationService.translateText(text, destLang);
        return translated
                .onErrorResumeNext(throwable -> Observable.error(throwable))
                .map(value -> {
                    try {
                        return parseResponse(value);
                    } catch (YandexTranslateException e) {
                        throw OnErrorThrowable.from(e);
                    }
                })
                .toSingle();
    }

    private void checkCode(int code) throws YandexTranslateException {
        switch (code) {
            case HTTP_CODE_OK:
                return;
            case HTTP_CODE_WRONG_API_KEY:
                throw new YandexWrongApiKeyException();
            case HTTP_CODE_API_KEY_BANNED:
                throw new YandexApiKeyBannedException();
            case HTTP_CODE_QUOTA_EXCEEDED:
                throw new YandexQuotaExceededException();
            case HTTP_CODE_TEXT_TOO_LONG:
                throw new YandexTextTooLongException();
            case HTTP_CODE_TRANSLATE_FAILED:
                throw new YandexTranslateFailedException();
            case HTTP_CODE_UNSUPPORTED_LANG:
                throw new YandexUnsupportedLanguageException();
            default:
                throw new YandexTranslateException("Unknown error: " + code);
        }
    }

    private TranslatedText parseResponse(Response<TranslateResponse> response) throws YandexTranslateException {
        checkCode(response.code());
        TranslateResponse responseBody = response.body();
        if (responseBody != null) {
            checkCode(responseBody.mCode);
            List<String> textList = responseBody.mText;
            if (textList != null && textList.size() > 0) {
                String translatedText = responseBody.mText.get(0);
                String translatedLang = responseBody.mLang;
                return new TranslatedText("TODO", translatedText, translatedLang); // TODO
            } else {
                throw new YandexTranslateException("No text in response");
            }
        } else {
            throw new YandexTranslateException("Response body is null");
        }
    }

}