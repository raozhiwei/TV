package cn.wappt.m.apptv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import cn.wappt.m.apptv.utils.StatusBarUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btntiaoguo;
    private int reclen = 3;
    private Handler handler;
    Timer timer = new Timer();
    private Runnable runnable;


    /***
     * 程序入口，设置启动界面
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   //     ChoicenessFragment co = new ChoicenessFragment();
    //    co.runjsoup();
        //定义全屏参数

        int falg = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(falg,falg);
        init();

        timer.schedule(task,1000,1000); //等待一秒，停顿一秒
        //不触发点击事件
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Activitv_Home.class);
                startActivity(intent);
                finish();
            }
        },3000);    //延迟3秒后跳转

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

    }
    //初始化变量
    public void init (){
        btntiaoguo = findViewById(R.id.btn_tiaoguo);
        btntiaoguo.setOnClickListener(this);    //跳过监听
    }
    TimerTask task  = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reclen--;
                    btntiaoguo.setText("跳过"+reclen);
                    if (reclen <0) {
                        timer.cancel();
                        btntiaoguo.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    /**
     * 点击跳过
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tiaoguo:
                Intent intent = new Intent(MainActivity.this, Activitv_Home.class);
                startActivity(intent);
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }
    }



}
