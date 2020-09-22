package com.example.latestfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.latestfeed.Adapters.AppAdapter;
import com.example.latestfeed.Adapters.DummyAdapter;
import com.example.latestfeed.Adapters.DummyNewsAdapter;
import com.example.latestfeed.Adapters.NewsAdapter;
import com.example.latestfeed.Adapters.SongAdapter;
import com.example.latestfeed.Entities.App;
import com.example.latestfeed.Entities.News;
import com.example.latestfeed.Entities.Song;
import com.example.latestfeed.Parsers.AppParser;
import com.example.latestfeed.Parsers.NewsParser;
import com.example.latestfeed.Parsers.SongParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnNewsListener, AppAdapter.OnAppListener {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    boolean isDark;
    int currentNightMode;
    static boolean firstTimeNews;
    static boolean firstTimeSongs;
    static boolean firstTimeFreeApps;
    static boolean firstTimePaidApps;
    CustomSwipeToRefresh customSwipeToRefresh;
    static RecyclerView topSongsRecycler;
    static RecyclerView topFreeAppsRecycler;
    static RecyclerView topPaidAppsRecycler;
    static RecyclerView latestNewsRecycler;
    static ArrayList<Song> songs;
    static ArrayList<App> freeApps;
    static ArrayList<App> paidApps;
    static ArrayList<News> latestNews;
    static AppAdapter freeAppsAdapter;
    static AppAdapter paidAppsAdapter;
    static SongAdapter songAdapter;
    static NewsAdapter newsAdapter;

    static DummyNewsAdapter dummyNewsAdapter;
    static DummyAdapter dummyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Configuration configuration = getResources().getConfiguration();
        currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        getData(currentNightMode);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customSwipeToRefresh = findViewById(R.id.refresh_layout);
        customSwipeToRefresh.setOnRefreshListener(new CustomSwipeToRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                featuredRecycler();
                customSwipeToRefresh.setRefreshing(false);
            }
        });
        initialize();
        featuredRecycler();
//        setAdapters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        MenuItem menuItem;
        if (isDark) {
            menuItem = menu.findItem(R.id.menu_dark);
        } else {
            menuItem = menu.findItem(R.id.menu_light);
        }
        menuItem.setChecked(true);
        return true;
    }

    private void getData(int currentNightMode) {
        boolean check = false;
        if (currentNightMode == 1) {
            check = true;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isDark = sharedPreferences.getBoolean("isDark", check);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        editor.clear();
        switch(id) {
            case R.id.menu_dark:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isDark", true);
                break;

            case R.id.menu_light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isDark", false);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        editor.apply();
        return true;
    }

    private void initialize() {
        topSongsRecycler = findViewById(R.id.top_songs_recycler);
        topFreeAppsRecycler = findViewById(R.id.top_free_apps_recycler);
        topPaidAppsRecycler = findViewById(R.id.top_paid_apps_recycler);
        latestNewsRecycler = findViewById(R.id.latest_news_recycler);
        songs = new ArrayList<>();
        freeApps = new ArrayList<>();
        paidApps = new ArrayList<>();
        latestNews = new ArrayList<>();
        songAdapter = new SongAdapter(songs);
        freeAppsAdapter = new AppAdapter(freeApps, this);
        paidAppsAdapter = new AppAdapter(paidApps, this);
        newsAdapter = new NewsAdapter(latestNews, this);
        topSongsRecycler.setHasFixedSize(true);
        topSongsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topFreeAppsRecycler.setHasFixedSize(true);
        topFreeAppsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topPaidAppsRecycler.setHasFixedSize(true);
        topPaidAppsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        latestNewsRecycler.setHasFixedSize(true);
        latestNewsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dummyNewsAdapter = new DummyNewsAdapter();
        dummyAdapter = new DummyAdapter();
        latestNewsRecycler.setAdapter(dummyNewsAdapter);
        topSongsRecycler.setAdapter(dummyAdapter);
        topFreeAppsRecycler.setAdapter(dummyAdapter);
        topPaidAppsRecycler.setAdapter(dummyAdapter);
        firstTimeNews = true;
        firstTimeSongs = true;
        firstTimeFreeApps = true;
        firstTimePaidApps = true;
    }

    private void setAdapters() {
        latestNewsRecycler.setAdapter(newsAdapter);
        topSongsRecycler.setAdapter(songAdapter);
        topFreeAppsRecycler.setAdapter(freeAppsAdapter);
        topPaidAppsRecycler.setAdapter(paidAppsAdapter);
    }

    private void featuredRecycler() {
            downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=100/xml", 1);
            downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=100/xml", 2);
            downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=100/xml", 3);
            downloadUrl("https://rcs.mako.co.il/rss/news-israel.xml", 4);

    }

    private void downloadUrl(String feedUrl, int type) {
        DownloadData downloadData = new DownloadData(type);
        downloadData.execute(feedUrl);
    }

    private static class DownloadData extends AsyncTask<String, Void, String> {
        private int type;
        private static final String TAG = "DownloadData";

        public DownloadData(int type) {
            this.type = type;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AppParser appParser;
            switch(type) {
                case 1:
                    SongParser songParser = new SongParser();
                    songParser.parse(s);
                    songs = SongParser.getSongs();
                    if(firstTimeSongs) {
                        topSongsRecycler.setAdapter(songAdapter);
                        firstTimeSongs = false;
                    }
                    songAdapter.updateData(songs);
                    break;

                case 2:
                    appParser = new AppParser();
                    appParser.parse(s);
                    freeApps = AppParser.getApps();
                    if(firstTimeFreeApps) {
                        topFreeAppsRecycler.setAdapter(freeAppsAdapter);
                        firstTimeFreeApps = false;
                    }
                    freeAppsAdapter.updateData(freeApps);
                    break;

                case 3:
                    appParser = new AppParser();
                    appParser.parse(s);
                    paidApps = AppParser.getApps();
                    if(firstTimePaidApps) {
                        topPaidAppsRecycler.setAdapter(paidAppsAdapter);
                        firstTimePaidApps = false;
                    }
                    paidAppsAdapter.updateData(paidApps);
                    break;

                case 4:
                    NewsParser newsParser = new NewsParser();
                    newsParser.parse(s);
                    latestNews = newsParser.getNews();
                    if(firstTimeNews) {
                        latestNewsRecycler.setAdapter(newsAdapter);
                        firstTimeNews = false;
                    }
                    newsAdapter.updateData(latestNews);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true) {
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0) {
                        break;
                    }
                    if(charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e(TAG, "downloadXML: IO Exception reading file " + e.getMessage());
            } catch (SecurityException e) {
                //e.printStackTrace();
                Log.e(TAG, "downloadXML: Security Exception, needs permission? " + e.getMessage());
            }
            return null;
        }
    }

    @Override
    public void onNewsClick(int position) {
        Intent intent = new Intent(this, NewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("news", latestNews.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAppClick(int position, String title) {
        Intent intent = new Intent(this, AppActivity.class);
        Bundle bundle = new Bundle();
        if (freeApps.get(position).getTitle().equals(title)) {
            bundle.putSerializable("app", freeApps.get(position));
        } else {
            bundle.putSerializable("app", paidApps.get(position));
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}