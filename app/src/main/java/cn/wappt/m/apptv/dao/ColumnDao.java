package cn.wappt.m.apptv.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.wappt.m.apptv.base.Columnbase;
import cn.wappt.m.apptv.helper.ColumnHelper;
import cn.wappt.m.apptv.utils.DateDiff;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description:
 */
public class ColumnDao {

    private ColumnHelper helper;

    public ColumnDao(Context context)
    {
        helper = new ColumnHelper(context);
    }

    /**
     * 添加一条记录到数据库
     * @param vod_Types 类型
     */
    public void add(String vod_Types,Columnbase columnbase)
    {
        String vod_id=columnbase.getVod_id();
        String vod_name=columnbase.getVod_name();
        String vod_pic=columnbase.getVod_pic();
        String vod_blurb=columnbase.getVod_blurb();
        String vod_class=columnbase.getVod_class();
        String vod_score=columnbase.getVod_score();
         //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into columnHistory(vod_Types,vod_id,vod_name,vod_pic,vod_blurb,vod_class,vod_score,vod_data) values(?,?,?,?,?,?,?,?)",
                new Object[]{ vod_Types, vod_id, vod_name, vod_pic, vod_blurb, vod_class, vod_score,date});
        db.close();
    }

    /**
     *获取数据
     * vod_Types 类型
     * */
    public List<Columnbase> findByColumnTypes(String vod_Types)
    {
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        //计算时间差

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select vod_id,vod_name,vod_pic,vod_blurb,vod_class,vod_score,vod_data from columnHistory where vod_Types=?",new String[]{vod_Types});
        List<Columnbase> list = new ArrayList<Columnbase>();

        DateFormat startTimedata=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while(cursor.moveToNext())
        {
            String vod_id = cursor.getString(cursor.getColumnIndex("vod_id"));
            String vod_name = cursor.getString(cursor.getColumnIndex("vod_name"));
            String vod_pic = cursor.getString(cursor.getColumnIndex("vod_pic"));
            String vod_blurb = cursor.getString(cursor.getColumnIndex("vod_blurb"));
            String vod_class = cursor.getString(cursor.getColumnIndex("vod_class"));
            String vod_score = cursor.getString(cursor.getColumnIndex("vod_score"));
            Columnbase columnbase=new Columnbase(vod_id,vod_name,vod_pic,vod_blurb,vod_class,vod_score);
            list.add(columnbase);
            //进行判断时间是否大于一天
            Date dates = new Date(cursor.getString(cursor.getColumnIndex("vod_data")));
            long daynumber = (int) DateDiff.getInstance().dateDiff(startTimedata.format(dates),startTimedata.format(date) , "yyyy-MM-dd");
            if (daynumber>1){
                System.out.println("获取数据数据返回空");
                list=null;
                break;
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    //删除
    public void delect(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("columnHistory",null,null);
        db.close();
    }

    //删除一条数据
    public void delecttype(String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        String delecctData="delete from columnHistory  WHERE vod_Types=?";
        try {
            db.execSQL(delecctData,new String[]{type});
        }catch (Exception e){
            e.printStackTrace();
            Log.d("delectDATA", "清除数据的数据异常：" + e.toString());
        }finally {
            db.close();
        }
    }

}
