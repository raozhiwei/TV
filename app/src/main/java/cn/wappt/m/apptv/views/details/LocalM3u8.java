package cn.wappt.m.apptv.views.details;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.utils.Constants;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * @author: wsq
 * @date: 2020/10/16
 * Description:
 */
public class LocalM3u8 extends AppCompatActivity {

    //简单的Exo播放器存储视频
    int line = Constants.line;//线路

    //将推荐视频id传给接口拿到
    String[] videoid;
    private VoiewGSYVideoPlayer player;           //播放器
    private OrientationUtils orientationUtils;  //处理屏幕旋转的的逻辑
    private boolean isPlay;
    private boolean isPause;  //暂停
    GSYVideoModel list;
    GSYVideoOptionBuilder gsyVideoOption;

    //主线程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localm3u8);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //通过Activity.getIntent()获取当前页面接收到的Intent。
        Intent intent = getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        String m3u8url =intent.getStringExtra("videoUrl");
        list=new GSYVideoModel(m3u8url, "本地视频");
        System.out.println("播放视频的url"+m3u8url);

        player = findViewById(R.id.local_detail_player);
        initData();//进行数据操作
    }

    // 初始化播放器
    private void initPlayer() {
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, player);
        //初始化不打开外部的旋转
    //    orientationUtils.setEnable(false);

        //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
  //      player.startWindowFullscreen(LocalM3u8.this, true, true);

        gsyVideoOption = new GSYVideoOptionBuilder();  //配置工具类 进行配置播放器
        gsyVideoOption
                .setIsTouchWiget(true)     // 是否可以滑动界面改变进度，声音等
                .setRotateViewAuto(false)  //设置旋转视图自动
                .setLockLand(false)
                .setAutoFullWithSize(true) //设置自动满尺寸
                .setShowFullAnimation(false)  //设置显示完整动画
                .setNeedLockFull(true)
                .setCacheWithPlay(false)   //设置带播放缓存
                .setNeedShowWifiTip(true)   //显示流量提示
                .setVideoAllCallBack(new GSYSampleCallBack() {   //设置视频回调
                    @Override  // 开始播放了才能旋转和全屏
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                     orientationUtils.setEnable(true);
                        isPlay = true;
                    }
                    @Override  //全屏退出
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                    // title
                        Log.i("play", "***** onQuitFullscreen **** " + objects[0]);
                        // 当前非全屏player
                        Log.i("play", "***** onQuitFullscreen **** " + objects[1]);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                }).setLockClickListener((view, lock) -> {  //设置锁定点击监听器
            if (orientationUtils != null) {
                //配合下方的onConfigurationChanged
                orientationUtils.setEnable(!lock);
            }
        }).build(player);
        orientationUtils.resolveByClick();
/*        //而不是全屏设置全屏按键功能,这是使用的是选择屏幕，
        player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        //设置返回按键功能
        player.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

/*        // 全屏按钮
        player.getFullscreenButton().setOnClickListener(v -> {
            //直接横屏
            orientationUtils.resolveByClick();
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            player.startWindowFullscreen(LocalM3u8.this, true, true);
        });*/

    }

    //进行数据初始化
    protected void initData() {
        initPlayer();
        videoPlay(list);
    }

    /**
     * 视频播放
     */
    private void videoPlay(GSYVideoModel list) {
        if (list.getUrl().substring(list.getUrl().lastIndexOf(".")+1,list.getUrl().length())=="video"){
            PlayerFactory.setPlayManager(IjkPlayerManager.class);
            System.out.println("IjkPlayerManager"+list.getUrl());
        }else  {
            PlayerFactory.setPlayManager(Exo2PlayerManager.class);
            System.out.println("Exo2PlayerManager"+list.getUrl());
        }

        System.out.println("进行播放视频");
        player.setUp(list.getUrl(), false, list.getTitle());
        player.startPlayLogic();
    }



    // 视频暂停
    private void videoPause() {
        player.getCurrentPlayer().onVideoPause();
        isPause = true;
    }

    // 视频终止
    private void videoStop() {
        GSYVideoManager.releaseAllVideos();
    }


    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        player.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        player.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            player.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            player.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }
}


