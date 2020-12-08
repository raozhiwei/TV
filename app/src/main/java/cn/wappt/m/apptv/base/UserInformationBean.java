package cn.wappt.m.apptv.base;

import java.util.Date;

/**
 * @author: wsq
 * @date: 2020/11/24
 * Description:
 */
public class UserInformationBean {

    String username;
    String membergroup;
    String accountpoints;
    String membershipperiod;
    String qqnumber;
    String emailaddress;
    Date registrationtime;
    Date lastlogin;
    String cookie;

    public UserInformationBean(String username, String membergroup, String accountpoints, String membershipperiod, String qqnumber, String emailaddress, Date registrationtime, Date lastlogin, String cookie) {
        this.username = username;
        this.membergroup = membergroup;
        this.accountpoints = accountpoints;
        this.membershipperiod = membershipperiod;
        this.qqnumber = qqnumber;
        this.emailaddress = emailaddress;
        this.registrationtime = registrationtime;
        this.lastlogin = lastlogin;
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public UserInformationBean() {
    }


    @Override
    public String toString() {
        return "UserInformationBean{" +
                "username='" + username + '\'' +
                ", membergroup='" + membergroup + '\'' +
                ", accountpoints='" + accountpoints + '\'' +
                ", membershipperiod='" + membershipperiod + '\'' +
                ", qqnumber='" + qqnumber + '\'' +
                ", emailaddress='" + emailaddress + '\'' +
                ", registrationtime=" + registrationtime +
                ", lastlogin=" + lastlogin +
                ", cookie='" + cookie + '\'' +
                '}';
    }

    public UserInformationBean(String username, String membergroup, String accountpoints, String membershipperiod, String qqnumber, String emailaddress, Date registrationtime, Date lastlogin) {
        this.username = username;
        this.membergroup = membergroup;
        this.accountpoints = accountpoints;
        this.membershipperiod = membershipperiod;
        this.qqnumber = qqnumber;
        this.emailaddress = emailaddress;
        this.registrationtime = registrationtime;
        this.lastlogin = lastlogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMembergroup() {
        return membergroup;
    }

    public void setMembergroup(String membergroup) {
        this.membergroup = membergroup;
    }

    public String getAccountpoints() {
        return accountpoints;
    }

    public void setAccountpoints(String accountpoints) {
        this.accountpoints = accountpoints;
    }

    public String getMembershipperiod() {
        return membershipperiod;
    }

    public void setMembershipperiod(String membershipperiod) {
        this.membershipperiod = membershipperiod;
    }

    public String getQqnumber() {
        return qqnumber;
    }

    public void setQqnumber(String qqnumber) {
        this.qqnumber = qqnumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public Date getRegistrationtime() {
        return registrationtime;
    }

    public void setRegistrationtime(Date registrationtime) {
        this.registrationtime = registrationtime;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }
}
