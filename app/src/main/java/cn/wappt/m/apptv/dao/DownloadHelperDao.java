package cn.wappt.m.apptv.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.base.DownloadHelperUtil;
import cn.wappt.m.apptv.helper.DownloadHelper;

/**
 * @author: wsq
 * @date: 2020/11/17
 * Description:
 */
public class DownloadHelperDao {
    private DownloadHelper helper;

    public DownloadHelperDao(Context context)
    {
        helper = new DownloadHelper(context);
    }



    /**
     *   添加一条记录到数据库
     * @param download_url   url
     * @param download_name   名称
     * @param download_imagerl   图片路径
     */
    public void add(String download_url,String download_name,String download_imagerl)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            System.out.println("添加一条记录到数据库");
            db.execSQL("insert into downloadHelper( download_url ,download_name ,download_imagerl) values(?,?,?)",
                    new Object[]{  download_url, download_name,download_imagerl});
        }catch (Exception e){
            e.printStackTrace();
            Log.d("add", "添加数据异常：" + e.toString());
        }finally {
            db.close();
        }
    }

    //修改数据
    public void update_index(String download_url,String download_name, String  download_imagerl){
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            System.out.println("修改数据");
            db.execSQL("update downloadHelper set download_name=?,download_imagerl=? where download_url=?",new String[]{
                    download_name ,download_imagerl,download_url
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
    public List<DownloadHelperUtil> findByBrowse()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from downloadHelper ",null);
        List<DownloadHelperUtil> list = new ArrayList<DownloadHelperUtil>();
        while(cursor.moveToNext())
        {
            String download_name = cursor.getString(cursor.getColumnIndex("download_name"));
            String download_imagerl = cursor.getString(cursor.getColumnIndex("download_imagerl"));
            String download_url = cursor.getString(cursor.getColumnIndex("download_url"));
            DownloadHelperUtil columnbase=new DownloadHelperUtil(download_name,download_url,download_imagerl);
            list.add(columnbase);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 对比数据是否重复 返回数据
     * */
    public int  findByBrowse_index(String download_url)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from downloadHelper where download_url=?",new String[]{
                download_url
        });

        int count =cursor.getCount();
        System.out.println("有几条数据:   "+count);
        cursor.close();
        db.close();
        return   count;
    }


    //清空数据
    public void delectData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String delecctData="delete from downloadHelper";
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
    public void delectid(String download_url){
        SQLiteDatabase db = helper.getWritableDatabase();
        String delecctData="delete from downloadHelper  WHERE download_url=?";
        try {
            db.execSQL(delecctData,new String[]{download_url});
        }catch (Exception e){
            e.printStackTrace();
            Log.d("delectDATA", "清除数据的数据异常：" + e.toString());
        }finally {
            db.close();
        }
    }



}
