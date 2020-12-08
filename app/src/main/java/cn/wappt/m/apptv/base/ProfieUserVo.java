package cn.wappt.m.apptv.base;

/**
 * @author: wsq
 * @date: 2020/9/10
 * Description:个人信息
 */
public class ProfieUserVo {
    private String user_name;//用户名
    private String Membergroup;//所属会员组
    private String Accountpoints;//账户积分
    private String Membershipperiod;//会员期限
    private String QQnumber;//QQ号码
    private int e_mail;//Email地址
    private int Registration; //注册时间
    private int Lastlogin; //上次登录
    private int Logintimes; //登录次数
    private int LoginIP;  //登陆IP
    private int LoginTime; //登陆时间

    public ProfieUserVo() {
    }

    public ProfieUserVo(String user_name, String membergroup, String accountpoints, String membershipperiod, String QQnumber, int e_mail, int registration, int lastlogin, int logintimes, int loginIP, int loginTime) {
        this.user_name = user_name;
        Membergroup = membergroup;
        Accountpoints = accountpoints;
        Membershipperiod = membershipperiod;
        this.QQnumber = QQnumber;
        this.e_mail = e_mail;
        Registration = registration;
        Lastlogin = lastlogin;
        Logintimes = logintimes;
        LoginIP = loginIP;
        LoginTime = loginTime;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMembergroup() {
        return Membergroup;
    }

    public void setMembergroup(String membergroup) {
        Membergroup = membergroup;
    }

    public String getAccountpoints() {
        return Accountpoints;
    }

    public void setAccountpoints(String accountpoints) {
        Accountpoints = accountpoints;
    }

    public String getMembershipperiod() {
        return Membershipperiod;
    }

    public void setMembershipperiod(String membershipperiod) {
        Membershipperiod = membershipperiod;
    }

    public String getQQnumber() {
        return QQnumber;
    }

    public void setQQnumber(String QQnumber) {
        this.QQnumber = QQnumber;
    }

    public int getE_mail() {
        return e_mail;
    }

    public void setE_mail(int e_mail) {
        this.e_mail = e_mail;
    }

    public int getRegistration() {
        return Registration;
    }

    public void setRegistration(int registration) {
        Registration = registration;
    }

    public int getLastlogin() {
        return Lastlogin;
    }

    public void setLastlogin(int lastlogin) {
        Lastlogin = lastlogin;
    }

    public int getLogintimes() {
        return Logintimes;
    }

    public void setLogintimes(int logintimes) {
        Logintimes = logintimes;
    }

    public int getLoginIP() {
        return LoginIP;
    }

    public void setLoginIP(int loginIP) {
        LoginIP = loginIP;
    }

    public int getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(int loginTime) {
        LoginTime = loginTime;
    }
}
