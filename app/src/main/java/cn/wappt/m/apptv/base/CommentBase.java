package cn.wappt.m.apptv.base;

import java.util.List;

/**
 * @author: wsq
 * @date: 2020/11/30
 * Description:
 */
public class CommentBase {
    int comment_id;//编号
    int  comment_mid;//模块id，1视频2文字3专题
    String comment_rid;//关联数据id
    String comment_pid;//数据上级id
    String user_id;//登录用户id;
    String comment_status;//状态0未审核1已审核
    String comment_name;//昵称
    String comment_ip;//ip地址
    String comment_time;//时间
    String  comment_content;//留言内容
    String comment_up;//	顶数
    String comment_down;//	踩数
    String comment_reply;//无
    String comment_report;//	举报;
    List<CommentBase> commentBases;

    public List<CommentBase> getCommentBases() {
        return commentBases;
    }

    public void setCommentBases(List<CommentBase> commentBases) {
        this.commentBases = commentBases;
    }

    public CommentBase(int comment_id, int comment_mid, String comment_rid, String comment_pid, String user_id, String comment_status, String comment_name, String comment_ip, String comment_time, String comment_content, String comment_up, String comment_down, String comment_reply, String comment_report, List<CommentBase> commentBases) {
        this.comment_id = comment_id;
        this.comment_mid = comment_mid;
        this.comment_rid = comment_rid;
        this.comment_pid = comment_pid;
        this.user_id = user_id;
        this.comment_status = comment_status;
        this.comment_name = comment_name;
        this.comment_ip = comment_ip;
        this.comment_time = comment_time;
        this.comment_content = comment_content;
        this.comment_up = comment_up;
        this.comment_down = comment_down;
        this.comment_reply = comment_reply;
        this.comment_report = comment_report;
        this.commentBases = commentBases;
    }

    public CommentBase() {
    }

    public CommentBase(int comment_id, int comment_mid, String comment_rid, String comment_pid, String user_id, String comment_status, String comment_name, String comment_ip, String comment_time, String comment_content, String comment_up, String comment_down, String comment_reply, String comment_report) {
        this.comment_id = comment_id;
        this.comment_mid = comment_mid;
        this.comment_rid = comment_rid;
        this.comment_pid = comment_pid;
        this.user_id = user_id;
        this.comment_status = comment_status;
        this.comment_name = comment_name;
        this.comment_ip = comment_ip;
        this.comment_time = comment_time;
        this.comment_content = comment_content;
        this.comment_up = comment_up;
        this.comment_down = comment_down;
        this.comment_reply = comment_reply;
        this.comment_report = comment_report;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getComment_mid() {
        return comment_mid;
    }

    public void setComment_mid(int comment_mid) {
        this.comment_mid = comment_mid;
    }

    public String getComment_rid() {
        return comment_rid;
    }

    public void setComment_rid(String comment_rid) {
        this.comment_rid = comment_rid;
    }

    public String getComment_pid() {
        return comment_pid;
    }

    public void setComment_pid(String comment_pid) {
        this.comment_pid = comment_pid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    public String getComment_ip() {
        return comment_ip;
    }

    public void setComment_ip(String comment_ip) {
        this.comment_ip = comment_ip;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_up() {
        return comment_up;
    }

    public void setComment_up(String comment_up) {
        this.comment_up = comment_up;
    }

    public String getComment_down() {
        return comment_down;
    }

    public void setComment_down(String comment_down) {
        this.comment_down = comment_down;
    }

    public String getComment_reply() {
        return comment_reply;
    }

    public void setComment_reply(String comment_reply) {
        this.comment_reply = comment_reply;
    }

    public String getComment_report() {
        return comment_report;
    }

    public void setComment_report(String comment_report) {
        this.comment_report = comment_report;
    }

    @Override
    public String toString() {
        return "CommentBase{" +
                "comment_id=" + comment_id +
                ", comment_mid=" + comment_mid +
                ", comment_rid='" + comment_rid + '\'' +
                ", comment_pid='" + comment_pid + '\'' +
                ", user_id='" + user_id + '\'' +
                ", comment_status='" + comment_status + '\'' +
                ", comment_name='" + comment_name + '\'' +
                ", comment_ip='" + comment_ip + '\'' +
                ", comment_time='" + comment_time + '\'' +
                ", comment_content='" + comment_content + '\'' +
                ", comment_up='" + comment_up + '\'' +
                ", comment_down='" + comment_down + '\'' +
                ", comment_reply='" + comment_reply + '\'' +
                ", comment_report='" + comment_report + '\'' +
                ", commentBases=" + commentBases +
                '}';
    }
}
