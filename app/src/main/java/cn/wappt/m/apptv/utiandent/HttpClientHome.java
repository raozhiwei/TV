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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.wappt.m.apptv.utils.Constants;

/**
 * @author 纵游四方悠自得
 * @create 2020/9/29--18:43
 * @effect
 */
public class HttpClientHome {
    private String httpurl = Constants.dataAnalysis;

    String strRead =null;  //转码
    public String [] [] httpvideomode;  //不同线路的不同视频源


    //-------第二部分   获取视频信息(集数，播放次数，评分)
    //创建变量
    public String [] datamode ;    //临时变量
    public String [] number;   //储存视频数量
    public String [] count;    //储存播放次数
    public String [] score;    //储存评分
    public String [] imageurl;    //储存图片
    public String [] title;    //储存标题
    public String [] remarks;    //储存视频状态
    public  String [] id;    //储存视频id

    //初始化变量
    public void initmode(int i){
        number = new String[i];
        count = new String[i];
        score = new String[i];
        imageurl = new String[i];
        title = new String[i];
        remarks = new String[i];
        id = new String[i];
    }


    /**
     * Post请求获取视频信息
     * @param artlist
     */
    public void httpclienttow(final String [] artlist){
        //调用方法初始化变量
        initmode(artlist.length);
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
                    //将数组转换成JSON格式数组
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i <artlist.length ; i++) {
                        jsonArray.put(artlist[i]);
                    }
                    param.put("ids",jsonArray);
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
                    //解析数据
                    String [] sdf = strRead.split(",");
                    findjsonmode(sdf);

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

    /**
     /**
     * 解析数据(获取集数，播放次数，评分)
     * @param sdp
     */
    public void findjsonmode(String [] sdp){
        //定义初始变量
        String [] datamode1;
        int j = 0;

        //取出视频地址
        for (int i = 0; i < sdp.length; i++) {
            //获取视频地址
            if (sdp[i].length()>20) {
                if (sdp[i].substring(1,13).equals("vod_play_url")) {
                    //截取视频数量
                    datamode = sdp[i].split("\\u0024\\u0024\\u0024");
                    datamode1 = datamode[0].split("#");
                    number[j] = String.valueOf(datamode1.length);
                    j++;
                }
            }
            //获取视频id
            if (sdp[i].length()>19) {
                if (sdp[i].substring(10,18).equals("vod_id\":")) {
                    datamode = sdp[i].split(":");
                    id[j] = datamode[2];
                }
            }
            if (sdp[i].length()>10) {
                if(sdp[i].substring(2,10).equals("vod_id\":")){
                    datamode = sdp[i].split(":");
                    id[j] = datamode[1];
                }
            }

            //获取视频播放次数
            if (sdp[i].length()>12) {
                if (sdp[i].substring(1,11).equals("vod_hits\":")) {
                    //截取播放次数
                    datamode = sdp[i].split(":");
                    count[j] = datamode[1]; //录入播放次数

                }
            }
            //获取评分
            if (sdp[i].length()>14) {
                if (sdp[i].substring(1,12).equals("vod_score\":")) {
                    datamode = sdp[i].split(":");
                    datamode1 = datamode[1].split("\"");
                    score[j] = datamode1[1];

                }
            }
            //获取标题
            if (sdp[i].length()>14) {
                if (sdp[i].substring(1,11).equals("vod_name\":")) {
                    datamode = sdp[i].split(":");
                    title[j] = convertUnicodeToCh(datamode[1]).substring(1,convertUnicodeToCh(datamode[1]).length()-1);
                }
            }
            //获取状态
            if (sdp[i].length()>13) {
                if (sdp[i].substring(1,14).equals("vod_remarks\":")) {
                    datamode = sdp[i].split(":");
                    if (convertUnicodeToCh(datamode[1]).substring(1,3).equals("更新")) {
                        remarks[j] = convertUnicodeToCh(datamode[1].substring(1,datamode[1].length()-1));
                    }else {
                        remarks[j] = "完结";
                    }
                }
            }
            //获取视频图片
            if (sdp[i].length()>9) {
                if (sdp[i].substring(1,10).equals("vod_pic\":")) {
                    datamode = sdp[i].substring(11,sdp[i].length()-1).split("\\\\");
                    StringBuffer sb = new StringBuffer();
                    for (int eee = 0; eee <datamode.length ; eee++) {
                        sb.append(datamode[eee]);
                    }
                    imageurl[j] = sb.toString();
                }
            }

        }
    }

    /**
     * unicode转字符串
     */

    private static String convertUnicodeToCh(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\w{4}))");
        Matcher matcher = pattern.matcher(str);

        // 迭代，将str中的所有unicode转换为正常字符
        while (matcher.find()) {
            String unicodeFull = matcher.group(1); // 匹配出的每个字的unicode，比如\u67e5
            String unicodeNum = matcher.group(2); // 匹配出每个字的数字，比如\u67e5，会匹配出67e5

            // 将匹配出的数字按照16进制转换为10进制，转换为char类型，就是对应的正常字符了
            char singleChar = (char) Integer.parseInt(unicodeNum, 16);

            // 替换原始字符串中的unicode码
            str = str.replace(unicodeFull, singleChar + "");
        }
        return str;
    }


    /**
     * POST请求接口
     * @param ids     请求参数
     */
    public void httpclient(final String ids){
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
                    param.put("ids",ids);
                    connection.connect();        //连接

                    // 得到请求的输出流对象
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
                    //解析数据
                    String [] sdf = strRead.split(",");
                    jsonmode(sdf);
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

    //解析JSON格式
    public void jsonmode(String [] sdf){
        //解析JSON格式数据
        List<String> nolist = new ArrayList<String>();  //创建集合接收数据
        //取出视频地址
        for (int i = 0; i < sdf.length; i++) {
            if (sdf[i].length()>20) {
                if (sdf[i].substring(1,13).equals("vod_play_url")) {
                    strRead = sdf[i];
                }
            }
        }
        //判断是否是多线路
        sdf = strRead.split("\\u0024\\u0024\\u0024");
        String [] [] videomode = new String[sdf.length][];
        for (int i = 0; i <sdf.length ; i++) {
            String [] videodemoput = sdf[i].split("#");
            videomode[i] = new String[videodemoput.length];
            for (int j = 0; j <videodemoput.length ; j++) {
                videomode[i][j] = videodemoput[j];
            }
        }

        //获取到多条线路的视频地址后截取路径

        StringBuffer sba ;
        String [] nameput ;
        for (int i = 0; i <sdf.length ; i++) {
            String [] videodemoput = sdf[i].split("#");
            videomode[i] = new String[videodemoput.length];
            for (int j = 0; j <videodemoput.length ; j++) {
                sba = new StringBuffer();
                nameput = videodemoput[j].split("\\u0024");
                nameput = nameput[1].split("\\\\");
                for (int k = 0; k <nameput.length ; k++) {
                    sba.append(nameput[k]);
                }
                videomode[i][j] = sba.toString();
            }
        }
        httpvideomode = videomode;

    }
}
