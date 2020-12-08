package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/12/7
 * Description:
 */
public class VideoDetailsutliRecommend {

    //根据视频id获取相关视频推荐
    private String  list_recommend;    //推荐视频id
    private String  list_videoname;    //推荐视频标题
    private String  list_familiar;     //推荐视频状态
    private String  list_pic;          //推荐视频图片
    private String  list_score_all;    //推荐视频评分
    private String  list_hits;         //推荐视频播放次数
    private String  list_index;        //推荐视频集数
    private String  list_number_name;        //推荐视频集数

    public VideoDetailsutliRecommend() {
    }

    @Override
    public String toString() {
        return "VideoDetailsutliRecommend{" +
                "list_recommend='" + list_recommend + '\'' +
                ", list_videoname='" + list_videoname + '\'' +
                ", list_familiar='" + list_familiar + '\'' +
                ", list_pic='" + list_pic + '\'' +
                ", list_score_all='" + list_score_all + '\'' +
                ", list_hits='" + list_hits + '\'' +
                ", list_index='" + list_index + '\'' +
                ", list_number_name='" + list_number_name + '\'' +
                '}';
    }

    public String getList_recommend() {
        return list_recommend;
    }

    public void setList_recommend(String list_recommend) {
        this.list_recommend = list_recommend;
    }

    public String getList_videoname() {
        return list_videoname;
    }

    public void setList_videoname(String list_videoname) {
        this.list_videoname = list_videoname;
    }

    public String getList_familiar() {
        return list_familiar;
    }

    public void setList_familiar(String list_familiar) {
        this.list_familiar = list_familiar;
    }

    public String getList_pic() {
        return list_pic;
    }

    public void setList_pic(String list_pic) {
        this.list_pic = list_pic;
    }

    public String getList_score_all() {
        return list_score_all;
    }

    public void setList_score_all(String list_score_all) {
        this.list_score_all = list_score_all;
    }

    public String getList_hits() {
        return list_hits;
    }

    public void setList_hits(String list_hits) {
        this.list_hits = list_hits;
    }

    public String getList_index() {
        return list_index;
    }

    public void setList_index(String list_index) {
        this.list_index = list_index;
    }

    public String getList_number_name() {
        return list_number_name;
    }

    public void setList_number_name(String list_number_name) {
        this.list_number_name = list_number_name;
    }

    public VideoDetailsutliRecommend(String list_recommend, String list_videoname, String list_familiar, String list_pic, String list_score_all, String list_hits, String list_index, String list_number_name) {
        this.list_recommend = list_recommend;
        this.list_videoname = list_videoname;
        this.list_familiar = list_familiar;
        this.list_pic = list_pic;
        this.list_score_all = list_score_all;
        this.list_hits = list_hits;
        this.list_index = list_index;
        this.list_number_name = list_number_name;
    }
}
