package cn.wappt.m.apptv.utils;


import com.jeffmony.downloader.model.VideoTaskItem;

/**
 * @author: wsq
 * @date: 2020/10/14
 * Description:定义消息事件类
 */
public class MessageEvent {
        VideoTaskItem videoTaskItem;

    public MessageEvent(VideoTaskItem videoTaskItem) {
        this.videoTaskItem = videoTaskItem;
    }

    public VideoTaskItem getVideoTaskItem() {
        return videoTaskItem;
    }

    public void setVideoTaskItem(VideoTaskItem videoTaskItem) {
        this.videoTaskItem = videoTaskItem;
    }

    public MessageEvent() {
    }

}
