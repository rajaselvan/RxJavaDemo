package com.chrisarriola.githubrxjava;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface NewsApiService {

    String API_KEY = "b3d117f722194ee0aa5af45a4198790c";
    @GET("articles")
    Observable<ApiResponse> fetchNewsArticles(@Query("source") String source,
                                              @Query("apiKey") String apiKey);
}
