package cn.wappt.m.apptv.views.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.utiandent.HttpConStringNew;
import cn.wappt.m.apptv.views.details.Videodetails;

//搜索出来的结果
public class SearchForDetails extends AppCompatActivity implements View.OnClickListener {

    HttpConStringNew hcsn;
    ImageView zanwuimg;
    private TextView textquit;
    private EditText searchdata;
    private ImageView clear;
    private int num = 1;
    private ProgressBar progressBar;
    private ScrollView scrollViewdata;
    private LinearLayout recommend;
    Message msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_details);
        init();
        put();
        handler.postDelayed(runnable,200);     //调用定时器
        handler2.postDelayed(runnable2,200);     //调用定时器
    }

    /**
     * 接收搜索传递过来的参数
     */
    private void put() {
        Intent idm = getIntent();
        hcsn = new HttpConStringNew();
        String title = idm.getStringExtra("searchString");
        hcsn.httpclienttow(title);
        searchdata.setText(title);
    }

    //创建定时器获取基本数据
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            handler.sendMessage(msg);
            handler.postDelayed(this,200); //设置多久刷新一次
        }
    };

    /**
     * 显示搜索结果
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断是否可以显示
            if (hcsn.datamode == 1) {
                zanwuimg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                hcsn.datamode = 0;
            }
            if (hcsn.remarksurl != null) {
                if (hcsn.jsonArray.length()==hcsn.remarksurl.length && hcsn.remarksurl[hcsn.remarksurl.length-1] != null) {
                    progressBar.setVisibility(View.GONE);
                    rostCat();
                    scrollViewdata.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(runnable);      //解除定时器
                }
            }
        }
    };

    //创建定时器获取基本数据
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            handler2.sendMessage(msg);
            handler2.postDelayed(this,200); //设置多久刷新一次
        }
    };

    //检测如果有值则显示清空按钮
    @SuppressLint("HandlerLeak")
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断是否可以显示
            if (searchdata.getText().length()!=0) {
                if (num == 1) {
                    clear.setVisibility(View.VISIBLE);
                    num--;
                }
            }
            if (searchdata.getText().length()==0) {
                if (num == 0) {
                    clear.setVisibility(View.GONE);
                    num++;
                }
            }
        }
    };

    /**
     * 初始化数据
     */
    private void init() {
        zanwuimg = findViewById(R.id.zanwuimg);
        textquit = findViewById(R.id.textquit2);
        searchdata = findViewById(R.id.searchdata2);
        clear = findViewById(R.id.clear2);
        progressBar = findViewById(R.id.progressBar);
        scrollViewdata = findViewById(R.id.scrollViewdata);
        clear.setOnClickListener(this);
        textquit.setOnClickListener(this);
        searchdata.setOnClickListener(this);

        searchdata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //监听回车事件
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String names = searchdata.getText().toString();
                Intent intent=new Intent(SearchForDetails.this,SearchForDetails.class);//传递数据
                intent.putExtra("searchString",names);    //一定要加toString 不然传过去的是地址
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击取消则关闭页面
            case R.id.textquit2:
                //更改成跳转回首页
                finish();
                break;
           /* //点击搜索栏，出现搜索推荐(以后再整)
            case R.id.searchdata:

                break;*/
            case R.id.clear2:
                searchdata.setText("");
                break;
        }
    }

    /**
     * 点击跳转播放视频界面
     */
    Handler handmsg = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(SearchForDetails.this, Videodetails.class);
            intent.putExtra("detailsName",hcsn.titleurl[msg.what]);
            intent.putExtra("detailsUrl","/voddetail/"+hcsn.idurl[msg.what]+".html");
            intent.putExtra("detailsImage",hcsn.imageurl[msg.what]);
            startActivity(intent);
        }
    };

    /**
     * 动态生成视频列表
     */
    public void rostCat(){
        recommend = findViewById(R.id.lineraLayoutmode);
        recommend.removeAllViews(); //清空布局容器

        for (int i = 0; i <hcsn.jsonArray.length() ; i++) {
            //1.添加外部LinerLayout.
            LinearLayout layout = new LinearLayout(this);
            //转换参数类型
            int heightmode = px2dip(this,110);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
                    //ViewGroup.LayoutParams.WRAP_CONTENT    //高度内容自适应
                    heightmode
            );

            layout.setLayoutParams(layoutParams);   //给layout设置布局参数
            layout.setPadding(6,16,6,16);
            layout.setOrientation(LinearLayout.HORIZONTAL); //设置横向布局
            final int index = i;

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg = handmsg.obtainMessage();
                    msg.what = index;
                    handmsg.sendMessage(msg);
                }
            });

            //2.生成内容
            //2.1视频图片
            ImageView imagediss = new ImageView(this);
            heightmode = px2dip(this,90);
            LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                    heightmode,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            imagediss.setLayoutParams(imageLayout); //给ImageView设置布局参数
            imagediss.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
            //设置图片路径
            Glide.with(this)
                    .load(hcsn.imageurl[i])
                    .into(imagediss);
            imagediss.setPadding(12,0,12,0);
            layout.addView(imagediss);      //将图片添加进布局

            //2.2加载第二个容器
            LinearLayout layoutSecond = new LinearLayout(this);
            LinearLayout.LayoutParams layoutSecondParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,   //宽度内容占满整行
                    ViewGroup.LayoutParams.MATCH_PARENT    //高度内容自适应
                    ,1      //设置自适应
            );
            layoutSecond.setLayoutParams(layoutSecondParams);
            layoutSecond.setOrientation(LinearLayout.VERTICAL); //设置横向布局
            layout.addView(layoutSecond);

            //2.2.1设置标题
            TextView textTitle = new TextView(this);
            heightmode = px2dip(this,40);
            LinearLayout.LayoutParams textTitleParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
                    heightmode
            );

            textTitle.setLayoutParams(textTitleParams);
            textTitle.setPadding(px2dip(this,4),px2dip(this,2),0,0);    //设置边距
            textTitle.setTextColor(getResources().getColor(R.color.textCloroMode));      //设置字体颜色
            textTitle.setTextSize(dip2px(this,5)); //设置字体大小
            textTitle.setText(hcsn.titleurl[i]);        //设置内容
            layoutSecond.addView(textTitle);    //将控件添加进容器里

            //2.2.2设置状态
            TextView textState = new TextView(this);
            LinearLayout.LayoutParams textStateParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textState.setLayoutParams(textStateParams);
            textState.setPadding(px2dip(this,4),px2dip(this,22),0,0);
            textState.setText(hcsn.remarksurl[i]);
            textState.setTextSize(dip2px(this,4));
            layoutSecond.addView(textState);    //将控件添加进容器里

            //2.2.3设置集数和点击数
            TextView textCount = new TextView(this);
            LinearLayout.LayoutParams textCountParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textCount.setLayoutParams(textCountParams);
            textCount.setPadding(px2dip(this,4),px2dip(this,6),0,2);    //设置边距
            textCount.setTextSize(dip2px(this,4));
            textCount.setText("共"+hcsn.videourl.length+"话\t\t点击:"+hcsn.playcount[i]);
            layoutSecond.addView(textCount);

            //2.3设置评分
            TextView textScore = new TextView(this);
            heightmode = px2dip(this,80);
            LinearLayout.LayoutParams textScoreParams = new LinearLayout.LayoutParams(
                    heightmode,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            textScore.setLayoutParams(textScoreParams);
            textScore.setText(hcsn.scoreurl[i]);
            textScore.setPadding(px2dip(this,26),px2dip(this,12),px2dip(this,8),0);    //设置边距
            textScore.setTextSize(dip2px(this,8));
            textScore.setTextColor(getResources().getColor(R.color.textScore));
            layout.addView(textScore);
            recommend.addView(layout);

        }
    }


    /**
     *根据手机的分辨率从dp的单位转换成px（像素）
     */
    public static int dip2px(Context context, float dpValue){
        final float scal=context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scal + 0.5f);
    }
    /**
     * 根据手机的分辨率从px（像素）的单位转换成dp
     */
    public static int px2dip(Context context, float pxValue){
        final float scal=context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scal + 0.5f);
    }

}
