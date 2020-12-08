package cn.wappt.m.apptv.views.details;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jeffmony.downloader.database.VideoDownloadDatabaseHelper;
import com.jeffmony.downloader.model.VideoTaskItem;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.BrowseRecordsBase;
import cn.wappt.m.apptv.base.CommentBase;
import cn.wappt.m.apptv.base.LoginReceive;
import cn.wappt.m.apptv.base.VideoDetailsutliRecommend;
import cn.wappt.m.apptv.dao.DetailsDao;
import cn.wappt.m.apptv.interfaces.CommentInterfaces;
import cn.wappt.m.apptv.interfaces.VideodetailsInterfaces;
import cn.wappt.m.apptv.utiandent.VideoDetailsutli;
import cn.wappt.m.apptv.utils.GlideLoadUtils;
import cn.wappt.m.apptv.utils.GlideRoundTransform;
import cn.wappt.m.apptv.utils.MessageEvent;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.view.CommentExpandableListView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.content.ContentValues.TAG;

/**
 * @author: wsq
 * @date: 2020/10/10
 * Description:
 */
public class Videodetails extends Activity {

    @BindView(R.id.Details_player)
    VoiewGSYVideoPlayer player;           //播放器
    @BindView(R.id.main_media_frame)
    ConstraintLayout mainMediaFrame;
    @BindView(R.id.player_recommend)
    LinearLayout playerRecommend;
    @BindView(R.id.player_scrollView2)
    ScrollView playerScrollView2;
    @BindView(R.id.line_magicinden)
    MagicIndicator linemagicinden;
    @BindView(R.id.yearen)
    TextView yearen;
    @BindView(R.id.titleen)
    TextView titleen;
    @BindView(R.id.player_Details_download)
    Button player_Details_download;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.stateen)
    TextView stateen;
    @BindView(R.id.score_allen)
    TextView scoreAllen;
    @BindView(R.id.textlike)
    TextView textlike;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.particulars)
    ImageView particulars;
    @BindView(R.id.guideline2)
    Guideline guideline2;
    @BindView(R.id.Hidden)
    TextView Hidden;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.player_magicinden)
    MagicIndicator playerMagicinden;
    @BindView(R.id.player_constrainten)
    ConstraintLayout playerConstrainten;
    @BindView(R.id.player_scrollviewen)
    ScrollView playerScrollviewen;
    @BindView(R.id.progesbaren)
    ProgressBar progesbaren;
    @BindView(R.id.detail_page_lv_comment)
    CommentExpandableListView detailPageLvComment;
    @BindView(R.id.videoDecoder_smartrefreshLayout2)
    SmartRefreshLayout videoDecoderSmartrefreshLayout2;

    //传入的数据值
    String detailsImage;
    String detailsName;
    String detailsUrl;
    //播放器
    SharedPreferences sp;
    private OrientationUtils orientationUtils;  //处理屏幕旋转的的逻辑
    private boolean isPlay;  //播放
    private boolean isPause;  //暂停
    GSYVideoModel urllist;   //存储视频播放地址
    GSYVideoOptionBuilder gsyVideoOption;//视频配置工具类
    //获取的数据


    public VideoDetailsutli videoDetailsutli;   //获取当前的视频
    public List<VideoDetailsutliRecommend> videoDetailsutliRecommends;//获取喜欢的视频

    ArrayList<VideoTaskItem> list;  //进行数据操作下载

    DetailsDao detailsDao; //浏览记录数据库
    int line = 0;//线路

    CommentExpandableListView expandableListView;    //显示评论

    BottomSheetDialog dialog;

    TextView bt_comment;//输入评论

    VideoDownloadDatabaseHelper videoDownloadDatabaseHelper;//下载数据库

    boolean Whether_to_execute = false;

    private List<CommentBase> commentsList;//进行存储评论
    Message msg;
    Handler obtainMessage;


    ///不用查看
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetails);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        //获取传入的值
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        Whether_to_execute = true;
        if (bundle != null) {
            detailsImage = bundle.getString("detailsImage");
            detailsName = bundle.getString("detailsName");
            detailsUrl = bundle.getString("detailsUrl");
            if (detailsUrl.length() > 15 && detailsUrl.substring(0, 1).equals("/")) {
                detailsUrl = detailsUrl.substring(detailsUrl.lastIndexOf("/") + 1, detailsUrl.lastIndexOf("."));
            }
        }
        runVideoRecommendxq(Integer.parseInt(detailsUrl));

        //实例化数据库类
        detailsDao = new DetailsDao(getApplicationContext());
        //进行EventBus的注册
        EventBus.getDefault().register(this);

        player_Details_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diyi(v);
            }
        });
        //评论
        expandableListView = findViewById(R.id.detail_page_lv_comment);
        bt_comment = findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });

        commentsList = new ArrayList<>();
        obtainMessage = new Handler() {
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        initExpandableListView(commentsList);
                        break;
                    case 2:
                        adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        if (videoDetailsutli.getVod_id()!=null){
                            initview();
                            initData();
                            runVideoRecommendlike(videoDetailsutli.getVod_type_id(),"10");
                        }
                        break;
                    case 4:
                            recommend();
                            progesbaren.setVisibility(View.GONE);
                            playerScrollView2.setVisibility(View.VISIBLE);
                            playerScrollviewen.setVisibility(View.VISIBLE); //显示控件
                            Whether_to_execute=true;
                            //取消加载提醒
                            List<BrowseRecordsBase> browseRecordsBases=new ArrayList<>();
                            browseRecordsBases=detailsDao.findByBrowse_index(detailsUrl);
                            if (browseRecordsBases.size()>0){
                                detailsDao.update_index(detailsUrl,"0");
                            }else {
                                detailsDao.add(detailsUrl, detailsName, detailsImage, "0");
                            }

                        break;
                }
            }
        };

        videoDecoderSmartrefreshLayout2.setEnableRefresh(false);//是否启用刷新功能
        videoDecoderSmartrefreshLayout2.setEnableLoadMore(true);//是否启用加载功能
        videoDecoderSmartrefreshLayout2.setRefreshFooter(new ClassicsFooter(this)); //设置刷新页脚
        videoDecoderSmartrefreshLayout2.setOnLoadMoreListener(new OnLoadMoreListener() {   //设置“加载更多侦听器”
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                if (videoDetailsutli.getVod_id()!=null){
                    page++;
                    runComment(sum,page,videoDetailsutli.getVod_id(),2);
                    Toast.makeText(Videodetails.this, "正在加载", Toast.LENGTH_SHORT).show();
                }
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        sp = getSharedPreferences("info", MODE_PRIVATE);
    }

    //进行调用Handler 东西的方法
    public void setmHandler(int what) {
        msg = obtainMessage.obtainMessage();
        msg.what = what;
        // 在工作线程中 通过Handler发送消息到消息队列中
        obtainMessage.sendMessage(msg);
    }

    String [] tap;
    private  void runVideoRecommendxq(int url){
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        VideodetailsInterfaces request = retrofit.create(VideodetailsInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.detail(url);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONObject jsonObject2 = new JSONObject(jsonStr);
                    JSONArray jsonArray=jsonObject2.getJSONArray("list");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    System.out.println(jsonObject);
                    videoDetailsutli=new VideoDetailsutli();
                    videoDetailsutli.setVod_id(jsonObject.getString("vod_id"));
                    videoDetailsutli.setVod_name(jsonObject.getString("vod_name"));
                    videoDetailsutli.setVod_year(jsonObject.getString("vod_year"));
                    videoDetailsutli.setVod_content(jsonObject.getString("vod_content"));
                    videoDetailsutli.setVod_score_all(jsonObject.getString("vod_score"));
                    videoDetailsutli.setVod_state(jsonObject.getString("vod_state"));
                    videoDetailsutli.setVod_hits(jsonObject.getString("vod_hits"));
                    videoDetailsutli.setVod_type_id(jsonObject.getString("type_id"));
                    String url = jsonObject.getString("vod_play_url");        //视频地址
                    String [] testdemo = url.split("\\u0024\\u0024\\u0024");        //不同路// 线的视频
                    String[] [] vod_number_name = new String[testdemo.length][];    //视频地址
                    String[] []  list_anthology = new String[testdemo.length][];    //视频地址

                    String type = jsonObject.getString("vod_play_from");
                    String[] type_of=type.split("\\u0024\\u0024\\u0024");
                    videoDetailsutli.setVod_types_of(type_of);
                    //获取不同线路的视频地址
                    for (int j = 0; j < testdemo.length; j++) {
                        //分割该路线的视频
                        if (testdemo[j].indexOf("#")==-1) {
                            String [] momo = testdemo[j].split("#");
                            list_anthology [j] = new String[1];
                            vod_number_name [j] = new String[1];
                            String [] tap =momo[0].split("\\u0024");
                            vod_number_name [j][0] = tap[0];
                            list_anthology[j][0] = tap[1];
                        }else{
                            String [] momo = testdemo[j].split("#");
                            list_anthology [j] = new String[momo.length];
                            vod_number_name [j] = new String[momo.length];
                            for (int k = 0; k < momo.length; k++) {
                                tap =momo[k].split("\\u0024");
                                if (tap.length>1){
                                    list_anthology[j][k] =tap[1];
                                    vod_number_name[j][k] =tap[0];
                                }

                            }
                        }
                    }
                videoDetailsutli.setVod_number_name(vod_number_name);
                videoDetailsutli.setList_anthology(list_anthology);
                    setmHandler(3);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Videodetails.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });



    }
   String list_index;
   // t	是	string	类别,
  //  s	是	string	数量，值不带默认12条
    private  void runVideoRecommendlike(String t, String s){
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        VideodetailsInterfaces request = retrofit.create(VideodetailsInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.cnxh(t,s);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    videoDetailsutliRecommends=new ArrayList<>();
                    jsonStr = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject object=jsonArray.getJSONObject(i);
                        VideoDetailsutliRecommend recommend=new VideoDetailsutliRecommend();
                        recommend.setList_recommend(object.getString("vod_id"));
                        recommend.setList_videoname(object.getString("vod_name"));
                        recommend.setList_familiar( object.getString("vod_state"));
                        recommend.setList_pic(object.get("vod_pic").toString().replace("mac", "https"));
                        recommend.setList_score_all(object.getString("vod_score"));
                        recommend.setList_hits(object.getString("vod_hits"));
                        String url = object.getString("vod_play_url");        //视频地址
                        if (url != null && url.length()>0) {
                            String [] testdemo = url.split("\\u0024\\u0024\\u0024");  //切割线路
                            //判断是否只有一个视频
                            if (testdemo[0].indexOf("#") == -1) {
                                list_index= "1";
                            }else{
                                String [] kankan = testdemo[0].split("#");
                                list_index = String.valueOf(kankan.length);
                            }
                        }else{
                            list_index = object.getString("vod_play_url");
                        }
                        recommend.setList_index(list_index);
                        videoDetailsutliRecommends.add(recommend);
                    }
                    System.out.println(videoDetailsutliRecommends);
                    setmHandler(4);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Videodetails.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public ObjectAnimator objectAnimator;
    int page=1;
    int sum=50;
    public void initview() {
        //请求数据
        runComment(sum, page, videoDetailsutli.getVod_id(),1);
        //填充内容
        titleen.setText(videoDetailsutli.getVod_name());
        //填充内容
        stateen.setText("状态:" + videoDetailsutli.getVod_state());
        scoreAllen.setText(videoDetailsutli.getVod_score_all() + "分");
        textlike.setText("播放:" + videoDetailsutli.getVod_hits());
        yearen.setText(videoDetailsutli.getVod_year());
        Hidden.setText("\t\t" + videoDetailsutli.getVod_content());
        //隐藏文本域并且不占据空间
        Hidden.setVisibility(View.GONE);
        //调用填充控件方法
        indicatorStyle();
        //绑定详情的监听事件
        particulars.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (Hidden.getVisibility() == 0) {
                    objectAnimator = ObjectAnimator.ofFloat(particulars, "rotation", 90f, 0);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    Hidden.setVisibility(View.GONE); //不可见
                } else {
                    objectAnimator = ObjectAnimator.ofFloat(particulars, "rotation", 0f, 90);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    Hidden.setVisibility(View.VISIBLE); //可见
                }
            }
        });
        videoDownloadDatabaseHelper = new VideoDownloadDatabaseHelper(this);
        list = (ArrayList<VideoTaskItem>) videoDownloadDatabaseHelper.getDownloadInfos();
        indicatorStyleline();
    }

    Pop_Download download;

    //进行下载
    public void diyi(View view) {
        if (videoDetailsutli != null && videoDetailsutliRecommends.size() > 0 && Whether_to_execute) {
            if (download != null) {
                download = null;
            }
            download = new Pop_Download(this, line, videoDetailsutli, videoDetailsutliRecommends, detailsName, detailsImage, detailsUrl, list);
            download.size = videoDetailsutli.getList_anthology()[line].length;
            new XPopup.Builder(this)
                    .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
                    .asCustom(download)
                    .show();
        } else {
            Toast.makeText(Videodetails.this, "数据没有 ", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 创建填充线路
     */
    public void indicatorStyleline() {
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            //getCount数量
            @Override
            public int getCount() {
                return videoDetailsutli.getList_anthology().length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.gray));     //未选中颜色
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.title_text));   //选中颜色
                simplePagerTitleView.setTextSize(12);   //字体大小
                    simplePagerTitleView.setText(videoDetailsutli.getVod_types_of()[index]);    //填充内容
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //处理点击事件将集数传递到播放器上---------关键
                        linemagicinden.onPageScrolled(index, 0, 0);
                        linemagicinden.onPageSelected(index);
                        linemagicinden.onPageScrollStateChanged(0);
                        commonNavigator.onPageScrolled(index, 0, 0);
                        commonNavigator.onPageSelected(index);
                        commonNavigator.onPageScrollStateChanged(0);
                        List<BrowseRecordsBase> browseRecordsBases = new ArrayList<>();
                        browseRecordsBases = detailsDao.findByBrowse_index(detailsUrl);
                        if (browseRecordsBases.size() > 0) {
                            detailsDao.update_index(detailsUrl, String.valueOf(index));
                        } else {
                            detailsDao.add(detailsUrl, detailsName, detailsImage, String.valueOf(index));
                        }
                        line = index;
                        if (videoDetailsutli.getList_anthology()[line].length > 1) {
                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_number_name()[line][0]);
                        } else {
                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_number_name()[line][0]);
                        }
                        for (int i = 0; i <videoDetailsutli.getList_anthology()[line].length ; i++) {
                            System.out.println("线路:"+line+"   "+videoDetailsutli.getList_anthology()[line][i]);
                        }
                         iterate = 0;//执行多少次
                         videoPlay(urllist);
                        indicatorStyle();
                    }
                });
                simplePagerTitleView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                return simplePagerTitleView;
            }

            //LinePagerIndicator 当标题被选中时，标题下方出现一条直线
            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        linemagicinden.setNavigator(commonNavigator);
    }

    /**
     * 创建填充选集
     */
    public void indicatorStyle() {
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            //getCount数量
            @Override
            public int getCount() {
                return videoDetailsutli.getList_anthology()[line].length;
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.gray));     //未选中颜色
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.title_text));   //选中颜色
                simplePagerTitleView.setTextSize(12);   //字体大小
                simplePagerTitleView.setText(videoDetailsutli.getVod_number_name()[line][index]);    //填充内容
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //处理点击事件将集数传递到播放器上---------关键
                        playerMagicinden.onPageScrolled(index, 0, 0);
                        playerMagicinden.onPageSelected(index);
                        playerMagicinden.onPageScrollStateChanged(0);
                        commonNavigator.onPageScrolled(index, 0, 0);
                        commonNavigator.onPageSelected(index);
                        commonNavigator.onPageScrollStateChanged(0);
                        List<BrowseRecordsBase> browseRecordsBases = new ArrayList<>();
                        browseRecordsBases = detailsDao.findByBrowse_index(detailsUrl);
                        if (browseRecordsBases.size() > 0) {
                            detailsDao.update_index(detailsUrl, String.valueOf(index));
                        } else {
                            detailsDao.add(detailsUrl, detailsName, detailsImage, String.valueOf(index));
                        }
                        if (videoDetailsutli.getList_anthology()[line].length > 1) {
                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][index], videoDetailsutli.getVod_number_name()[line][index]);
                        } else {
                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][index], videoDetailsutli.getVod_number_name()[line][index]);
                        }
                        iterate = 0;//执行多少次
                        videoPlay(urllist);
                    }
                });
                simplePagerTitleView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                return simplePagerTitleView;
            }

            //LinePagerIndicator 当标题被选中时，标题下方出现一条直线
            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        playerMagicinden.setNavigator(commonNavigator);
    }

    /**
     * 动态生成推荐视频
     */
    public void recommend() {

        for (int i = 0; i < videoDetailsutliRecommends.size(); i++) {
            //1.添加外部LinerLayout.
            LinearLayout layout = new LinearLayout(this);
            //转换参数类型
            int heightmode = px2dip(this, 100);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
                    heightmode
            );
            layout.setLayoutParams(layoutParams);   //给layout设置布局参数
            layout.setPadding(6, 12, 6, 12);
            layout.setOrientation(LinearLayout.HORIZONTAL); //设置横向布局
            //2.生成内容
            //2.1视频图片
            ImageView imagediss = new ImageView(this);
            heightmode = px2dip(this, 140);
            LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                    heightmode,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            imagediss.setLayoutParams(imageLayout); //给ImageView设置布局参数
            imagediss.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
            run(this, imagediss, videoDetailsutliRecommends.get(i).getList_pic(),heightmode,ViewGroup.LayoutParams.MATCH_PARENT);
            imagediss.setPadding(12, 0, 12, 0);
            layout.addView(imagediss);      //将图片添加进布局
            //2.2加载第二个容器
            LinearLayout layoutSecond = new LinearLayout(this);
            LinearLayout.LayoutParams layoutSecondParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,   //宽度内容占满整行
                    ViewGroup.LayoutParams.MATCH_PARENT    //高度内容自适应
                    , 1      //设置自适应
            );
            layoutSecond.setLayoutParams(layoutSecondParams);
            layoutSecond.setOrientation(LinearLayout.VERTICAL); //设置横向布局
            layout.addView(layoutSecond);
            //2.2.1设置标题
            TextView textTitle = new TextView(this);
            heightmode = px2dip(this, 40);
            LinearLayout.LayoutParams textTitleParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
                    heightmode
            );
            //设置视频标题
            textTitle.setLayoutParams(textTitleParams);
            textTitle.setPadding(px2dip(this, 4), 0, 0, 0);    //设置边距
            textTitle.setTextColor(getResources().getColor(R.color.textCloroMode));      //设置字体颜色
            textTitle.setTextSize(dip2px(this, 5)); //设置字体大小
            textTitle.setText(videoDetailsutliRecommends.get(i).getList_videoname());        //设置内容
            layoutSecond.addView(textTitle);    //将控件添加进容器里
            //设置状态
            TextView textState = new TextView(this);
            LinearLayout.LayoutParams textStateParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textState.setLayoutParams(textStateParams);
            textState.setPadding(px2dip(this, 4), px2dip(this, 17), 0, 0);
            textState.setText(videoDetailsutliRecommends.get(i).getList_familiar());
            textState.setTextSize(dip2px(this, 4));
            layoutSecond.addView(textState);    //将控件添加进容器里
            //2.2.3设置集数和点击数
            TextView textCount = new TextView(this);
            LinearLayout.LayoutParams textCountParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textCount.setLayoutParams(textCountParams);
            textCount.setPadding(px2dip(this, 4), 0, 0, 2);    //设置边距
            textCount.setTextSize(dip2px(this, 4));
            textCount.setText("共" + videoDetailsutliRecommends.get(i).getList_index() + "话\t\t点击:" + videoDetailsutliRecommends.get(i).getList_hits());
            layoutSecond.addView(textCount);
            //2.3设置评分
            TextView textScore = new TextView(this);
            heightmode = px2dip(this, 80);
            LinearLayout.LayoutParams textScoreParams = new LinearLayout.LayoutParams(
                    heightmode,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            textScore.setLayoutParams(textScoreParams);
            textScore.setText(videoDetailsutliRecommends.get(i).getList_score_all());
            textScore.setPadding(px2dip(this, 26), px2dip(this, 12), px2dip(this, 8), 0);    //设置边距
            textScore.setTextSize(dip2px(this, 7));
            textScore.setTextColor(getResources().getColor(R.color.textScore));
            layout.addView(textScore);
            //设置跳转
            final int finalI = i;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Videodetails.this, Videodetails.class);
                    intent.putExtra("detailsName", videoDetailsutliRecommends.get(finalI).getList_videoname()); //视频名称
                    intent.putExtra("detailsUrl", videoDetailsutliRecommends.get(finalI).getList_recommend());
                    intent.putExtra("detailsImage", videoDetailsutliRecommends.get(finalI).getList_pic());//推荐视频图片
                    startActivity(intent);
                }
            });
            playerRecommend.addView(layout);
            //2.4设置分割线
            if (i < videoDetailsutliRecommends.size()) {
                View view = new View(this);
                LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );
                viewParams.setMargins(20, 10, 20, 10);
                view.setLayoutParams(viewParams);
                view.setBackgroundResource(R.drawable.divider);
                playerRecommend.addView(view);
            }
        }
    }

    public void run(Context context, ImageView imageView, String url,int w, int h) {
        // 因为runOnUiThread是Activity中的方法，Context是它的父类，所以要转换成Activity对象才能使用
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(1));
                //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
                GlideLoadUtils.getInstance().glideLoads(Videodetails.this,url,imageView,options,w,h);
            }
        });
    }





    /**
     * 根据手机的分辨率从dp的单位转换成px（像素）
     */
    public static int dip2px(Context context, float dpValue) {
        final float scal = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scal + 0.5f);
    }

    /**
     * 根据手机的分辨率从px（像素）的单位转换成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scal = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scal + 0.5f);
    }


    //视频下载
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void handleData(MessageEvent mMessageEvent) {
        if (mMessageEvent.getVideoTaskItem() != null && mMessageEvent.getVideoTaskItem().getUrl() != null) {
            Toast.makeText(Videodetails.this, "开始下载", Toast.LENGTH_SHORT).show();
        }
    }


    //进行数据初始化
    protected void initData() {
        urllist=null;
        if (videoDetailsutli.getList_anthology()[line].length > 1) {
            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_number_name()[line][0]);
        } else {
            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_number_name()[line][0]);
        }
        initPlayer();
        videoPlay(urllist);
    }


    int iterate = 0;//执行多少次

    // 初始化播放器
    private void initPlayer() {//EXOPlayer内核，支持格式更多
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, player);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        //设置返回键显示
        player.getBackButton().setVisibility(View.VISIBLE);
        gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption
                .setIsTouchWiget(true)     // 是否可以滑动界面改变进度，声音等
                .setRotateViewAuto(false)  //设置旋转视图自动
                .setLockLand(true)
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

                    //播放错误，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
                    @Override
                    public void onPlayError(String url, Object... objects) {
                        if (iterate < 4) {
                            System.out.println("播放错误开始播放");
                            player.startPlayLogic();
                        }
                        System.out.println(iterate);
                        iterate++;
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
        // 全屏按钮
        player.getFullscreenButton().setOnClickListener(v -> {
            //直接横屏
            orientationUtils.resolveByClick();
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            player.startWindowFullscreen(Videodetails.this, true, true);
        });
        //设置返回按键功能
        player.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }
    /**
     * 视频播放
     */
    private void videoPlay(GSYVideoModel list) {

        // 超时时间
        GSYVideoManager.instance().setTimeOut(16 * 1000, true);
        String format = list.getUrl().substring(list.getUrl().lastIndexOf(".") + 1, list.getUrl().length());
        System.out.println(format);
        if (format.equals("mp4")) {
            //ijk关闭log
            IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
            PlayerFactory.setPlayManager(IjkPlayerManager.class);
            CacheFactory.setCacheManager(ProxyCacheManager.class); //代理缓存模式，支持所有模式，不支持m3u8等
            System.out.println("IjkPlayerManager" + list.getUrl());
        } else if (format.equals("m3u8")) {
            PlayerFactory.setPlayManager(Exo2PlayerManager.class);
            System.out.println("Exo2PlayerManager" + list.getUrl());
            CacheFactory.setCacheManager(ExoPlayerCacheManager.class); // exo缓存模式，支持m3u8，只支持exo
        }
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
        //释放所有
        player.setVideoAllCallBack(null);
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
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            player.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }
    CommentExpandAdapter adapter;

    /**
     * 初始化评论和回复列表
     */
    public void initExpandableListView(List<CommentBase> commentsList) {
        expandableListView.setNestedScrollingEnabled(false); //设置启用嵌套滚动
        expandableListView.setGroupIndicator(null); //设置组指标
        adapter  =new CommentExpandAdapter(commentsList,Videodetails.this);
        expandableListView.setAdapter(adapter);

        for(int i = 0; i<commentsList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentsList.get(groupPosition).getComment_id());
                showReplyDialog(groupPosition);
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(Videodetails.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }
    /**
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getComment_name() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {
                    String username = sp.getString("username", "");
                    String password = sp.getString("password", "");
                    if (username != null && password != null && !username.equals("")) {  //登录
                        getcommentPlus(commentContent, videoDetailsutli.getVod_id(), "1", String.valueOf(commentsList.get(position).getComment_id()), username, password,position);
                    } else {    //游客
                        getcommentPlusTourist(commentContent, videoDetailsutli.getVod_id(), "1", String.valueOf(commentsList.get(position).getComment_id()),position);
                    }
                }
                    dialog.dismiss();
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    //弹出全部评论
    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = commentView.findViewById(R.id.dialog_comment_bt); //发布
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {
                    String username = sp.getString("username", "");
                    String password = sp.getString("password", "");
                    String cookis=sp.getString("cookie","");
                    if (username!=null && password!=null && !username.equals("")){  //登录
                        getcommentPlus(commentContent,videoDetailsutli.getVod_id(),"1","0",username,password,0);
                    }else {    //游客
                        getcommentPlusTourist(commentContent,videoDetailsutli.getVod_id(),"1","0",0);
                    }
                    dialog.dismiss();
                    //进行添加上传
                } else {
                    Toast.makeText(Videodetails.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            //  更改文字之前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            //判断是否输入文字进行判断
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    /*
     * 评论加回复（登录）
     * @param content 内容（必须包含中文）
     * @param rid 关联数据id
     * @param mid 模块id，1视频2文字3专题
     * @param pid 数据上级id（评论回复要用到）注：就是评论那条数据id
     * @param name 用户
     * @param pwd 密码
     */
    private void getcommentPlus(String  content,
                                String  rid
            ,  String mid
            , String pid
            , String name
            ,String pwd
    ,int position){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        CommentInterfaces request = retrofit.create(CommentInterfaces.class);
        //对 发送请求数据进行封装
        Call<LoginReceive> call = request.commentPlus(content,rid,mid,pid,name,pwd);
        //发送网络请求(异步)
        call.enqueue(new Callback<LoginReceive>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<LoginReceive> call, Response<LoginReceive> response) {
                LoginReceive receive = response.body();
                System.out.println(receive.getMsg());
                System.out.println(receive.getCode());
                CommentBase detailBean = new CommentBase();
                detailBean.setComment_name(name);
                detailBean.setComment_content(content);
                Date date=new Date();
                detailBean.setComment_time(String.valueOf(date.getTime()));
                detailBean.setComment_rid(rid);
                detailBean.setComment_mid(Integer.parseInt(mid));
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                detailBean.setComment_time(df.format(new Date()));// new Date()为获取当前系统时间
                detailBean.setComment_pid(pid);
                if (pid.equals("0")&& position==0){
                    adapter.addTheCommentData(detailBean);
                }else {
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                }
                Toast.makeText(Videodetails.this,"评论成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginReceive> call, Throwable t) {
                  Toast.makeText(Videodetails.this,"评论加回复（登录)失败",Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     *评论加回复（游客）
     * @param comment_content  内容（必须包含中文）
     * @param comment_rid      关联数据id
     * @param comment_mid     模块id，1视频2文字3专题
     * @param comment_pid     数据上级id（评论回复要用到）注：就是评论那条数据id
     */
    private void getcommentPlusTourist( String  comment_content,
                                 String  comment_rid
            , String comment_mid,String comment_pid,int position){

        //创建retrofit
        Retrofit   retrofit = RetrofitManager.getInstance().getRetrofit();
        CommentInterfaces request = retrofit.create(CommentInterfaces.class);
        //对 发送请求数据进行封装
        Call<LoginReceive> call = request.commentPlusTourist(comment_pid,comment_content,comment_mid,comment_rid);
        //发送网络请求(异步)
        call.enqueue(new Callback<LoginReceive>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<LoginReceive> call, Response<LoginReceive> response) {
                System.out.println(response.body());
                LoginReceive receive = response.body();
                System.out.println(receive.getMsg());
                System.out.println(receive.getCode());
                CommentBase detailBean = new CommentBase();
                detailBean.setComment_name("游客");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                detailBean.setComment_time(df.format(new Date()));// new Date()为获取当前系统时间
                detailBean.setComment_content(comment_content);
                detailBean.setComment_rid(comment_rid);
                detailBean.setComment_mid(Integer.parseInt(comment_mid));
                detailBean.setComment_pid(String.valueOf(comment_pid));

                if (comment_pid.equals("0")&& position==0){
                    adapter.addTheCommentData(detailBean);
                }else {
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                }
                Toast.makeText(Videodetails.this,"评论成功",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<LoginReceive> call, Throwable t) {
                System.out.println(t);
                System.out.println(call.toString());
                Toast.makeText(Videodetails.this,"评论加回复（游客)失败",Toast.LENGTH_SHORT).show();
            }
        });
    }


    //获取评论
    public void runComment(int s, int p, String rid ,int msg) {
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        CommentInterfaces request = retrofit.create(CommentInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.commentList(s, p, rid);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonStr = null;
                JSONArray jsonArray = null;

                List<CommentBase> commentBaselist=new ArrayList<>();
                try {
                    jsonStr = response.body().string();
                    jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CommentBase base = new CommentBase();
                        base.setComment_id(jsonObject.getInt("comment_id"));
                        base.setComment_mid(jsonObject.getInt("comment_mid"));
                        base.setComment_rid(jsonObject.get("comment_rid").toString());
                        base.setComment_pid(jsonObject.get("comment_pid").toString());
                        base.setUser_id(jsonObject.get("user_id").toString());
                        base.setComment_status(jsonObject.get("comment_status").toString());
                        base.setComment_name(jsonObject.get("comment_name").toString());
                        base.setComment_ip(jsonObject.get("comment_ip").toString());
                /*        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        Date date=df.parse(jsonObject.get("comment_time").toString());
                        base.setComment_time(String.valueOf(date));*/
                        long timeStamp = Long.valueOf((String) jsonObject.get("comment_time")) ;  //获取当前时间戳,也可以是你自已给的一个随机的或是别人给你的时间戳(一定是long型的数据)
                        base.setComment_time(stampToTime(timeStamp));
                        base.setComment_content(jsonObject.get("comment_content").toString());
                        base.setComment_up(jsonObject.get("comment_up").toString());
                        base.setComment_down(jsonObject.get("comment_down").toString());
                        base.setComment_reply(jsonObject.get("comment_reply").toString());
                        base.setComment_report(jsonObject.get("comment_report").toString());

                        commentBaselist.add(base);
                    }
                    List<CommentBase> baselist=new ArrayList<>();
                    //先获取子
                  for (int i = 0; i <commentBaselist.size() ; i++) {
                        String id= String.valueOf(commentBaselist.get(i).getComment_id());
                         for (int j = 0; j <commentBaselist.size() ; j++) {
                            if (id.equals(commentBaselist.get(j).getComment_pid())){
                                baselist.add( commentBaselist.get(j));
                            }
                        }
                    }
                  //在获取父
                    for (int i = 0; i <commentBaselist.size() ; i++) {
                        for (int j = 0; j <baselist.size() ; j++) {
                            if (commentBaselist.get(i).equals(baselist.get(j))){
                                commentBaselist.remove(commentBaselist.get(i));

                            }
                        }
                    }
                //获取处理好的
                    for (int i = 0; i <commentBaselist.size() ; i++) {
                        String id= String.valueOf(commentBaselist.get(i).getComment_id());
                        CommentBase commentBase=new CommentBase();
                        List<CommentBase> baselist3=new ArrayList<>();
                        for (int j = 0; j <baselist.size() ; j++) {
                            if (id.equals(baselist.get(j).getComment_pid())&& baselist.get(j).getComment_pid()!=null){
                                System.out.println(baselist.get(j));
                                baselist3.add(baselist.get(j));
                            }
                        }
                        commentBase=commentBaselist.get(i);
                        commentBase.setCommentBases(baselist3);
                        commentsList.add(commentBase);
                    }
                    System.out.println(commentBaselist);
                    setmHandler(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Videodetails.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String stampToTime(long stamp) {
        String time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp*1000);
        time = simpleDateFormat.format(date);
        return time;
    }

}
