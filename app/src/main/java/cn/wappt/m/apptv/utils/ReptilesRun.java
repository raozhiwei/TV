package cn.wappt.m.apptv.utils;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: wsq
 * @date: 2020/9/22
 * Description:
 */
public class ReptilesRun {
    private static ReptilesRun reptilesRun;

    private ReptilesRun(){
    }

    //调用生成的对象
    public static ReptilesRun getInstance(){//方法无需同步，各线程同时访问
        if (reptilesRun==null){
            synchronized (ReptilesRun.class){
                if (reptilesRun==null){
                    reptilesRun=new ReptilesRun();
                }
            }
        }
        return reptilesRun;
    }



    public  static HashMap runLBT(Elements titleLinks) {
             //获取轮播图数据
               HashMap map=new HashMap();
                List banner_url = new ArrayList<>();
                List banner_title = new ArrayList<>();
                List banner_style = new ArrayList<>();
                     if (titleLinks.size() != 0) {
                         int length = titleLinks.size();
                         for (int i = 0; i < titleLinks.size(); i++) {
                             String url = titleLinks.get(i).select("a").attr("href");
                             String title = titleLinks.get(i).select("a").attr("title");
                             String styles = titleLinks.get(i).select("a").attr("style");
                             String style = styles.substring(21, (styles.length() - 1));
                             if (!style.substring(0, 1).equals("h")) {
                                 style = Constants.AppUrl + style;
                             }
                             banner_url.add(i, url);
                             banner_title.add(i, title);
                             banner_style.add(i, style);
                             map.put("banner_url", banner_url);
                             map.put("banner_title", banner_title);
                             map.put("banner_style", banner_style);
                         }
                     }
        return  map;
    }

    //获取电影推荐数据
    public static HashMap rundyLBoup(Elements titleList){
        HashMap map=new HashMap();
        List  film_name = new ArrayList<>();
        List   film_image= new ArrayList<>();
        List   film_url=new ArrayList<>();
                    if (titleList.size() != 0) {
                        for (int i = 0; i < titleList.size(); i++) {
                            //  qq_phone = titleLinks.select("input[name=user_qq]").attr("value");
                            String styles = titleList.get(i).select("a").attr("data-original");//图片链接
                            String url=titleList.get(i).select("a").attr("href");
                            String name=titleList.get(i).select("a").attr("title");
                            //因为6,7这几条数据是页面隐藏的
                            if (i!=6 && i!=7){

                                if (!styles.substring(0, 1).equals("h")) {
                                    styles = Constants.AppUrl + styles;
                                }
                                film_image.add(styles);
                                film_name.add(name);
                                film_url.add(url);

//                                System.out.println("电影name=" + film_name.get(i));
//                                System.out.println("电影image=" + film_image.get(i));
//                                System.out.println("电影url=" + film_url.get(i));
                            }

                        }
                        map.put("film_image",film_image);
                        map.put("film_name",film_name);
                        map.put("film_url",film_url);

                    }
                    return map;
    }
    //获取电视剧推荐数据
    public static HashMap rundsjLBoup(Elements titleList){
        HashMap map=new HashMap();
        List  film_name = new ArrayList<>();
        List   film_image= new ArrayList<>();
        List   film_url=new ArrayList<>();
        if (titleList.size() != 0) {
            for (int i = 0; i < titleList.size(); i++) {
                String styles = titleList.get(i).select("a").attr("data-original");//图片链接
                String url=titleList.get(i).select("a").attr("href");
                String name=titleList.get(i).select("a").attr("title");
                //因为6,7这几条数据是页面隐藏的
                if (i>=6){
                    if (!styles.substring(0, 1).equals("h")) {
                        styles = Constants.AppUrl + styles;
                    }
                    film_image.add(styles);
                    film_name.add(name);
                    film_url.add(url);
//                                System.out.println("电影name=" + film_name.get(i));
//                                System.out.println("电影image=" + film_image.get(i));
//                                System.out.println("电影url=" + film_url.get(i));
                }

            }
            map.put("film_image",film_image);
            map.put("film_name",film_name);
            map.put("film_url",film_url);

        }
        return map;
    }



    //获取动漫推荐数据
    public static HashMap rundmjLBoup(Elements titleList){
        HashMap map=new HashMap();
        List  film_name = new ArrayList<>();
        List   film_image= new ArrayList<>();
        List   film_url=new ArrayList<>();
        if (titleList.size() != 0) {
            for (int i = 0; i < titleList.size(); i++) {
                String styles = titleList.get(i).select("a").attr("data-original");//图片链接
                String url=titleList.get(i).select("a").attr("href");
                String name=titleList.get(i).select("a").attr("title");
                    if (!styles.substring(0, 1).equals("h")) {
                        styles = Constants.AppUrl + styles;
                    }
                    film_image.add(styles);
                    film_name.add(name);
                    film_url.add(url);
//                                System.out.println("电影name=" + film_name.get(i));
//                                System.out.println("电影image=" + film_image.get(i));
//                                System.out.println("电影url=" + film_url.get(i));
            }
            map.put("film_image",film_image);
            map.put("film_name",film_name);
            map.put("film_url",film_url);

        }
        return map;
    }



}
