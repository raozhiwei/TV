package cn.wappt.m.apptv.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: wsq
 * @date: 2020/11/24
 * Description:
 */
public class UserHelper extends SQLiteOpenHelper {

    /**
     * 数据库的构造方法  用来定义数据库的名称  数据库查询的结果集 数据库的版本
     **/

    public UserHelper(Context context) {
        super(context, "userhelper.db", null, 1);
    }


    public UserHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UserHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public UserHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //浏览记录存储的数据库
        String DetailsSql = "create table userHelper( username varchar(20),membergroup varchar(20),accountpoints varchar(20),membershipperiod varchar(20),qqnumber varchar(20),emailaddress varchar(20),registrationtime DATETIME , lastlogin DATETIME,cookie varchar(100) )";
        db.execSQL(DetailsSql);
    }

    /**
     * 当数据库的版本号发生变化的时候(增加的时候) 调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table userHelper add account varchar(20)");
    }
}
