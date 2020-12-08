/*
package cn.wappt.m.apptv.utiandent;

import android.util.Log;

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
import java.util.Random;

import cn.wappt.m.apptv.utils.Constants;


*
 * @author 纵游四方悠自得
 * @create 2020/11/9--16:01
 * @effect


public class HttpGettingData  {

    private static HttpGettingData httpGettingData;
    //消息传入

    public HttpGettingData( ) {

    }

    //调用生成的对象
    public static HttpGettingData getInstance( ){//方法无需同步，各线程同时访问
        if (httpGettingData==null){
            synchronized (HttpGettingData.class){
                if (httpGettingData==null){
                    httpGettingData=new HttpGettingData();
                }
            }
        }
        return httpGettingData;
    }

    public VideoDetailsutli videoDetailsutli;

    public VideoDetailsutli videoData;
    public List<VideoDetailsutli> videoDetailsutliList ;
    private JSONArray jsonArray;
    String [] tap;
    int [] record = new int[10];
    Random r = new Random();

    String  list_recommend ;  //推荐视频id
    String  list_videoname ;  //推荐视频标题
    String  list_familiar ;   //推荐视频状态
    String  list_pic ;        //推荐视频图片
    String  list_score_all;  //推荐视频评分
    String  list_hits;       //推荐视频播放次数
    String  list_index ;      //推荐视频集数

    //获取推荐视频id
    public void random(){
        int q = 0;
        if (videoDetailsutli.getVod_type_id() != null && videoDetailsutli.getVod_type_id().length>0) {

            //创建随机数
            q = videoDetailsutli.vod_index[Integer.valueOf(videoDetailsutli.getVod_type_id()[0])];
            //随机哪一页
            int rm = r.nextInt(q+1);
            //生成第一个随机数插入数组
            record[0] = r.nextInt(20)+1;
            int item = 1;
            //循环10个不同的随机数
            while (item<record.length){
                boolean bool = true;
                int romm = r.nextInt(20)+1;
                for (int i = 0; i < item; i++) {
                        if (romm == record[i]) {
                            bool = false;
                            break;
                        }
                }
                //判断是否重复
                if (bool) {
                    record[item] = romm;
                    item++;
                }
            }
            System.out.println("    //创建随机数");
             httpGetting(50,Integer.valueOf(videoDetailsutli.getVod_type_id()[0]),rm);
        }
    }

*
     * 推荐视频
     * @param count  数量
     * @param t      类型
     * @param p      页数


    public void httpGetting(final int count,final int t,final int p){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Constants.datazxdl+"?s="+count+"&p="+p+"&pd=3&t="+t);
                    HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //设置请求方法
                    int responrseCode =connection.getResponseCode();
                    //判断是否响应成功
                    if (responrseCode == 200) {
                        //解析
                        InputStream is = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                        StringBuffer sbf = new StringBuffer();

                        String strRead =null;  //转码
                        //获取数据
                        while ((strRead = reader.readLine()) != null) {
                            sbf.append(strRead);
                        }

                        //关闭数据流
                        reader.close();
                        connection.disconnect();

                        strRead = sbf.toString();
                        //调用解析
                        finddata(strRead);
                    }else{
                        //处理未获取到数据反馈提示
                    }
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

    public void finddata(String strRead) throws JSONException {
        videoDetailsutliList = new ArrayList<>();
        JSONArray ja = new JSONArray(strRead);                  //解析JSON数据
        for (int i = 0; i < record.length; i++) {
            JSONObject jsonObject = ja.getJSONObject(record[i]);
            list_recommend = jsonObject.getString("vod_id");
            list_videoname = jsonObject.getString("vod_name");
            list_familiar = jsonObject.getString("vod_state");
            list_pic =  jsonObject.get("vod_pic").toString().replace("mac", "https");
            list_score_all = jsonObject.getString("vod_score");
            list_hits = jsonObject.getString("vod_hits");

            //判断视频状态
            if (list_familiar != null && list_familiar.length()!=0) {
                if (!list_familiar.substring(0,2).equals("更新")) {
                    list_familiar = "完结";
                }
            }

            String url = jsonObject.getString("vod_play_url");        //视频地址
            if (url != null && url.length()>0) {
                String [] testdemo = url.split("\\u0024\\u0024\\u0024");  //切割线路
                //判断是否只有一个视频
                if (testdemo[0].indexOf("#") == -1) {
                    list_index= "1";
                }else{
                    String [] kankan = testdemo[0].split("#");
                    list_index = String.valueOf(kankan.length);
                }
            }else{
                list_index = jsonObject.getString("vod_play_url");
            }
            videoData = new VideoDetailsutli();
            videoData.setList_recommend(list_recommend);
            videoData.setList_videoname(list_videoname);
            videoData.setList_familiar(list_familiar);
            videoData.setList_score_all(list_score_all);

            videoData.setList_pic(list_pic);
            videoData.setList_hits(list_hits);
            videoData.setList_index(list_index);
            videoDetailsutliList.add(videoData);
        }
        for (int i = 0; i <videoDetailsutliList.size() ; i++) {
            Log.i("wdnmd","推荐视频id:"+videoDetailsutliList.get(i).getList_recommend());
            Log.i("wdnmd","推荐视频标题:"+videoDetailsutliList.get(i).getList_videoname());
            Log.i("wdnmd","推荐视频状态:"+videoDetailsutliList.get(i).getList_familiar());
            Log.i("wdnmd","推荐视频图片:"+videoDetailsutliList.get(i).getList_pic());
            Log.i("wdnmd","推荐视频评分:"+videoDetailsutliList.get(i).getList_score_all());
            Log.i("wdnmd","推荐视频播放次数:"+videoDetailsutliList.get(i).getList_hits());
            Log.i("wdnmd","推荐视频集数:"+videoDetailsutliList.get(i).getList_index());
        }




    }


*
     * 根据id获取数据
     * @param id


    public void httpPostting(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Constants.dataAnalysis);
                    HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);         //打开httpURLConnection的输入流
                    connection.setDoOutput(true);        //打开httpURLConnection的输出流
                    connection.setRequestMethod("POST"); //设置请求方法
                    connection.setUseCaches(false);      //设置不使用缓存，POST请求使用缓存可能会出现一些异常
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");      //设置请求头
                    JSONObject param = new JSONObject();
                    param.put("ac","detail");

                    param.put("ids",id);

                    //得到请求的输出流对象
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(param.toString());
                    writer.flush();

                    // 获取服务端响应，通过输入流来读取URL的响应
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    StringBuffer sbf = new StringBuffer();

                    String strRead =null;  //转码
                    //获取数据
                    while ((strRead = reader.readLine()) != null) {
                        sbf.append(strRead);
                    }
                    // 关闭连接
                    reader.close();
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

*
     * 进行解析数据
     * @param strRead
     * @throws JSONException


    private void findmode(String strRead) throws JSONException {
        videoDetailsutli= new VideoDetailsutli();
        JSONObject jo = new JSONObject(strRead);               //解析JSON数据
        jsonArray = jo.getJSONArray("list");            //转换JSON数组

        String [] vod_id = new String[jsonArray.length()];      //视频id
        String [] vod_name = new String[jsonArray.length()];    //视频标题
        String [] vod_year = new String[jsonArray.length()];    //上映年份
        String [] vod_content = new String[jsonArray.length()]; //简介
        String [] vod_score_all = new String[jsonArray.length()];//总评分
        String [] vod_state = new String[jsonArray.length()];   //状态
        String [] vod_hits = new String[jsonArray.length()];    //播放次数
        String [] vod_type_id = new String[jsonArray.length()]; //视频类型
        String [] [] vod_number_name = null;//视频集
        String [] [] list_anthology = null;

        for (int i = 0; i <jsonArray.length() ; i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            vod_id[i] = jsonObject.getString("vod_id");
            vod_name[i] = jsonObject.getString("vod_name");
            vod_year[i] = jsonObject.getString("vod_year");
            vod_content[i] = jsonObject.getString("vod_content");
            vod_score_all[i] = jsonObject.getString("vod_score");
            vod_state[i] = jsonObject.getString("vod_state");
            vod_hits[i] = jsonObject.getString("vod_hits");
            vod_type_id[i] = jsonObject.getString("type_id_1");

            //判断视频状态
            if (vod_state[i] != null && vod_state[i].length()!=0) {
                if (!vod_state[i].substring(0,2).equals("更新")) {
                    vod_state[i] = "完结";
                }
            }

            String url = jsonObject.getString("vod_play_url");        //视频地址
            String [] testdemo = url.split("\\u0024\\u0024\\u0024");        //不同路线的视频
            vod_number_name = new String[testdemo.length][];    //视频地址
            list_anthology = new String[testdemo.length][];    //视频地址

            //获取不同线路的视频地址
            for (int j = 0; j < testdemo.length; j++) {
                //分割该路线的视频
                if (testdemo[j].indexOf("#")==-1) {
                    String [] momo = testdemo[j].split("#");
                    list_anthology [j] = new String[1];
                    vod_number_name [j] = new String[1];
                    String [] tap =momo[0].split("\\u0024");
                    vod_number_name [j][0] = tap[0];
                    list_anthology[j][0] = tap[1];
                }else{
                    String [] momo = testdemo[j].split("#");
                    list_anthology [j] = new String[momo.length];
                    vod_number_name [j] = new String[momo.length];
                    for (int k = 0; k < momo.length; k++) {
                        tap =momo[k].split("\\u0024");
                        list_anthology[j][k] =tap[1];
                        vod_number_name[j][k] =tap[0];
                    }
                }
            }
        }

        videoDetailsutli.setVod_id(vod_id);
        videoDetailsutli.setVod_name(vod_name);
        videoDetailsutli.setVod_year(vod_year);
        videoDetailsutli.setVod_content(vod_content);
        videoDetailsutli.setVod_score_all(vod_score_all);
        videoDetailsutli.setVod_state(vod_state);
        videoDetailsutli.setVod_hits(vod_hits);
        videoDetailsutli.setVod_type_id(vod_type_id);
        videoDetailsutli.setVod_number_name(vod_number_name);
        videoDetailsutli.setList_anthology(list_anthology);
        random();



        //便利
           for (String s:videoDetailsutli.getVod_id()) {
                Log.i("wdnmd","视频id:"+s);
            }
            for (String s:videoDetailsutli.getVod_name()) {
                Log.i("wdnmd","视频标题:"+s);
            }
            for (String s:videoDetailsutli.getVod_year()) {
                Log.i("wdnmd","上映年份:"+s);
            }
            for (String s:videoDetailsutli.getVod_content()) {
                Log.i("wdnmd","简介:"+s);
            }
            for (String s:videoDetailsutli.getVod_score_all()) {
                Log.i("wdnmd","总评分:"+s);
            }
            for (String s:videoDetailsutli.getVod_state()) {
                Log.i("wdnmd","状态:"+s);
            }
            for (String s:videoDetailsutli.getVod_hits()) {
                Log.i("wdnmd","播放次数:"+s);
            }
            for (String s:videoDetailsutli.getVod_type_id()) {
                Log.i("wdnmd","视频类型:"+s);
            }
            for (String[] s:videoDetailsutli.getList_anthology()) {
                for (int i = 0; i < s.length; i++) {
                    Log.i("wdnmd","视频地址:"+s[i]);
                }
            }
        for (String[] s:videoDetailsutli.getVod_number_name()) {
            for (int i = 0; i < s.length; i++) {
                Log.i("wdnmd","视频集:"+s[i]);
            }
        }


    }
}
*/
