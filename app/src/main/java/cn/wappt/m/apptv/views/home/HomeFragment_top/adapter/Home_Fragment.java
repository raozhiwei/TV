package cn.wappt.m.apptv.views.home.HomeFragment_top.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.FilmBase;
import cn.wappt.m.apptv.utils.DensityUtils;
import cn.wappt.m.apptv.utils.GlideLoadUtils;
import cn.wappt.m.apptv.utils.GlideRoundTransform;
import cn.wappt.m.apptv.views.classification.ClassificationActivty;
import cn.wappt.m.apptv.views.details.Videodetails;
import cn.wappt.m.apptv.views.home.HomeFragment;
import cn.wappt.m.apptv.views.recommend.FeaturedActivty;

/*
 * @author: wsq
 * @date: 2020/11/5
 * Description:  没有用
 */

public class Home_Fragment extends RecyclerView.Adapter<Home_Fragment.Home_FragmentHolder> {
    Context mContext;
    List<FilmBase> filmBases;
    private LayoutInflater inflater; //接受布局文件
    String[] name;
    int[] nameid;
    public int index = 0;
    String fragmentName;
    public int hightth =  HomeFragment.screenHight;//屏幕高度
    public int widthth =  HomeFragment.screenWidth;//屏幕宽度

    protected boolean isScrolling = false;

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }
    public Home_Fragment(Context mContext, List dataInfor ,String[] names,int [] nameids,String fragmentNames ,int indesx) {
        if (filmBases == null) {
            filmBases = new ArrayList<>();
        }
        this.mContext = mContext;
        this.filmBases = dataInfor;
        name=new String[names.length];
        nameid=new int[nameids.length];
        name=names;
        nameid=nameids;
        fragmentName=fragmentNames;
        inflater = LayoutInflater.from(mContext);
        index=indesx;
    }

    @NonNull
    @Override
    public Home_FragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.home_fragment_look, parent, false);
        Home_FragmentHolder holder = new Home_FragmentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Home_FragmentHolder holder, int position) {
            Context context=  holder.linearLayout.getContext();
            //包括标题和查看更多
            LinearLayout linkenei = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linkenei.setOrientation(LinearLayout.HORIZONTAL);
           layoutParams.setMargins(0,20 , 0, 0);
            linkenei.setLayoutParams(layoutParams);
            //创建一个标题
            TextView textView = new TextView(linkenei.getContext());
            //设置属性的宽高
            LinearLayout.LayoutParams layoupa = new LinearLayout.LayoutParams(700, 80);
            //将以上的属性赋给LinearLayout
            textView.setLayoutParams(layoupa);
            textView.setText(name[position]);                 //设置文本内容
            textView.setTextSize(18);               //设置字体大小
            textView.setGravity(Gravity.LEFT);      //设置对齐方式

            //获取字体样式
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/5555.ttf");
            textView.setTypeface(typeface); //添加改变文字样式
            //-----------------------------------------
            TextView textOnView = new TextView(linkenei.getContext());
            LinearLayout.LayoutParams layoup = new LinearLayout.LayoutParams(350, 80);
            textOnView.setLayoutParams(layoup);
            textOnView.setText("查看更多  >");                 //设置文本内容
            textOnView.setTextSize(16);               //设置字体大小
            textOnView.setGravity(Gravity.RIGHT);      //设置对齐方式
            textOnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if (fragmentName=="精选") {
                        switch (name[position]) {
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
                        intent.putExtra("id",nameid[position]);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",1);
                        intent.putExtra("preferencesname","Movie_Classification");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="连续剧"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid[position]);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",2);
                        intent.putExtra("preferencesname","Series_classification");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="综艺"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid[position]);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",3);
                        intent.putExtra("preferencesname","Variety_Show");
                        context.startActivity(intent, bundle);
                    }else if (fragmentName=="动漫"){
                        Intent intent = new Intent(context, ClassificationActivty.class);
                        intent.putExtra("id",nameid[position]);
                        intent.putExtra("name",fragmentName);
                        intent.putExtra("sortID",4);
                        intent.putExtra("preferencesname","Anime_Classification");
                        context.startActivity(intent, bundle);
                    }
                }
            });
            linkenei.setGravity( Gravity.CENTER);
            //将文本添加进容器1
            linkenei.addView(textView);
            linkenei.addView(textOnView);
            //将容器1添加进父容器
            holder.linearLayout.addView(linkenei);
            //循环创建视图容器
            for (int i = 0; i < 2; i++) {
                //创建主视图存放子级容器
                LinearLayout kelaout = new LinearLayout(context);
                kelaout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <3; j++) {
                    //再创建一个容器2，存放图片，将他放进父容器里
                    LinearLayout linkelaout = new LinearLayout(kelaout.getContext());
                    LinearLayout.LayoutParams modolyout = new LinearLayout.LayoutParams((widthth/3-40), LinearLayout.LayoutParams.WRAP_CONTENT);
                    linkelaout.setOrientation(LinearLayout.VERTICAL);
                    if (j==0){
                        modolyout.setMargins(25,0,20,0);
                    }else {
                        modolyout.setMargins(15,0,20,0);
                    }
                    linkelaout.setLayoutParams(modolyout);
                    //创建RelativeLayout容器包容图片和视频状态
                    RelativeLayout rlayout = new RelativeLayout(context);
                    RelativeLayout.LayoutParams rela = new RelativeLayout.LayoutParams(modolyout.width,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rlayout.setLayoutParams(rela);
                    //创建状态
                    TextView textstate = new TextView(kelaout.getContext());
                    RelativeLayout.LayoutParams textStateLout = new RelativeLayout.LayoutParams(modolyout.width, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    textStateLout.addRule(rlayout.ALIGN_PARENT_BOTTOM);
                    textstate.setLayoutParams(textStateLout);
                    textstate.setBackgroundResource(R.color.textcolorstate);
                    textstate.setPadding(5,0,15,8);
                    textstate.setTextSize(11);
                  textstate.setBackgroundResource(R.drawable.linearlayout);
                    textstate.setGravity(Gravity.RIGHT);
                    textstate.setSingleLine(true);
                    textstate.setEllipsize(TextUtils.TruncateAt.END);
                    textstate.setTextColor(Color.WHITE);
                    textstate.setText(filmBases.get(index).getFilm_remarks());
                    //创建图像
                    ImageView iview = new ImageView(kelaout.getContext());
                    LinearLayout.LayoutParams iviewlyout = new LinearLayout.LayoutParams(modolyout.width,400);
                    iview.setLayoutParams(iviewlyout);
                    //设置背景样式
                    iview.setBackgroundResource(R.drawable.secrch_bg);
                    iview.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
                   //设置图片路径
                      if (filmBases!=null&&filmBases.size() > index) {
                          if (!TextUtils.isEmpty(filmBases.get(index).getFilm_image()) && !isScrolling) {
                              // 这里可以用Glide等网络图片加载库
                              run(context,iview,filmBases.get(index).getFilm_image(),((widthth/3+30)- DensityUtils.dp2px(kelaout.getContext(),20)),400);
                          } else {
                              iview.setImageResource(R.drawable.defaultpicture);
                          }
                      }
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

                    //生成标题文字
                    TextView textTitle = new TextView(kelaout.getContext());
                    LinearLayout.LayoutParams textTitlelout = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textTitle.setLayoutParams(textTitlelout);
                    textTitle.setPadding(0,5 , 0, 8);
                    textTitle.setTextSize(14);               //设置字体大小
                    textTitle.setMaxEms(7);
                    textTitle.setSingleLine(true);
                    textTitle.setEllipsize(TextUtils.TruncateAt.END);
                    if (filmBases!=null&&filmBases.size() > index) {
                        textTitle.setText(filmBases.get(index).getFilm_name());                //替换成集合里的内容
                    } else {
                        textTitle.setText("");
                    }
                    rlayout.addView(iview);
                    rlayout.addView(textstate);
                    //将图片存进容器2
                    linkelaout.addView(rlayout);
                    linkelaout.addView(textTitle);
                    kelaout.addView(linkelaout);
                    index++;
                }
                holder.linearLayout.addView(kelaout);
            }

    }



    @Override
    public int getItemCount() {
        return name.length;
    }

    public  void  run(Context context,ImageView imageView,String url,int w,int h){
        // 因为runOnUiThread是Activity中的方法，Context是它的父类，所以要转换成Activity对象才能使用
          new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(10));
                //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
                GlideLoadUtils.getInstance().glideLoads(context,url,imageView,options,w,h);
            }
        });
    }




    public class Home_FragmentHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        public Home_FragmentHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.home_fragment_videoList);
        }
    }






}

