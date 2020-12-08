package cn.wappt.m.apptv.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import cn.wappt.m.apptv.base.UserInformationBean;
import cn.wappt.m.apptv.helper.UserHelper;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description:
 */
public class UserDao {

    private UserHelper helper;

    public UserDao(Context context)
    {
        helper = new UserHelper(context);
    }

    /**
     * 添加一条记录到数据库
     */
    public void add(UserInformationBean userInformationBean )
    {
        String username=userInformationBean.getUsername();
        String membergroup=userInformationBean.getMembergroup();
        String accountpoints=userInformationBean.getAccountpoints();
        String membershipperiod=userInformationBean.getMembershipperiod();
        String qqnumber=userInformationBean.getQqnumber();
        String emailaddress=userInformationBean.getEmailaddress();
        Date registrationtime=userInformationBean.getRegistrationtime();
        Date lastlogin=userInformationBean.getLastlogin();
        String cookie=userInformationBean.getCookie();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into userHelper(username ,membergroup ,accountpoints ,membershipperiod ,qqnumber,emailaddress ,registrationtime  , lastlogin,cookie) values(?,?,?,?,?,?,?,?,?)",
                new Object[]{ username, membergroup, accountpoints, membershipperiod, qqnumber, emailaddress, registrationtime,lastlogin,cookie});
        db.close();
    }


    /**
     *获取数据
     * vod_Types 类型
     * */
    public UserInformationBean findByColumnTypes()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from userHelper",null);

        UserInformationBean userInformationBean=null;
        while(cursor.moveToNext())
        {
            userInformationBean = new UserInformationBean();
            userInformationBean.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            userInformationBean.setMembergroup(cursor.getString(cursor.getColumnIndex("membergroup")));
            userInformationBean.setAccountpoints(cursor.getString(cursor.getColumnIndex("accountpoints")));
            userInformationBean.setMembershipperiod(cursor.getString(cursor.getColumnIndex("membershipperiod")));
            userInformationBean.setQqnumber(cursor.getString(cursor.getColumnIndex("qqnumber")));
            userInformationBean.setEmailaddress(cursor.getString(cursor.getColumnIndex("emailaddress")));
            userInformationBean.setRegistrationtime(new Date(cursor.getString(cursor.getColumnIndex("registrationtime"))));
            userInformationBean.setCookie(cursor.getString(cursor.getColumnIndex("cookie")));
            if (cursor.getString(cursor.getColumnIndex("lastlogin"))!=null && cursor.getString(cursor.getColumnIndex("lastlogin"))!=""){
                userInformationBean.setLastlogin(new Date(cursor.getString(cursor.getColumnIndex("lastlogin"))));
        }

        }
        cursor.close();
        db.close();
        return userInformationBean;
    }

    //删除
    public void delect(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("userHelper",null,null);
        db.close();
    }

}
