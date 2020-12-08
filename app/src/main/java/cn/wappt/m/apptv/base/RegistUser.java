package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description:注册传入的类
 */

public class RegistUser {
    public String user_name;//用户名
    public String user_pwd;//密码
    public  String user_pwd2;//确认密码
    public String ac;//
    public  String to;//手机号码
    public Integer code;//短信验证码
    public Integer verify;//图片验证码

    @Override
    public String toString() {
        return "RegistUser{" +
                "user_name='" + user_name + '\'' +
                ", user_pwd='" + user_pwd + '\'' +
                ", user_pwd2='" + user_pwd2 + '\'' +
                ", ac='" + ac + '\'' +
                ", to='" + to + '\'' +
                ", code=" + code +
                ", verify=" + verify +
                '}';
    }

    public RegistUser() {
    }

    public RegistUser(String user_name, String user_pwd, String user_pwd2, String ac, String to, Integer code, Integer verify) {
        this.user_name = user_name;
        this.user_pwd = user_pwd;
        this.user_pwd2 = user_pwd2;
        this.ac = ac;
        this.to = to;
        this.code = code;
        this.verify = verify;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_pwd2() {
        return user_pwd2;
    }

    public void setUser_pwd2(String user_pwd2) {
        this.user_pwd2 = user_pwd2;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }
}
