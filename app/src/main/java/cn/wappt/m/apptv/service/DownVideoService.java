package cn.wappt.m.apptv.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeffmony.downloader.VideoDownloadManager;
import com.jeffmony.downloader.model.VideoTaskItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.wappt.m.apptv.utils.MessageEvent;


/**
 * @author: wsq
 * @date: 2020/10/13
 * Description:
 */
public class DownVideoService extends Service {


    String url; //url下载地址
    String Imageurl;//下载图片
    String name;//电影名称
    VideoTaskItem  videoTaskItem;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 我们这里执行服务启动都要做的操作
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
         url=intent.getStringExtra("url");
         Imageurl=intent.getStringExtra("Imageurl");
         name=intent.getStringExtra("name");
         videoTaskItem=new VideoTaskItem(url);
    //    videoTaskItem.setMimeType(Imageurl);
        //开始下载
         VideoDownloadManager.getInstance().startDownload(videoTaskItem);

        //注册EventBus
       // EventBus.getDefault().register(this);
    /*    Intent intent1=new Intent(DownVideoService.this, m3u8.class);
        //携带额外数据
      //  intent1.putExtra("videoTaskItem",videoTaskItem);

        *//*     intent.putExtra("nameEpisode",(index+1));*//*
        //发送数据给service
        startService(intent); //启动服务*/

        EventBus.getDefault().post(new MessageEvent(videoTaskItem));
        return super.onStartCommand(intent, flags, startId);
    }

    // 服务销毁时的回调
    @Override
    public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
        SharedPreferences sp =getSharedPreferences("m3u8_list", MODE_PRIVATE);//创建sp对象,如果有key为""的sp就取出
        String peopleListJson = sp.getString("MessageEvent", "");  //如果值为空，则将第二个参数作为默认值赋值
        Gson gson = new Gson();
         List<VideoTaskItem> items = gson.fromJson(peopleListJson, new TypeToken<List<VideoTaskItem>>() {
        }.getType()); //将json字符串转换成List集合
        for (int i = 0; i <items.size() ; i++) {
            if (!items.get(i).isCompleted()){
                VideoDownloadManager.getInstance().pauseDownloadTask(items.get(i));
            }
        }
        //注销EventBus
     //    EventBus.getDefault().unregister(this);
    }

}
