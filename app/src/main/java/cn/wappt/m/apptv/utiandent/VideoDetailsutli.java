package cn.wappt.m.apptv.utiandent;


/**
 * @author 纵游四方悠自得
 * @create 2020/9/24--15:33
 * @effect
 */

public class VideoDetailsutli {

    //根据id获取视频内容
    private String  vod_id;      //视频id
    private String  vod_name;    //视频标题
    private String  vod_year;    //上映年份
    private String  vod_content; //简介
    private String  vod_score_all;//总评分
    private String  vod_state;   //状态
    private String  vod_hits;    //播放次数
    private String [] [] list_anthology;    //视频地址
    private String  vod_type_id; //视频类型
    private String[] vod_types_of;
    private String[] [] vod_number_name;  //视频集

    public VideoDetailsutli() {
    }

    public String[] getVod_types_of() {
        return vod_types_of;
    }

    public void setVod_types_of(String[] vod_types_of) {
        this.vod_types_of = vod_types_of;
    }

    public VideoDetailsutli(String vod_id, String vod_name, String vod_year, String vod_content, String vod_score_all, String vod_state, String vod_hits, String[][] list_anthology, String vod_type_id, String[] vod_types_of, String[][] vod_number_name) {
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_year = vod_year;
        this.vod_content = vod_content;
        this.vod_score_all = vod_score_all;
        this.vod_state = vod_state;
        this.vod_hits = vod_hits;
        this.list_anthology = list_anthology;
        this.vod_type_id = vod_type_id;
        this.vod_types_of = vod_types_of;
        this.vod_number_name = vod_number_name;
    }

    @Override
    public String toString() {
        return "VideoDetailsutli{" +
                "vod_id='" + vod_id + '\'' +
                ", vod_name='" + vod_name + '\'' +
                ", vod_year='" + vod_year + '\'' +
                ", vod_content='" + vod_content + '\'' +
                ", vod_score_all='" + vod_score_all + '\'' +
                ", vod_state='" + vod_state + '\'' +
                ", vod_hits='" + vod_hits + '\'' +
                ", list_anthology=" + list_anthology[0][0] +
                ", vod_type_id='" + vod_type_id + '\'' +
                ", vod_number_name=" +vod_number_name[0][0] +
                ", vod_number_name=" +vod_types_of +
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

    public String getVod_hits() {
        return vod_hits;
    }

    public void setVod_hits(String vod_hits) {
        this.vod_hits = vod_hits;
    }

    public String[][] getList_anthology() {
        return list_anthology;
    }

    public void setList_anthology(String[][] list_anthology) {
        this.list_anthology = list_anthology;
    }

    public String getVod_type_id() {
        return vod_type_id;
    }

    public void setVod_type_id(String vod_type_id) {
        this.vod_type_id = vod_type_id;
    }

    public String[][] getVod_number_name() {
        return vod_number_name;
    }

    public void setVod_number_name(String[][] vod_number_name) {
        this.vod_number_name = vod_number_name;
    }

    public VideoDetailsutli(String vod_id, String vod_name, String vod_year, String vod_content, String vod_score_all, String vod_state, String vod_hits, String[] []list_anthology, String vod_type_id, String[] []vod_number_name) {
        this.vod_id = vod_id;
        this.vod_name = vod_name;
        this.vod_year = vod_year;
        this.vod_content = vod_content;
        this.vod_score_all = vod_score_all;
        this.vod_state = vod_state;
        this.vod_hits = vod_hits;
        this.list_anthology = list_anthology;
        this.vod_type_id = vod_type_id;
        this.vod_number_name = vod_number_name;
    }
}
