package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/12/2
 * Description: 评论提交
 */
public class CommentLikeBase {
    Integer mid;
    Integer id;
    String type;

    public CommentLikeBase() {
    }

    public CommentLikeBase(Integer mid, Integer id, String type) {
        this.mid = mid;
        this.id = id;
        this.type = type;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
