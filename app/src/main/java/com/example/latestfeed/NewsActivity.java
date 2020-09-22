package com.example.latestfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latestfeed.Entities.News;
import com.squareup.picasso.Picasso;

public class NewsActivity extends AppCompatActivity {

    ImageView articleImage;
    TextView articleCreator;
    TextView articlePublishDate;
    TextView articleTitle;
    TextView articleSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        articleImage = findViewById(R.id.article_image);
        articleCreator = findViewById(R.id.article_creator);
        articlePublishDate = findViewById(R.id.article_publish_date);
        articleTitle = findViewById(R.id.article_title);
        articleSummary = findViewById(R.id.article_summary);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        News news = (News) bundle.getSerializable("news");
        if (news != null) {
            Picasso.get().load(news.getImgUrl()).into(articleImage);
            articleCreator.setText(news.getCreator());
            articlePublishDate.setText(news.getPublishDate());
            articleTitle.setText(news.getTitle());
            articleSummary.setText(news.getBody());
        }
    }
}