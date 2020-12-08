package cn.wappt.m.apptv.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.base.Columnbase;
import cn.wappt.m.apptv.interfaces.ColumnInterfaces;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author: wsq
 * @date: 2020/10/27
 * Description:
 */
public  class ColimnListManager   {

    private static ColimnListManager colimnListManager;

    private ColimnListManager(){
    }

    //调用生成的对象
    public static ColimnListManager getInstance(){//方法无需同步，各线程同时访问
        if (colimnListManager==null){
            synchronized (ColimnListManager.class){
                if (colimnListManager==null){
                    colimnListManager=new ColimnListManager();
                }
            }
        }
        return colimnListManager;
    }
    //// 使用接口获取排行榜信息
    public List<Columnbase> Leaderboard(Context context, int amounts, int pages, int sortnames ) {
        //进行实例化数据
        List<Columnbase > columnbases=new ArrayList<>();
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        ColumnInterfaces request = retrofit.create(ColumnInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.bdxssort(amounts, pages, sortnames);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Columnbase columnbase=new Columnbase();
                        columnbase.setVod_pic(jsonObject.get("vod_pic").toString().replace("mac", "https"));
                        columnbase.setVod_blurb((String) jsonObject.get("vod_blurb"));
                        columnbase.setVod_name((String) jsonObject.get("vod_name"));
                        columnbase.setVod_score((String) jsonObject.get("vod_score"));
                        columnbase.setVod_id((String) jsonObject.get("vod_id"));
                        columnbase.setVod_class((String) jsonObject.get("vod_class"));
                        columnbases.add(columnbase);
                        System.out.println(columnbase);
                    }
                //存储排行榜信息

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    return  columnbases;
    }



}
