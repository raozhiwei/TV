package cn.wappt.m.apptv.utils.ThreadPooL;

/**
 * @author: wsq
 * @date: 2020/11/27
 * Description:任务单元
 */
public abstract class ThreadPoolTask  implements Runnable{
    protected String url;  //图片路径

    public ThreadPoolTask(String url) {
        this.url = url;
    }

    public abstract void run();

    public String getURL() {
        return this.url;
    }

}
