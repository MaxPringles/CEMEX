package com.telstock.tmanager.cemex.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USRMICRO10 on 10/08/2016.
 */
public class ApiClient {

    public static Retrofit retrofit = null;

    public static Retrofit getClient(final Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                SharedPreferences shared = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("_appVersion", Url.VERSION_WS + "")
                        .header("_idPlataforma", Url.ID_PLATAFORMA + "")
                        .header("Authorization", "bearer " + shared.getString(Valores.SHARED_PREFERENCES_TOKEN, ""));

                Request request = requestBuilder.build();

                // try the request
                Response response = chain.proceed(request);

                int tryCount = 0;

                while (!response.isSuccessful() && tryCount < 3) {

                    Log.d("intercept", "Request is not successful - " + tryCount + " " + response.request().url());

                    tryCount++;

                    // retry the request
                    response = chain.proceed(request);
                }

                // otherwise just pass the original response on
                return response;
            }
        });

        OkHttpClient client = httpClient.build();

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url.URL_WEBSERVICE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }



}
