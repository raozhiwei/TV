package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/10/21
 * Description:
 */
public class DataInforVidbase {
    //获取的数据
      String vod_id;//id
      String vod_name;//影名
      String vod_pic;//图片
      String vod_score;//评分
      String vod_total; //总集数
      int vod_serial; //连栽数
    public String getVod_score() {
        return vod_score;
    }

    public void setVod_score(String vod_score) {
        this.vod_score = vod_score;
    }

    public String getVod_total() {
        return vod_total;
    }

    public void setVod_total(String vod_total) {
        this.vod_total = vod_total;
    }

    public int getVod_serial() {
        return vod_serial;
    }

    public void setVod_serial(int vod_serial) {
        this.vod_serial = vod_serial;
    }

    public DataInforVidbase(String vod_id, String vod_name, String vod_pic, String vod_score, String vod_total, int vod_serial) {
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_pic = vod_pic;
        this.vod_score = vod_score;
        this.vod_total = vod_total;
        this.vod_serial = vod_serial;
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

    public DataInforVidbase() {
    }

    public DataInforVidbase(String vod_id, String vod_name, String vod_pic) {
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_pic = vod_pic;
    }

    @Override
    public String toString() {
        return "DataInforVidbase{" +
                "vod_id='" + vod_id + '\'' +
                ", vod_name='" + vod_name + '\'' +
                ", vod_pic='" + vod_pic + '\'' +
                '}';
    }
}
