package com.chrisarriola.githubrxjava;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import static com.chrisarriola.githubrxjava.NewsApiService.API_KEY;

public class MainActivity extends AppCompatActivity {

    private String className = MainActivity.class.getName();
    private long startTime, endTime;
    private List<Article> feedItems;
    private NewsFeedAdapter adapter;
    private List<String> sources = Arrays.asList("techcrunch", "bbc-sport", "espn-cric-info", "abc-news-au", "al-jazeera-english",
            "ars-technica", "associated-press", "bbc-news", "bild",
            "cnn", "engadget", "financial-times", "google-news", "mashable");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.list_view_repos);
        adapter = new NewsFeedAdapter();
        listView.setAdapter(adapter);
        if (savedInstanceState == null) {
            getNewsFeed(sources, API_KEY);
        } else {
            this.feedItems = savedInstanceState.<Article>getParcelableArrayList(className);
            adapter.setArticles(feedItems);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(className, (ArrayList<? extends Parcelable>) feedItems);
        super.onSaveInstanceState(outState);
    }


    private void getNewsFeed(List<String> sources, String apiKey) {
        startTime = System.currentTimeMillis();
        NewsApiClient newsApiClient = NewsApiClient.getInstance();
        List<Observable<ApiResponse>> callerList = new ArrayList<Observable<ApiResponse>>();

        for (String source : sources) {
            callerList.add(newsApiClient.getApiResponse(source, API_KEY).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()));
        }

        Observable.zip(
                callerList,
                new FuncN<List<Article>>() {

                    @Override
                    public List<Article> call(Object... args) {
                        List<Article> totalList = new ArrayList<Article>();
                        for (Object o : args) {
                            totalList.addAll(((ApiResponse) o).getArticles());
                        }
                        return totalList;
                    }
                })
                .subscribe(new Observer<List<Article>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        feedItems = articles;
                        endTime = System.currentTimeMillis();
                        adapter.setArticles(articles);
                        Toast.makeText(getApplicationContext(), String.format("The time taken %1$s seconds", (endTime - startTime) / 1000.0), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
