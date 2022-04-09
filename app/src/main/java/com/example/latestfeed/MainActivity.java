package com.example.latestfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latestfeed.Adapters.AppAdapter;
import com.example.latestfeed.Adapters.DummyAdapter;
import com.example.latestfeed.Adapters.NewsAdapter;
import com.example.latestfeed.Adapters.ParcelAdapter;
import com.example.latestfeed.Adapters.SongAdapter;
import com.example.latestfeed.Entities.App;
import com.example.latestfeed.Entities.News;
import com.example.latestfeed.Entities.Song;
import com.example.latestfeed.Entities.TypeEnum;
import com.example.latestfeed.IHerb.AddIHerbItemDialog;
import com.example.latestfeed.IHerb.IHerbItem;
import com.example.latestfeed.IHerb.IHerbItemAdapter;
import com.example.latestfeed.IHerb.IHerbItemFetcher;
import com.example.latestfeed.IHerb.JVVM.IHerbViewModel;
import com.example.latestfeed.IsraelPost.Entities.MyPackage;
import com.example.latestfeed.IsraelPost.Entities.Parcel;
import com.example.latestfeed.IsraelPost.JVVM.ParcelViewModel;
import com.example.latestfeed.IsraelPost.PostService;
import com.example.latestfeed.Parsers.AppParser;
import com.example.latestfeed.Parsers.NewsParser;
import com.example.latestfeed.Parsers.SongParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnNewsListener, AppAdapter.OnAppListener, SongAdapter.OnSongListener, ParcelAdapter.OnParcelListener, AddPackageDialog.PackageDialogListener, AddIHerbItemDialog.IHerbItemDialogListener, IHerbItemAdapter.OnIHerbItemListener {

    static String sequence = "";
    static PostService postsService;
    static ArrayList<Parcel> parcels;
    static ParcelAdapter parcelAdapter;
    static RecyclerView parcelRecycler;
    static ParcelViewModel parcelViewModel;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    boolean isDark;
    int currentNightMode;
    static boolean firstTimeNews;
    static boolean firstTimeSongs;
    static boolean firstTimeFreeApps;
    static boolean firstTimePaidApps;
    static CustomSwipeToRefresh customSwipeToRefresh;
    static RecyclerView topSongsRecycler;
    static RecyclerView topFreeAppsRecycler;
    static RecyclerView topPaidAppsRecycler;
    static RecyclerView latestNewsRecycler;
    static ArrayList<Song> songs;
    static ArrayList<App> freeApps;
    static ArrayList<App> paidApps;
    static ArrayList<News> latestNews;
    static String currentNewsURL = "https://rcs.mako.co.il/rss/MainSliderRss.xml";
    static AppAdapter freeAppsAdapter;
    static AppAdapter paidAppsAdapter;
    static SongAdapter songAdapter;
    static NewsAdapter newsAdapter;
    static DummyAdapter dummyNewsAdapter;
    static DummyAdapter dummyAdapter;

    public static ArrayList<IHerbItem> iHerbItems;
    public static IHerbItemAdapter iHerbItemsAdapter;
    public static RecyclerView iHerbItemsRecycler;
    public static IHerbViewModel iHerbItemsViewModel;

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
                featuredRecycler(currentNewsURL);
                for(Parcel parcel : parcels) {
                    if(parcel.getStatus() < 2) {
                        fetchPackage(parcel.getTrackingNumber(), parcel.getTitle(), parcel.getColor());
                    }
                }
                for(IHerbItem iHerbItem: iHerbItems) {
//                    fetchIherbItem(iHerbItem);
                    fetchIherbItem(String.valueOf(iHerbItem.getItemId()));
                }
            }
        });
        initialize();
        featuredRecycler(currentNewsURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        MenuItem menuItem;
        if (isDark) {
            menuItem = menu.findItem(R.id.menu_dark);
            menuItem.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        editor.clear();
        switch(id) {
            case R.id.menu_dark:
                if(isDark) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDark", false);
                    isDark = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDark", true);
                    isDark = true;
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        editor.apply();
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

    private void initialize() {
        topSongsRecycler = findViewById(R.id.top_songs_recycler);
        topFreeAppsRecycler = findViewById(R.id.top_free_apps_recycler);
        topPaidAppsRecycler = findViewById(R.id.top_paid_apps_recycler);
        latestNewsRecycler = findViewById(R.id.latest_news_recycler);
        songs = new ArrayList<>();
        freeApps = new ArrayList<>();
        paidApps = new ArrayList<>();
        latestNews = new ArrayList<>();
        songAdapter = new SongAdapter(songs, this);
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
        dummyNewsAdapter = new DummyAdapter(TypeEnum.NEWS);
        dummyAdapter = new DummyAdapter(TypeEnum.SONG);
        latestNewsRecycler.setAdapter(dummyNewsAdapter);
        topSongsRecycler.setAdapter(dummyAdapter);
        topFreeAppsRecycler.setAdapter(dummyAdapter);
        topPaidAppsRecycler.setAdapter(dummyAdapter);
        firstTimeNews = true;
        firstTimeSongs = true;
        firstTimeFreeApps = true;
        firstTimePaidApps = true;

        parcels = new ArrayList<>();
        parcelRecycler = findViewById(R.id.packages_recycler);
        parcelRecycler.setHasFixedSize(true);
        parcelRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        parcelAdapter = new ParcelAdapter(parcels, this, isDark);
        parcelRecycler.setAdapter(parcelAdapter);
        parcelViewModel = ViewModelProviders.of(this).get(ParcelViewModel.class);
        parcelViewModel.getAllPackages().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> myParcels) {
                parcels = (ArrayList<Parcel>) myParcels;
                parcelAdapter.updateData(parcels);
            }
        });
        parcelRecycler.smoothScrollToPosition(0);
        iHerbItems = new ArrayList<>();
        iHerbItemsRecycler = findViewById(R.id.iherb_recycler);
        iHerbItemsRecycler.setHasFixedSize(true);
        iHerbItemsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        iHerbItemsAdapter = new IHerbItemAdapter(iHerbItems, this, isDark);
        iHerbItemsRecycler.setAdapter(iHerbItemsAdapter);
        iHerbItemsViewModel = ViewModelProviders.of(this).get(IHerbViewModel.class);
        iHerbItemsViewModel.getAllIHerbItems().observe(this, new Observer<List<IHerbItem>>() {
            @Override
            public void onChanged(List<IHerbItem> myIHerbItems) {
                iHerbItems = (ArrayList<IHerbItem>) myIHerbItems;
                iHerbItems.sort(new Comparator<IHerbItem>() {
                    public int compare(IHerbItem c1, IHerbItem c2) {
                        if (c1.getDiscount() > c2.getDiscount()) return -1;
                        if (c1.getDiscount() < c2.getDiscount()) return 1;
                        return 0;
                    }
                });
                for(int i = 0; i < iHerbItems.size(); i++) {
                    if(!iHerbItems.get(i).getStock()) {
                        iHerbItems.add(iHerbItems.size() - 1, iHerbItems.remove(i));
                    }
                }

                iHerbItemsAdapter.updateData(iHerbItems);
            }
        });
        iHerbItemsRecycler.smoothScrollToPosition(0);

        Picasso picasso = new Picasso.Builder(this)
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    private void featuredRecycler(String url) {
            downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=100/xml", 1);
            downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=100/xml", 2);
            downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=100/xml", 3);
            downloadUrl(url, 4);
    }

    private void downloadUrl(String feedUrl, int type) {
        DownloadData downloadData = new DownloadData(type);
        downloadData.execute(feedUrl);
    }

    @Override
    public void applyInput(String name, String number, int color) {
        fetchPackage(number, name, color);
    }

    @Override
    public void applyInputIHerbItem(String url) {
        IHerbItemFetcher iHerbItemFetcher = new IHerbItemFetcher(false);
        iHerbItemFetcher.execute(url);
//        iHerbItemParser = new IHerbParser(false);
//        iHerbItemParser.execute(url);
    }

    public void fetchIherbItem(String itemId) {
        IHerbItemFetcher iHerbItemFetcher = new IHerbItemFetcher(true);
        iHerbItemFetcher.execute(itemId);
    }

    @Override
    public void onIHerbItemClick(int position) {
        if (iHerbItems.size() == position) {
            AddIHerbItemDialog addIHerbItemDialog = new AddIHerbItemDialog();
            addIHerbItemDialog.show(getSupportFragmentManager(), "tag");
        } else {
            try {
                Uri url = Uri.parse(iHerbItems.get(position).getUrlIL());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(url);
                startActivity(intent);
            } catch (ActivityNotFoundException ex){
                Log.e("IHerb url", ex.getMessage());
                Toast.makeText(this, "Can't open URL.\nTry again later)", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onIHerbItemLongClick(int position) {
        if (iHerbItems.size() != position) {
            iHerbItemsViewModel.delete(iHerbItems.get(position));
            iHerbItems.remove(position);
            iHerbItemsAdapter.updateData(iHerbItems);
        }
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
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
                    customSwipeToRefresh.setRefreshing(false);
                    break;
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
    public void onSongClick(int position) {
        PreviewMediaPlayer previewMediaPlayer = new PreviewMediaPlayer();
        Bundle args = new Bundle();
        args.putSerializable("song", songs.get(position));
        previewMediaPlayer.setArguments(args);
        previewMediaPlayer.show(getSupportFragmentManager(), "test");
    }

    @Override
    public void onParcelClick(int position) {
        if (parcels.size() == position) {
            AddPackageDialog addPackageDialog = new AddPackageDialog();
            addPackageDialog.show(getSupportFragmentManager(), "tag");
        } else {
            ViewPackageDialog addPackageDialog = new ViewPackageDialog();
            Bundle args = new Bundle();
            args.putSerializable("package", parcels.get(position));
            addPackageDialog.setArguments(args);
            addPackageDialog.show(getSupportFragmentManager(), "test");
        }
    }

    @Override
    public void onParcelLongClick(int position) {
        if (parcels.size() != position) {
            parcelViewModel.delete(parcels.get(position));
            parcels.remove(position);
            parcelAdapter.updateData(parcels);
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

    public void onNewsTopicClick(View view) {
        String topic = view.getTag().toString();
        String url = "https://rcs.mako.co.il/rss/MainSliderRss.xml";
        if (topic.equalsIgnoreCase("israel")) {
            url = "https://rcs.mako.co.il/rss/news-israel.xml";
            func(1);
        } else if (topic.equalsIgnoreCase("global")) {
            url = "https://rcs.mako.co.il/rss/news-world.xml";
            func(2);
        } else if (topic.equalsIgnoreCase("finance")) {
            url = "https://rcs.mako.co.il/rss/news-money.xml";
            func(3);
        } else if (topic.equalsIgnoreCase("food")) {
            url = "https://rcs.mako.co.il/rss/food-recipes.xml";
            func(5);
        } else if (topic.equalsIgnoreCase("criminal")) {
            url = "https://rcs.mako.co.il/rss/news-law.xml";
            func(4);
        }
        if (sequence.equalsIgnoreCase("15243")) {
            Toast.makeText(this, "I fucking love Shahar", Toast.LENGTH_SHORT).show();
        }
        featuredRecycler(url);
        currentNewsURL = url;
        TextView title = findViewById(R.id.title_news);
        title.setText(topic + " news");
    }

    public void func(int i) {
        if (sequence.length() == 5) {
            sequence = sequence.substring(1);
        }
        sequence = sequence + String.valueOf(i);
    }

    void fetchPackage(final String number, final String title, final int color) {
        getPostOfficeClientApiService();
        Call<MyPackage> call = postsService.getDeliveryDetails(getDeliveryBody(number));
        call.enqueue(new Callback<MyPackage>() {
            @Override
            public void onResponse(Call<MyPackage> call, Response<MyPackage> response) {
                if(response.body() == null) {
                    Log.e("FAILED", "onResponse: response code " + response.code() + "  ");
                    Toast.makeText(MainActivity.this, "Error. Try again later", Toast.LENGTH_SHORT).show();
                } else {
                    MyPackage myPackage = response.body();
                    Parcel parcel = new Parcel(number, title, color, myPackage.getResult().getItemcodeinfo().getInfoLines());
                    if (parcel.getLastStatus().equalsIgnoreCase("error")) {
                        Toast.makeText(MainActivity.this, "Malformed number. Try again", Toast.LENGTH_SHORT).show();
                    } else {
                        if (parcels.contains(parcel)) {
                            parcels.remove(parcels.indexOf(parcel));
                            parcels.add(parcel);
                            parcelViewModel.update(parcel);
                            parcelAdapter.updateData(parcels);
                        } else {
                            parcels.add(parcel);
                            parcelViewModel.insert(parcel);
                            parcelAdapter.updateData(parcels);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPackage> call, Throwable t) {
                Log.e("POST: onFailure: ", t.getMessage().toString());
                Toast.makeText(MainActivity.this, "No response:\n" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private static Map<String, String> getDeliveryBody(String trackingNumber) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("itemCode", trackingNumber);
        localHashMap.put("lcid", "1033"); //1033 - Hebrew, 1037 - English
        localHashMap.put("__RequestVerificationToken", "IFj9bHi37NeXmMTgov3QlHXFxG7p3pHDTWQa8PEAZ8Nca9Md1v4ksUlpPVmBIYlGrhi9Xm3t4Ap9jfZKNGPPMhpV2VGbxw2JoalUI1KuuUY1");
        return localHashMap;
    }

    private void getPostOfficeClientApiService() {
        if (postsService == null) {
            postsService = getApiService(60L);
        }
    }

    public PostService getApiService(long paramLong) {
        OkHttpClient localOkHttpClient = new OkHttpClient.Builder().readTimeout(paramLong, TimeUnit.SECONDS).writeTimeout(paramLong, TimeUnit.SECONDS).connectTimeout(paramLong, TimeUnit.SECONDS).addInterceptor(new Interceptor()
        {
            @NotNull
            public okhttp3.Response intercept(@NotNull Interceptor.Chain paramAnonymousChain)
                    throws IOException
            {
                Request localRequest = paramAnonymousChain.request();
                return paramAnonymousChain.proceed(localRequest.newBuilder().method(localRequest.method(), localRequest.body()).build());
            }
        }).build();
        return (PostService)new Retrofit.Builder().client(localOkHttpClient).baseUrl("https://mypost.israelpost.co.il").addConverterFactory(GsonConverterFactory.create()).build().create(PostService.class);
    }

}