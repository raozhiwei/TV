package cn.wappt.m.apptv.utiandent;

import java.util.List;

/**
 * @author 纵游四方悠自得
 * @create 2020/9/24--15:33
 * @effect
 */

public class VideoDetails {

    private String vod_id;      //视频id
    private String vod_name;    //视频名称
    private String vod_year;    //上映年份
    private String vod_content; //简介
    private String vod_score_all;//总评分
    private String vod_state;   //状态
    private String vod_like;    //喜欢次数
    private List<String> list_anthology;    //集数
    private List<String> list_recommend;    //推荐的id
    private List<String> list_videoname;    //视频名称
    private List<String> list_familiar;     //视频状态
    private List<String> vod_pic;     //推荐视频图片

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

    public String getVod_year() {
        return vod_year;
    }

    public void setVod_year(String vod_year) {
        this.vod_year = vod_year;
    }

    public String getVod_content() {
        return vod_content;
    }

    public void setVod_content(String vod_content) {
        this.vod_content = vod_content;
    }

    public String getVod_score_all() {
        return vod_score_all;
    }

    public void setVod_score_all(String vod_score_all) {
        this.vod_score_all = vod_score_all;
    }

    public String getVod_state() {
        return vod_state;
    }

    public void setVod_state(String vod_state) {
        this.vod_state = vod_state;
    }

    public String getVod_like() {
        return vod_like;
    }

    public void setVod_like(String vod_like) {
        this.vod_like = vod_like;
    }

    public List<String> getList_anthology() {
        return list_anthology;
    }

    public void setList_anthology(List<String> list_anthology) {
        this.list_anthology = list_anthology;
    }

    public List<String> getList_recommend() {
        return list_recommend;
    }

    public void setList_recommend(List<String> list_recommend) {
        this.list_recommend = list_recommend;
    }

    public List<String> getList_videoname() {
        return list_videoname;
    }

    public void setList_videoname(List<String> list_videoname) {
        this.list_videoname = list_videoname;
    }

    public List<String> getList_familiar() {
        return list_familiar;
    }

    public void setList_familiar(List<String> list_familiar) {
        this.list_familiar = list_familiar;
    }

    public List<String> getVod_pic() {
        return vod_pic;
    }

    public void setVod_pic(List<String> vod_pic) {
        this.vod_pic = vod_pic;
    }
}
