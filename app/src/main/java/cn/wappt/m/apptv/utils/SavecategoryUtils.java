package cn.wappt.m.apptv.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wsq
 * @date: 2020/10/22
 * Description:
 */
public class SavecategoryUtils {
    private static SavecategoryUtils instance;

    private SavecategoryUtils() {
    }
    public static synchronized SavecategoryUtils getInstance() {
        if (instance == null) {
            instance = new SavecategoryUtils();
        }
        return instance;
    }
    //Film Classification
    //保存分类数据的方法
    public void addShared(Context context, List name ,List nameid ,String preferencesname){
        SharedPreferences sp = context.getSharedPreferences(preferencesname, Context.MODE_PRIVATE);
        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        ed.clear();
        ed.putString("name",  gson.toJson(name));
        ed.putString("nameid", gson.toJson(nameid));
        //提交数据
        ed.commit();
    }


    //Film Classification
    //保存分类数据的方法
    public void addSharedClassifi(Context context, List sortlist ,List arealist ,List yearslist ,String preferencesname){
        SharedPreferences sp = context.getSharedPreferences(preferencesname, Context.MODE_PRIVATE);
        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        ed.clear();
        ed.putString("sort",  gson.toJson(sortlist));
        ed.putString("area", gson.toJson(arealist));
        ed.putString("years",  gson.toJson(yearslist));
        //提交数据
        ed.commit();
    }

    //返回数据
    public <T> List<T> getDataList(Context context,String preferencesname,String key) {
        SharedPreferences sp = context.getSharedPreferences(preferencesname, Context.MODE_PRIVATE);
        List<T> value=new ArrayList<T>();
        String Json =sp.getString(key, null);
        if (null == Json) {
            return value;
        }
        Gson gson = new Gson();
        value = gson.fromJson(Json, new TypeToken<List<T>>() {
        }.getType());
        return value;
    }


}
