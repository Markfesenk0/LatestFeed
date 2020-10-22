package com.example.latestfeed.IsraelPost;

import com.example.latestfeed.IsraelPost.Entities.MyPackage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostService {

    @FormUrlEncoded
    @Headers({"Cookie: __uzma=b2e58fc6-0bb6-4192-86cb-bb832753218e; __uzmb=1584384675; _ga=GA1.4.556378521.1584384679; __ssds=3; __ssuzjsr3=a9be0cd8e; __uzmaj3=0a5b4976-b2cc-4204-b2fa-6f9cd875273e; __uzmbj3=1584384680; __RequestVerificationToken=cW2tDqh_64axqd8kf2wjGIX32WoEuquP-l83DA2bQbcRRGehX8uFr20iwpxLIG9KkceO7FN0snk5IbBN4A8oLBKFPXhxlwqmePdwCa9LIKQ1; __uzmcj3=297292289892; __uzmdj3=1591697357; _gid=GA1.4.1497686368.1591697358; _gat_UA-88269527-1=1; __za_cds_19762770=%7B%22data_for_campaign%22%3A%7B%22country%22%3A%22IL%22%2C%22language%22%3A%22EN%22%2C%22ip%22%3A%2282.81.228.225%22%2C%22start_time%22%3A1591697357000%2C%22session_groups%22%3A%7B%221977%22%3A%7B%22campaign_Id%22%3A%2243220%22%7D%2C%221978%22%3A%7B%22campaign_Id%22%3A%2243303%22%7D%7D%7D%7D; __uzmc=9432910358135; __uzmd=1591697360; __za_cd_19762770=%7B%22visits%22%3A%22%5B1591697358%2C1589367675%2C1584384684%5D%22%2C%22campaigns_status%22%3A%7B%2243220%22%3A1591697366%2C%2245402%22%3A1589367683%2C%2245404%22%3A1585592328%7D%7D; __za_19762770=%7B%22sId%22%3A40322604%2C%22dbwId%22%3A%221%22%2C%22sCode%22%3A%22deceb5969033fe8db6358cba06d35dc9%22%2C%22sInt%22%3A5000%2C%22aLim%22%3A2000%2C%22asLim%22%3A100%2C%22na%22%3A1%2C%22td%22%3A1%2C%22ca%22%3A%221%22%7D", "Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36", "origin: https://mypost.israelpost.co.il", "referer: https://mypost.israelpost.co.il/%D7%9E%D7%A2%D7%A7%D7%91-%D7%9E%D7%A9%D7%9C%D7%95%D7%97%D7%99%D7%9D", "sec-fetch-dest: empty", "sec-fetch-mode: cors", "sec-fetch-site: same-origin", "x-requested-with: XMLHttpRequest"})
    @POST("/umbraco/Surface/ItemTrace/GetItemTrace")
    Call<MyPackage> getDeliveryDetails(@FieldMap Map<String, String> paramMap);
}
