package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description:
 */
public class BrowseRecordsBase {
    private String vod_id;
    private String vod_name;
    private String vod_pic;
    private  String vod_index;
    public boolean isSelect;
    public BrowseRecordsBase(String vod_id, String vod_name, String vod_pic, String vod_index) {
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_pic = vod_pic;
        this.vod_index = vod_index;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public BrowseRecordsBase() {

    }

    @Override
    public String toString() {
        return "BrowseRecordsBase{" +
                "vod_id='" + vod_id + '\'' +
                ", vod_name='" + vod_name + '\'' +
                ", vod_pic='" + vod_pic + '\'' +
                ", vod_index='" + vod_index + '\'' +
                '}';
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

    public String getVod_index() {
        return vod_index;
    }

    public void setVod_index(String vod_index) {
        this.vod_index = vod_index;
    }
}
