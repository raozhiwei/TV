package cn.wappt.m.apptv.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: wsq
 * @date: 2020/11/17
 * Description:
 */
public class DownloadHelper extends SQLiteOpenHelper {

    /**
     * 数据库的构造方法  用来定义数据库的名称  数据库查询的结果集 数据库的版本
     **/

    public DownloadHelper(Context context) {
        super(context, "downloadhelper.db", null, 1);
    }


    public DownloadHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DownloadHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DownloadHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //浏览记录存储的数据库
        String DetailsSql = "create table downloadHelper( download_url varchar(50),download_name varchar(20),download_imagerl varchar(50))";
        db.execSQL(DetailsSql);
    }

    /**
     * 当数据库的版本号发生变化的时候(增加的时候) 调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table downloadHelper add account varchar(20)");
    }
}
