package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/10/27
 * Description: 排行榜存储的数据
 */
public class Columnbase  {
    private String vod_id; //id
    private  String  vod_name;//视频名
    private  String vod_pic;//图片
    private  String  vod_blurb ;//简介
    private  String vod_class;// 扩展分类
    private  String vod_score;//平均分

    public Columnbase() {
    }

    public Columnbase(String vod_id, String vod_name, String vod_pic, String vod_blurb, String vod_class, String vod_score) {
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_pic = vod_pic;
        this.vod_blurb = vod_blurb;
        this.vod_class = vod_class;
        this.vod_score = vod_score;
    }

    public String getVod_id() {
        return vod_id;
    }

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    public String getVod_name() {
        return vod_name;
    }

    public void setVod_name(String vod_name) {
        this.vod_name = vod_name;
    }

    public String getVod_pic() {
        return vod_pic;
    }

    public void setVod_pic(String vod_pic) {
        this.vod_pic = vod_pic;
    }

    public String getVod_blurb() {
        return vod_blurb;
    }

    public void setVod_blurb(String vod_blurb) {
        this.vod_blurb = vod_blurb;
    }

    public String getVod_class() {
        return vod_class;
    }

    public void setVod_class(String vod_class) {
        this.vod_class = vod_class;
    }

    public String getVod_score() {
        return vod_score;
    }

    public void setVod_score(String vod_score) {
        this.vod_score = vod_score;
    }

    @Override
    public String toString() {
        return "Columnbase{" +
                "vod_id='" + vod_id + '\'' +
                ", vod_name='" + vod_name + '\'' +
                ", vod_pic='" + vod_pic + '\'' +
                ", vod_blurb='" + vod_blurb + '\'' +
                ", vod_class='" + vod_class + '\'' +
                ", vod_score='" + vod_score + '\'' +
                '}';
    }
}
