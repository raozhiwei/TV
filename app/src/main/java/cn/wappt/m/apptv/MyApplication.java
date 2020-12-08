package cn.wappt.m.apptv;

import android.app.Application;

import com.jeffmony.downloader.DownloadConstants;
import com.jeffmony.downloader.VideoDownloadConfig;
import com.jeffmony.downloader.VideoDownloadManager;
import com.jeffmony.downloader.utils.VideoDownloadUtils;

import java.io.File;

/**
 * @author: wsq
 * @date: 2020/10/10
 * Description:应用启动的时候注册下载配置
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        File file = VideoDownloadUtils.getVideoCacheDir(this); //获取视频缓存目录
        if (!file.exists()) {
            file.mkdir(); //创建
        }
        VideoDownloadConfig config = new VideoDownloadManager.Build(this)
                .setCacheRoot(file) //设置缓存根
                .setUrlRedirect(true) //设置网址重定向
                .setTimeOut(DownloadConstants.READ_TIMEOUT, DownloadConstants.CONN_TIMEOUT) //设置超时
                .setConcurrentCount(DownloadConstants.CONCURRENT) //设置并发计数
                .setIgnoreCertErrors(true) //设置忽略证书错误
                .setShouldM3U8Merged(false) //设置应该M3U8合并
                .buildConfig(); //构建配置

        VideoDownloadManager.getInstance().initConfig(config); //初始化配置
    }
}
