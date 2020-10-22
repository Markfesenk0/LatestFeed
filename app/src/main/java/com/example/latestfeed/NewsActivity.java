package com.example.latestfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latestfeed.Entities.News;
import com.example.latestfeed.Parsers.AppParser;
import com.example.latestfeed.Parsers.NewsInsideParser;
import com.example.latestfeed.Parsers.NewsParser;
import com.example.latestfeed.Parsers.SongParser;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsActivity extends AppCompatActivity {

    ImageView articleImage;
    TextView articleCreator;
    TextView articlePublishDate;
    TextView articleTitle;
    TextView articleSummary;
    TextView articleBody;
    News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        articleImage = findViewById(R.id.article_image);
        articleCreator = findViewById(R.id.article_creator);
        articlePublishDate = findViewById(R.id.article_publish_date);
        articleTitle = findViewById(R.id.article_title);
        articleSummary = findViewById(R.id.article_summary);
        articleBody = findViewById(R.id.article_body);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        news = (News) bundle.getSerializable("news");
        if (news != null) {
            try {
                Picasso.get().load(news.getImgUrl()).into(articleImage);
            } catch (IllegalArgumentException e) {
                Picasso.get().load(R.drawable.placeholder).into(articleImage);
            }
            if (news.getCreator().length() != 7) {
                articleCreator.setText(news.getCreator());
            } else {
                articleCreator.setText("");
            }
            System.out.println(news.getCreator().length());
            articlePublishDate.setText(news.getPublishDate());
            articleTitle.setText(news.getTitle());
            articleSummary.setText(news.getSnippet());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openLink(View view) {
        try {
            Uri url =Uri.parse(news.getNewsUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(url);
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(this, "Can't open URL.\nTry again later)", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}