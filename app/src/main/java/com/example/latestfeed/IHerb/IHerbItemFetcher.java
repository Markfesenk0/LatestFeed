package com.example.latestfeed.IHerb;

import android.os.AsyncTask;
import android.util.Log;

import com.example.latestfeed.MainActivity;
import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.IntStream;

public class IHerbItemFetcher extends AsyncTask<String, Void, String> {
    private IHerbItem iHerbItem;
    private boolean isRefresh;
    private String urlUS;
    private String urlIL;

    public IHerbItemFetcher(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String productId = strings[0].substring(strings[0].lastIndexOf('/') + 1);
        iHerbItem = new IHerbItem(url);
        String baseUrl = "https://catalog.app.iherb.com/product/";
        String ending = "?currCode=ILS&countryCode=IL&lc=en-US";
        String baseImgUrl = "https://iherb-ds.com/rec/freqpurchasedtogether?currCode=ILS&countryCode=IL&lc=en-US&pid=";
        int id = Integer.valueOf(productId);

        try {
            JSONObject jsonObjectBase = readJsonFromUrl(baseUrl + productId + ending);
            JSONObject jsonObjectImage = readJsonFromUrl(baseImgUrl + productId);
            JSONObject origin = jsonObjectImage.getJSONObject("Origin");
            iHerbItem.setItemId(id);
            iHerbItem.setTitle(origin.getString("Name"));
            iHerbItem.setBrand(jsonObjectBase.getString("brandName"));
            iHerbItem.setStandardPrice(Double.parseDouble(jsonObjectBase.getString("listPrice").substring(1)));
            iHerbItem.setCurrentPrice(Double.parseDouble(jsonObjectBase.getString("discountPrice").substring(1)));
            iHerbItem.setStock(origin.getBoolean("InStock") && jsonObjectBase.getBoolean("isAvailableToPurchase"));
            iHerbItem.setImgUrl(origin.getString("ProdImageRetina"));
            iHerbItem.setCurrency(origin.getString("CurrencySymbol"));
            if (iHerbItem.getStandardPrice() != 0) {
                iHerbItem.setDiscount(100 - (int) Math.round((iHerbItem.getCurrentPrice() / iHerbItem.getStandardPrice()) * 100));
            } else {
                iHerbItem.setDiscount(0);
            }
            Double minPrice = iHerbItem.getCurrentPrice();
            Double maxPrice = iHerbItem.getStandardPrice();
            iHerbItem.setMinPrice(minPrice);
            iHerbItem.setMaxPrice(maxPrice);
            if (isRefresh) {
                int index = IntStream.range(0, MainActivity.iHerbItems.size())
                        .filter(i -> MainActivity.iHerbItems.get(i).getItemId() == (id))
                        .findFirst()
                        .orElse(-1);
                if (index != -1) {
                    IHerbItem oldHerbItem = MainActivity.iHerbItems.get(index);
                    if (oldHerbItem.getMinPrice() > minPrice) {
                        iHerbItem.setMinPrice(minPrice);
                    } else {
                        iHerbItem.setMinPrice(oldHerbItem.getMinPrice());
                    }
                    if (oldHerbItem.getMinPrice() < maxPrice) {
                        iHerbItem.setMaxPrice(maxPrice);
                    } else {
                        iHerbItem.setMaxPrice(oldHerbItem.getMinPrice());
                    }
                    if (iHerbItem.equals(oldHerbItem)) {
                        return null;
                    }
                }
                MainActivity.iHerbItems.remove(iHerbItem);
            }
            MainActivity.iHerbItems.add(iHerbItem);
            MainActivity.iHerbItemsViewModel.insert(iHerbItem);
            MainActivity.iHerbItemsAdapter.updateData(MainActivity.iHerbItems);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
//        String newUrl = baseUrl + productId;
//        HttpURLConnection connection = null;
//        BufferedReader reader = null;
//        try {
//            URL url = new URL(newUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//
//            if(connection.getResponseCode() == 200) {
//                InputStream stream = connection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuffer buffer = new StringBuffer();
//                String line = "";
//
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line+"\n");
//                    Log.d("Response: ", "> " + line);
//                }
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
