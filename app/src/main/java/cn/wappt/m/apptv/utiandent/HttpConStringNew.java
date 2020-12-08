package cn.wappt.m.apptv.utiandent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.utils.Constants;

/**
 * @author 纵游四方悠自得
 * @create 2020/10/19--14:01
 * @effect   搜索框使用
 */
public class HttpConStringNew {


    //获取接口地址
    private String httpurl = Constants.dataAnalysis;;
    String strRead =null;  //转码
    public int datamode = 0;

    //定义数组进行存储
    public String[][] videourl;    //视频地址
    public String[] imageurl;      //图片地址
    public String[] titleurl;      //标题
    public String[] playcount;     //播放次数
    public String[] idurl;         //视频id
    public String[] scoreurl;      //视频评分
    public String[] remarksurl;    //视频状态
    public List<String[][]> list = new ArrayList<>();
    //临时变量
    String[] testdemo;
    String[] mode;
    String[] tap;
    public JSONArray jsonArray;


    public void findmode(String strRead){
        try {
            JSONObject jo = new JSONObject(strRead);               //解析JSON数据
            jsonArray = jo.getJSONArray("list");  //转换JSON数组\
            if (jsonArray == null || jsonArray.length()==0) {
                datamode = 1;
            }else{
                //初始化数组
                imageurl = new String[jsonArray.length()];
                titleurl = new String[jsonArray.length()];
                playcount = new String[jsonArray.length()];
                idurl = new String[jsonArray.length()];
                scoreurl = new String[jsonArray.length()];
                remarksurl = new String[jsonArray.length()];
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    //取值
                    String names = jsonObject.getString("vod_name");        //标题
                    String pic = jsonObject.getString("vod_pic");           //图片地址
                    String hits = jsonObject.getString("vod_hits");         //播放次数
                    String score = jsonObject.getString("vod_score");       //评分
                    String id = jsonObject.getString("vod_id");             //视频id
                    String remarks = jsonObject.getString("vod_remarks");   //视频状态
                    if (remarks != null && remarks.length()!=0) {
                        if (!remarks.substring(0,2).equals("更新")) {
                            remarks = "完结";
                        }
                    }

                    String url = jsonObject.getString("vod_play_url");        //视频地址

                    testdemo = url.split("\\u0024\\u0024\\u0024");
                    videourl = new String[testdemo.length][];
                    for (int k = 0; k < testdemo.length; k++) {
                        if (testdemo[k].indexOf("#")==-1) {
                            mode = testdemo[0].split("#");
                            videourl[k] = new String[1];
                            tap = mode[0].split("\\u0024");
                            videourl[k][0] = tap[1];
                        }else{
                            mode = testdemo[k].split("#");
                            videourl[k] = new String[mode.length];
                            for (int j = 0; j < mode.length; j++) {
                                tap = mode[j].split("\\u0024");
                                videourl[k][j] = tap[1];
                            }
                        }
                    }
                    list.add(videourl);

                    imageurl[i] = pic;
                    titleurl[i] = names;
                    playcount[i] = hits;
                    idurl[i] = id;
                    scoreurl[i] = score;
                    remarksurl[i] = remarks;
                }
                /*for (int j = 0; j <list.size() ; j++) {

                    Log.i("wdnmd","视频id："+idurl[j]);
                    Log.i("wdnmd","视频标题："+titleurl[j]);
                    Log.i("wdnmd","图片地址："+imageurl[j]);
                    Log.i("wdnmd","评分："+scoreurl[j]);
                    Log.i("wdnmd","播放次数："+playcount[j]);
                    Log.i("wdnmd","视频状态："+remarksurl[j]);

                    String [][] sb = list.get(j);
                    for (int i = 0; i <sb.length ; i++) {
                        for (int k = 0; k <sb[i].length ; k++) {
                            Log.i("wdnmd","视频地址："+sb[i][k]);
                        }
                    }
                }*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Post请求获取视频信息
     * @param text
     */
    public void httpclienttow(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(httpurl);          //获取服务器地址
                    HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);         //打开httpURLConnection的输入流
                    connection.setDoOutput(true);        //打开httpURLConnection的输出流
                    connection.setRequestMethod("POST"); //设置请求方法
                    connection.setUseCaches(false);      //设置不使用缓存，POST请求使用缓存可能会出现一些异常
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");      //设置请求头
                    JSONObject param = new JSONObject();
                    param.put("ac","detail");
                    param.put("wd",text);
                    connection.connect();        //连接

                    //得到请求的输出流对象
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(param.toString());
                    writer.flush();

                    // 获取服务端响应，通过输入流来读取URL的响应
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    StringBuffer sbf = new StringBuffer();

                    //获取数据
                    while ((strRead = reader.readLine()) != null) {
                        sbf.append(strRead);
                    }
                    reader.close();
                    // 关闭连接
                    connection.disconnect();
                    strRead = sbf.toString();
                    findmode(strRead);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
