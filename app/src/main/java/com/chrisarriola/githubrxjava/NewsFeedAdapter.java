package com.chrisarriola.githubrxjava;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class NewsFeedAdapter extends BaseAdapter {

    private List<Article> articleList = new ArrayList<>();

    @Override public int getCount() {
        return articleList.size();
    }

    @Override public Article getItem(int position) {
        if (position < 0 || position >= articleList.size()) {
            return null;
        } else {
            return articleList.get(position);
        }
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null ? convertView : createView(parent));
        final ArticleViewHolder viewHolder = (ArticleViewHolder) view.getTag();
        viewHolder.setArticle(getItem(position));
        return view;
    }

    public void setArticles(@Nullable List<Article> repos) {
        if (repos == null) {
            return;
        }
        articleList.clear();
        articleList.addAll(repos);
        notifyDataSetChanged();
    }

    private View createView(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.article_item, parent, false);
        final ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    public void add(Article article) {
        articleList.add(article);
        notifyDataSetChanged();
    }

    private static class ArticleViewHolder {

        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvAuthor;
        private TextView tvDate;

        public ArticleViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.text_article_title);
            tvDescription = (TextView) view.findViewById(R.id.text_article_description);
            tvAuthor = (TextView) view.findViewById(R.id.text_article_author);
            tvDate = (TextView) view.findViewById(R.id.text_article_date);
        }

        public void setArticle(Article article)  {
            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            tvAuthor.setText(article.getAuthor());
            tvDate.setText(article.getPublishedAt());
        }
    }
}
