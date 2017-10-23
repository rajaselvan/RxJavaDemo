package com.chrisarriola.githubrxjava;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;


public class NewsApiClient {

    private static final String NEW_API_URL = "https://newsapi.org/v1/";
    private static NewsApiClient instance;
    private NewsApiService newsApiService;

    private NewsApiClient() {
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(NEW_API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApiService = retrofit.create(NewsApiService.class);
    }

    public static NewsApiClient getInstance() {
        if (instance == null) {
            instance = new NewsApiClient();
        }
        return instance;
    }

    public Observable<ApiResponse> getApiResponse(@NonNull String source, @NonNull String apiKey) {
        return newsApiService.fetchNewsArticles(source, apiKey);
    }
}
