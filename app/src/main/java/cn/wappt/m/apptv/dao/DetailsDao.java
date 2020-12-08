package cn.wappt.m.apptv.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.base.BrowseRecordsBase;
import cn.wappt.m.apptv.helper.DetailsHelper;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description: 浏览记录
 */
public class DetailsDao {

    private DetailsHelper helper;

    public DetailsDao(Context context)
    {
        helper = new DetailsHelper(context);
    }


    String DetailsSql = "create table BrowseRecordsBase(vod_id varchar(20),vod_name varchar(20),vod_pic varchar(20),vod_index varchar(20))";

    /**
     *  添加一条记录到数据库
     * @param vod_id  视频id
     * @param vod_name
     * @param vod_pic  图片
     * @param vod_index  播放哪一集
     */
    public void add(String vod_id,String vod_name,String vod_pic,String vod_index)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            System.out.println("添加一条记录到数据库");
       db.execSQL("insert into browseRecords(vod_id ,vod_name ,vod_pic ,vod_index) values(?,?,?,?)",
                        new Object[]{ vod_id, vod_name, vod_pic,vod_index});
        }catch (Exception e){
            e.printStackTrace();
            Log.d("add", "添加数据异常：" + e.toString());
        }finally {
            db.close();
        }
    }

    //修改数据
    public void update_index(String vod_id,String vod_index){
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            System.out.println("修改数据");
            db.execSQL("update browseRecords set vod_index=? where vod_id=?",new String[]{
                vod_index,vod_id
        });
       }catch (Exception e){
        e.printStackTrace();
        Log.d("add", "修改数据异常：" + e.toString());
    }finally {
        db.close();
    }

    }


    /**
     * 获取数据
     * */
    public List<BrowseRecordsBase> findByBrowse()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from browseRecords ",null);
        List<BrowseRecordsBase> list = new ArrayList<BrowseRecordsBase>();
        while(cursor.moveToNext())
        {
            String vod_id = cursor.getString(cursor.getColumnIndex("vod_id"));
            String vod_name = cursor.getString(cursor.getColumnIndex("vod_name"));
            String vod_pic = cursor.getString(cursor.getColumnIndex("vod_pic"));
            String vod_index = cursor.getString(cursor.getColumnIndex("vod_index"));
            BrowseRecordsBase columnbase=new BrowseRecordsBase(vod_id,vod_name,vod_pic,vod_index);
            list.add(columnbase);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 对比数据是否重复
     * */
    public List<BrowseRecordsBase> findByBrowse_index(String vod_id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from browseRecords where vod_id=?",new String[]{
         vod_id
        });
        List<BrowseRecordsBase> list = new ArrayList<BrowseRecordsBase>();
        while(cursor.moveToNext())
        {
            String vod_ids = cursor.getString(cursor.getColumnIndex("vod_id"));
            String vod_name = cursor.getString(cursor.getColumnIndex("vod_name"));
            String vod_pic = cursor.getString(cursor.getColumnIndex("vod_pic"));
            String vod_index = cursor.getString(cursor.getColumnIndex("vod_index"));
            BrowseRecordsBase columnbase=new BrowseRecordsBase(vod_ids,vod_name,vod_pic,vod_index);
            list.add(columnbase);
        }
        cursor.close();
        db.close();
        return list;
    }


    //清空数据
    public void delectData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String delecctData="delete from browseRecords";
        try {
            db.execSQL(delecctData);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("delectDATA", "清除表的数据异常：" + e.toString());
        }finally {
            db.close();
        }
    }


    //删除一条数据
    public void delectid(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String delecctData="delete from browseRecords  WHERE vod_id=?";
        try {
            db.execSQL(delecctData,new String[]{id});
        }catch (Exception e){
            e.printStackTrace();
            Log.d("delectDATA", "清除数据的数据异常：" + e.toString());
        }finally {
            db.close();
        }
    }


}
