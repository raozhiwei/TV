package cn.wappt.m.apptv.utils;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description:
 */
public class RetrofitManager {
     private  static  final  RetrofitManager ourinstance=new RetrofitManager();
     private  final Retrofit mRetrofit;


    public static RetrofitManager getInstance() {
        return ourinstance;
    }

    private RetrofitManager() {
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(3, TimeUnit.SECONDS).
                readTimeout(3, TimeUnit.SECONDS).
                writeTimeout(3, TimeUnit.SECONDS).build();
        //创建retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.AppUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
