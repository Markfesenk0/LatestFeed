package com.example.latestfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.latestfeed.Entities.App;
import com.example.latestfeed.Entities.News;
import com.squareup.picasso.Picasso;

public class AppActivity extends AppCompatActivity {

    ImageView applicationImage;
    TextView applicationCreator;
    TextView applicationTitle;
    TextView applicationSummary;
    TextView applicationPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        applicationImage = findViewById(R.id.application_image);
        applicationCreator = findViewById(R.id.application_creator);
        applicationPrice = findViewById(R.id.application_price);
        applicationTitle = findViewById(R.id.application_title);
        applicationSummary = findViewById(R.id.application_summary);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        App app = (App) bundle.getSerializable("app");
        if (app != null) {
            Picasso.get().load(app.getImgUrl()).into(applicationImage);
            applicationCreator.setText(app.getArtist());
            applicationPrice.setText(app.getPrice());
            applicationTitle.setText(app.getTitle());
            applicationSummary.setText(app.getSummary());
        }
    }
}