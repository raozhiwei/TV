package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description: 登录注册返回的json
 */
public class UserUpdateReceive {
    private  int code;
    private String msg;
    private String data;
    private String url;
    private String wait;
    public UserUpdateReceive() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWait() {
        return wait;
    }

    public void setWait(String wait) {
        this.wait = wait;
    }

    public UserUpdateReceive(int code, String msg, String data, String url, String wait) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.url = url;
        this.wait = wait;
    }
}
