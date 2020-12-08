package cn.wappt.m.apptv.views.user;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.wappt.m.apptv.utils.Constants;

/**
 * @author 纵游四方悠自得
 * @create 2020/11/4--15:07
 * @effect   反馈提交接口
 */


public class HttpMessageSubmitted {
    boolean bo = false;
    static String mode = "0";

    String messageUrl = Constants.Feedback;
    public void get(String content){
        final String con = content;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //拼接get请求
                    URL url = new URL(messageUrl+"?gbook_content="+con);
                    //通过url地址打开连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置超时时间
                    conn.setConnectTimeout(3000);
                    //设置请求方式
                    conn.setRequestMethod("GET");
                    //设置请求头
                    conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
                    //返回判断结果，是否提交成功
                    bo =  conn.getResponseCode() == 200;
                    System.out.println("返回状态码："+conn.getResponseCode());
                    System.out.println("返回的信息"+conn.getResponseMessage());
                    if (bo) {
                        mode = "1";
                    }else{
                        mode = "2";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mode = "3";
                } catch (IOException e) {
                    e.printStackTrace();
                    mode = "3";
                }
            }
        }).start();
    }

}
