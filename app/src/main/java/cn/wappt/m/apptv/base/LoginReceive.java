package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description: 登录注册返回的json
 */
public class LoginReceive {
    private  int code;
    private String msg;

    public LoginReceive() {
    }

    @Override
    public String toString() {
        return "{\ncode=" + code +"\n"+
                ", msg='" + msg  +
                '}';
    }

    public LoginReceive(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

}
