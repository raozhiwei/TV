package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/11/5
 * Description:
 */
public class DataBean {
    String banner_url;     //详细链接
    String banner_title;    //存放标题
    String banner_style;//存放图片地址
    String banner_id;

    public DataBean(String banner_url, String banner_title, String banner_style, String banner_id) {
        this.banner_url = banner_url;
        this.banner_title = banner_title;
        this.banner_style = banner_style;
        this.banner_id = banner_id;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public DataBean() {
    }

    public DataBean(String banner_url, String banner_title, String banner_style) {
        this.banner_url = banner_url;
        this.banner_title = banner_title;
        this.banner_style = banner_style;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "banner_url='" + banner_url + '\'' +
                ", banner_title='" + banner_title + '\'' +
                ", banner_style='" + banner_style + '\'' +
                '}';
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getBanner_title() {
        return banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getBanner_style() {
        return banner_style;
    }

    public void setBanner_style(String banner_style) {
        this.banner_style = banner_style;
    }
}
