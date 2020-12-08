package cn.wappt.m.apptv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.views.classification.ClassificationActivty;
import cn.wappt.m.apptv.views.details.Videodetails;
import cn.wappt.m.apptv.views.home.HomeFragment;
import cn.wappt.m.apptv.views.recommend.FeaturedActivty;

/**
 * @author: wsq
 * @date: 2020/10/22
 * Description:动态生成首页
 */

public class DynamicallygeneratehomepageUtils {
    private static DynamicallygeneratehomepageUtils instance;
    public int index = 0;
    public int hightth =  HomeFragment.screenHight;
    public int widthth =  HomeFragment.screenWidth;

    private DynamicallygeneratehomepageUtils() {
    }

    public static synchronized DynamicallygeneratehomepageUtils getInstance() {
        if (instance == null) {
            instance = new DynamicallygeneratehomepageUtils();
        }
        return instance;
    }
    //生成数据
    @SuppressLint("ResourceAsColor")
    public void initvv(LinearLayout sv, View v, String name, int nameid, List<String> nName, List nImage, List nurl, Context context, String fragmentName, List<String> state) {
        if (name != "热点资讯") {
            //创建一个LinearLayout布局包裹标题和4个视频
            LinearLayout videoList = new LinearLayout(context);
            LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            videoList.setOrientation(LinearLayout.VERTICAL);
            videoList.setLayoutParams(layoutheight);

            //包括标题和查看更多
            LinearLayout linkenei = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            linkenei.setOrientation(LinearLayout.HORIZONTAL);
            layoutParams.setMargins(0, 10, 0, 0);
            linkenei.setLayoutParams(layoutParams);
            //创建一个标题
            TextView textView = new TextView(v.getContext());
            //设置属性的宽高
            LinearLayout.LayoutParams layoupa = new LinearLayout.LayoutParams(700, 100);
            //将以上的属性赋给LinearLayout
            textView.setLayoutParams(layoupa);
            textView.setText(name);                 //设置文本内容
            textView.setTextSize(18);               //设置字体大小
            textView.setGravity(Gravity.LEFT);      //设置对齐方式
            textView.setPadding(30, 20, 0, 0);
            //获取字体样式
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/5555.ttf");
            textView.setTypeface(typeface); //添加改变文字样式

            TextView textOnView = new TextView(v.getContext());
            LinearLayout.LayoutParams layoup = new LinearLayout.LayoutParams(350, 80);
            textOnView.setLayoutParams(layoup);
            textOnView.setText("查看更多 >");                 //设置文本内容
            textOnView.setTextSize(12);                     //设置字体大小
            textOnView.setPadding(0, 30, 5, 0);
            textOnView.setGravity(Gravity.RIGHT);      //设置对齐方式
            textOnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();

                    if (fragmentName=="精选") {
                        switch (name) {
                            case "热播推荐":
                                Intent intent = new Intent(context, FeaturedActivty.class);
                                context.startActivity(intent, bundle);
                                break;
                            case "电影":
                                HomeFragment.mviewPager.setCurrentItem(1);
                                break;
                            case "连续剧":
                                HomeFragment.mviewPager.setCurrentItem(2);
                                break;
                            case "综艺":
                                HomeFragment.mviewPager.setCurrentItem(3);
                                break;
                            case "动漫":
                                HomeFragment.mviewPager.setCurrentItem(4);
                                break;
                        }
                    }else if (fragmentName=="电影"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",name);
                        intent.putExtra("sortID",1);
                        intent.putExtra("preferencesname","Movie_Classification");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="连续剧"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",name);
                        intent.putExtra("sortID",2);
                        intent.putExtra("preferencesname","Series_classification");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="综艺"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",name);
                        intent.putExtra("sortID",3);
                        intent.putExtra("preferencesname","Variety_Show");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="动漫"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",name);
                        intent.putExtra("sortID",4);
                        intent.putExtra("preferencesname","Anime_Classification");
                        context.startActivity(intent, bundle);
                    }
                }
            });
            //将文本添加进容器1
            linkenei.addView(textView);
            linkenei.addView(textOnView);
            //将容器1添加进父容器
            videoList.addView(linkenei);
            //循环创建视图容器
            for (int i = 0; i < 2; i++) {
                //创建主视图存放子级容器
                LinearLayout kelaout = new LinearLayout(context);
                kelaout.setOrientation(LinearLayout.HORIZONTAL);
                kelaout.setPadding(10, 10, 0, 10);
                for (int j = 0; j <3; j++) {
                    //再创建一个容器2，存放图片，将他放进父容器里
                    LinearLayout linkelaout = new LinearLayout(v.getContext());
                    LinearLayout.LayoutParams modolyout = new LinearLayout.LayoutParams((widthth/3), LinearLayout.LayoutParams.WRAP_CONTENT);
                    linkelaout.setOrientation(LinearLayout.VERTICAL);
                    linkelaout.setLayoutParams(modolyout);
                    linkelaout.setPadding(10,0,10,0);

                    //创建RelativeLayout容器包容图片和视频状态
                    RelativeLayout rlayout = new RelativeLayout(context);
                    RelativeLayout.LayoutParams rela = new RelativeLayout.LayoutParams((widthth/3),RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rlayout.setLayoutParams(rela);

                    //创建状态
                    TextView textstate = new TextView(v.getContext());

                    RelativeLayout.LayoutParams textStateLout = new RelativeLayout.LayoutParams(((widthth/3)- DensityUtils.dp2px(v.getContext(),20)), RelativeLayout.LayoutParams.WRAP_CONTENT);
                    textStateLout.addRule(rlayout.ALIGN_PARENT_BOTTOM);
                    textstate.setLayoutParams(textStateLout);
                    textstate.setBackgroundResource(R.color.textcolorstate);
                    textstate.setPadding(0,0,14,8);
                    textstate.setTextSize(11);
                    textstate.setGravity(Gravity.RIGHT);
                    textstate.setSingleLine(true);
                    textstate.setEllipsize(TextUtils.TruncateAt.END);
                    //判断是否已经进入第二阶段
                        if (state.get(index).contains(".")) {
                            textstate.setTextColor(Color.YELLOW);
                        }else {
                            textstate.setTextColor(Color.WHITE);
                        }
                        textstate.setText(state.get(index));
                    //创建图像
                    ImageView iview = new ImageView(v.getContext());
                    LinearLayout.LayoutParams iviewlyout = new LinearLayout.LayoutParams(((widthth/3)- DensityUtils.dp2px(v.getContext(),20)),400);
                    iview.setLayoutParams(iviewlyout);
                    //设置背景样式
                    iview.setBackgroundResource(R.drawable.secrch_bg);
                    iview.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
                    //设置图片路径
                    if (nImage.size() > index) {
                        RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(5));
                        //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
                        Glide.with(context)
                                .load(nImage.get(index))
                                .centerCrop()
                                .placeholder(R.drawable.defaultpicture)
                                .apply(options)
                                .into(iview);
                        int finalS1 = index;

                        iview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(context, Videodetails.class);
                                intent.putExtra("detailsName", nName.get(finalS1).toString());
                                intent.putExtra("detailsUrl", nurl.get(finalS1).toString());
                                intent.putExtra("detailsImage", nImage.get(finalS1).toString());
                                context.startActivity(intent, bundle);
                            }
                        });
                    }

                    //生成标题文字
                    TextView textTitle = new TextView(v.getContext());
                    LinearLayout.LayoutParams textTitlelout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textTitle.setLayoutParams(textTitlelout);
                    textTitle.setPadding(0,4 , 0, 8);
                    textTitle.setTextSize(14);               //设置字体大小
                    textTitle.setMaxEms(7);
                    textTitle.setSingleLine(true);
                    textTitle.setEllipsize(TextUtils.TruncateAt.END);
                    if (nName.size() > index) {
                        textTitle.setText(nName.get(index));                //替换成集合里的内容
                    } else {
                        textTitle.setText(" ");
                    }
                    rlayout.addView(iview);
                    rlayout.addView(textstate);

                    //将图片存进容器2
                    linkelaout.addView(rlayout);
                    linkelaout.addView(textTitle);
                    kelaout.addView(linkelaout);
                    index++;
                }
                videoList.addView(kelaout);
            }
            sv.addView(videoList);
        }

    }
 /*   //生成数据
    public void initvvs(LinearLayout sv, View v, String name, int nameid, List<FilmBase> filmBases, Context context, String fragmentName, FragmentActivity activity) {

        //一个总的布局
        if (name == "热点资讯") {
            index = index + 6;
        }
        if (name != "热点资讯") {
            //创建一个LinearLayout布局包裹标题和4个视频
            LinearLayout videoList = new LinearLayout(context);
            LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            videoList.setOrientation(LinearLayout.VERTICAL);
            videoList.setLayoutParams(layoutheight);

            //包括标题和查看更多
            LinearLayout linkenei = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            linkenei.setOrientation(LinearLayout.HORIZONTAL);
            layoutParams.setMargins(0, 10, 0, 0);
            linkenei.setLayoutParams(layoutParams);
            //创建一个标题
            TextView textView = new TextView(v.getContext());
            //设置属性的宽高
            LinearLayout.LayoutParams layoupa = new LinearLayout.LayoutParams(700, 100);
            //将以上的属性赋给LinearLayout
            textView.setLayoutParams(layoupa);
            textView.setText(name);                 //设置文本内容
            textView.setTextSize(18);               //设置字体大小
            textView.setGravity(Gravity.LEFT);      //设置对齐方式
            textView.setPadding(30, 20, 0, 0);
            //获取字体样式
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/5555.ttf");
            textView.setTypeface(typeface); //添加改变文字样式
            //-----------------------------------------
            TextView textOnView = new TextView(v.getContext());
            LinearLayout.LayoutParams layoup = new LinearLayout.LayoutParams(350, 100);
            textOnView.setLayoutParams(layoup);
            textOnView.setText("查看更多  >");                 //设置文本内容
            textOnView.setTextSize(16);               //设置字体大小
            textOnView.setPadding(0, 30, 5, 0);
            textOnView.setGravity(Gravity.RIGHT);      //设置对齐方式
            textOnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();

                    if (fragmentName=="精选") {
                        switch (name) {
                            case "热播推荐":
                                Intent intent = new Intent(context, FeaturedActivty.class);
                                context.startActivity(intent, bundle);
                                break;
                            case "电影":
                                HomeFragment.mviewPager.setCurrentItem(1);
                                break;
                            case "连续剧":
                                HomeFragment.mviewPager.setCurrentItem(2);
                                break;
                            case "综艺":
                                HomeFragment.mviewPager.setCurrentItem(3);
                                break;
                            case "动漫":
                                HomeFragment.mviewPager.setCurrentItem(4);
                                break;
                        }
                    }else if (fragmentName=="电影"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",1);
                        intent.putExtra("preferencesname","Movie_Classification");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="连续剧"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",2);
                        intent.putExtra("preferencesname","Series_classification");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="综艺"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",3);
                        intent.putExtra("preferencesname","Variety_Show");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="动漫"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",4);
                        intent.putExtra("preferencesname","Anime_Classification");
                        context.startActivity(intent, bundle);
                    }
                }
            });
            //将文本添加进容器1
            linkenei.addView(textView);
            linkenei.addView(textOnView);
            //将容器1添加进父容器
            videoList.addView(linkenei);
            //循环创建视图容器
            for (int i = 0; i < 2; i++) {
                //创建主视图存放子级容器
                LinearLayout kelaout = new LinearLayout(context);
                kelaout.setOrientation(LinearLayout.HORIZONTAL);
                kelaout.setPadding(10, 10, 10, 10);
                for (int j = 0; j <3; j++) {
                    //再创建一个容器2，存放图片，将他放进父容器里
                    LinearLayout linkelaout = new LinearLayout(v.getContext());
                    LinearLayout.LayoutParams modolyout = new LinearLayout.LayoutParams((widthth/3), LinearLayout.LayoutParams.WRAP_CONTENT);
                    linkelaout.setPadding(20,0,20,0);
                    linkelaout.setOrientation(LinearLayout.VERTICAL);
                    linkelaout.setLayoutParams(modolyout);

                    //创建RelativeLayout容器包容图片和视频状态
                    RelativeLayout rlayout = new RelativeLayout(context);
                    RelativeLayout.LayoutParams rela = new RelativeLayout.LayoutParams((widthth/3),RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rlayout.setLayoutParams(rela);

                    //创建状态
                    TextView textstate = new TextView(v.getContext());

                    RelativeLayout.LayoutParams textStateLout = new RelativeLayout.LayoutParams(((widthth/3)- DensityUtils.dp2px(v.getContext(),20)), RelativeLayout.LayoutParams.WRAP_CONTENT);
                    textStateLout.addRule(rlayout.ALIGN_PARENT_BOTTOM);
                    textstate.setLayoutParams(textStateLout);
                    textstate.setBackgroundResource(R.color.textcolorstate);
                    textstate.setPadding(0,0,14,8);
                    textstate.setTextSize(11);
                    textstate.setGravity(Gravity.RIGHT);
                    textstate.setSingleLine(true);
                    textstate.setEllipsize(TextUtils.TruncateAt.END);
                    textstate.setTextColor(Color.WHITE);
                    textstate.setText(filmBases.get(index).getFilm_remarks());

                    //创建图像
                    ImageView iview = new ImageView(v.getContext());
                    LinearLayout.LayoutParams iviewlyout = new LinearLayout.LayoutParams(((widthth/3)- DensityUtils.dp2px(v.getContext(),20)),400);
                    iview.setLayoutParams(iviewlyout);
                    //设置背景样式
                    iview.setBackgroundResource(R.drawable.secrch_bg);
                    iview.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
                    //设置图片路径
                    if (filmBases!=null&&filmBases.size() > index) {
                        RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(5));
                        //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
                        Glide.with(context)
                                .load(filmBases.get(index).getFilm_image())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .placeholder(R.drawable.defaultpicture)
                                .apply(options)
                                .into(iview);
                        int finalS1 = index;
                        iview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(context, Videodetails.class);
                                intent.putExtra("detailsName", filmBases.get(finalS1).getFilm_name().toString());
                                intent.putExtra("detailsUrl", filmBases.get(finalS1).getFilm_url().toString());
                                intent.putExtra("detailsImage", filmBases.get(finalS1).getFilm_image().toString());
                                context.startActivity(intent, bundle);
                            }
                        });
                    }

                    //生成标题文字
                    TextView textTitle = new TextView(v.getContext());
                    LinearLayout.LayoutParams textTitlelout = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textTitle.setLayoutParams(textTitlelout);
                    textTitle.setPadding(0,4 , 0, 8);
                    textTitle.setTextSize(14);               //设置字体大小
                    textTitle.setMaxEms(7);
                    textTitle.setSingleLine(true);
                    textTitle.setEllipsize(TextUtils.TruncateAt.END);
                    if (filmBases!=null&&filmBases.size() > index) {
                        textTitle.setText(filmBases.get(index).getFilm_name());                //替换成集合里的内容
                    } else {
                        textTitle.setText(" ");
                    }

                    rlayout.addView(iview);
                    rlayout.addView(textstate);

                    //将图片存进容器2
                    linkelaout.addView(rlayout);
                    linkelaout.addView(textTitle);
                    kelaout.addView(linkelaout);
                    index++;
                }
                videoList.addView(kelaout);
            }
            sv.addView(videoList);
        }

    }
*/

}
