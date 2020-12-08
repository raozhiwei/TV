package cn.wappt.m.apptv.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description: 浏览记录数据库
 */
public class DetailsHelper  extends SQLiteOpenHelper {

    /**
     * 数据库的构造方法  用来定义数据库的名称  数据库查询的结果集 数据库的版本
     **/

    public DetailsHelper(Context context) {
        super(context, "detailshelper.db", null, 1);
    }


    public DetailsHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DetailsHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DetailsHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //浏览记录存储的数据库
        String DetailsSql = "create table browseRecords(vod_id varchar(20),vod_name varchar(20),vod_pic varchar(20),vod_index varchar(20))";
        db.execSQL(DetailsSql);
    }

    /**
     * 当数据库的版本号发生变化的时候(增加的时候) 调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table browseRecords add account varchar(20)");
    }
}
