//package cn.wappt.m.apptv.views.details;
//
//import android.animation.ObjectAnimator;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.constraintlayout.widget.Guideline;
//
//import com.bumptech.glide.Glide;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.jeffmony.downloader.database.VideoDownloadDatabaseHelper;
//import com.jeffmony.downloader.model.VideoTaskItem;
//import com.lxj.xpopup.XPopup;
//import com.shuyu.gsyvideoplayer.GSYVideoManager;
//import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
//import com.shuyu.gsyvideoplayer.cache.CacheFactory;
//import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
//import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
//import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
//import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
//import com.shuyu.gsyvideoplayer.player.PlayerFactory;
//import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
//
//import net.lucode.hackware.magicindicator.MagicIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.wappt.m.apptv.R;
//import cn.wappt.m.apptv.base.BrowseRecordsBase;
//import cn.wappt.m.apptv.base.CommentBase;
//import cn.wappt.m.apptv.dao.DetailsDao;
//import cn.wappt.m.apptv.interfaces.CommentInterfaces;
//import cn.wappt.m.apptv.utiandent.HttpGettingData;
//import cn.wappt.m.apptv.utiandent.VideoDetailsutli;
//import cn.wappt.m.apptv.utils.DensityUtils;
//import cn.wappt.m.apptv.utils.MessageEvent;
//import cn.wappt.m.apptv.utils.RetrofitManager;
//import cn.wappt.m.apptv.utils.StatusBarUtil;
//import cn.wappt.m.apptv.views.view.CommentExpandableListView;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
//import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//
///**
// * @author: wsq
// * @date: 2020/10/10
// * Description:
// */
//public class Videodetails extends Activity {
//
//    @BindView(R.id.Details_player)
//    VoiewGSYVideoPlayer player;           //播放器
//    @BindView(R.id.main_media_frame)
//    ConstraintLayout mainMediaFrame;
//    @BindView(R.id.player_recommend)
//    LinearLayout playerRecommend;
//    @BindView(R.id.player_scrollView2)
//    ScrollView playerScrollView2;
//
//    @BindView(R.id.line_magicinden)
//    MagicIndicator linemagicinden;
//    @BindView(R.id.yearen)
//    TextView yearen;
//    @BindView(R.id.titleen)
//    TextView titleen;
//    @BindView(R.id.player_Details_download)
//    Button player_Details_download;
//    @BindView(R.id.guideline)
//    Guideline guideline;
//    @BindView(R.id.stateen)
//    TextView stateen;
//    @BindView(R.id.score_allen)
//    TextView scoreAllen;
//    @BindView(R.id.textlike)
//    TextView textlike;
//    @BindView(R.id.relativeLayout)
//    RelativeLayout relativeLayout;
//    @BindView(R.id.particulars)
//    ImageView particulars;
//    @BindView(R.id.guideline2)
//    Guideline guideline2;
//    @BindView(R.id.Hidden)
//    TextView Hidden;
//    @BindView(R.id.imageView2)
//    ImageView imageView2;
//    @BindView(R.id.player_magicinden)
//    MagicIndicator playerMagicinden;
//    @BindView(R.id.player_constrainten)
//    ConstraintLayout playerConstrainten;
//    @BindView(R.id.player_scrollviewen)
//    ScrollView playerScrollviewen;
//    @BindView(R.id.progesbaren)
//    ProgressBar progesbaren;
//    //传入的数据值
//    String detailsImage;
//    String detailsName;
//    String detailsUrl;
//    @BindView(R.id.detail_page_lv_comment)
//    CommentExpandableListView detailPageLvComment;
///*    @BindView(R.id.videododetails_smartrefreshLayout2)
//    SmartRefreshLayout videododetailsSmartrefreshLayout2;*/
//    //播放器
//
//    private OrientationUtils orientationUtils;  //处理屏幕旋转的的逻辑
//    private boolean isPlay;
//    private boolean isPause;  //暂停
//    GSYVideoModel urllist;   //存储视频播放地址
//    GSYVideoOptionBuilder gsyVideoOption;//视频配置工具类
//    //获取的数据
//    public VideoDetailsutli videoDetailsutli;
//    public List<VideoDetailsutli> videoDetailsutliList;
//    //进行数据操作
//    ArrayList<VideoTaskItem> list;
//    DetailsDao detailsDao; //数据库
//    int line = 0;//线路
//    CommentExpandableListView expandableListView;    //显示评论
//    BottomSheetDialog dialog;
//    TextView bt_comment;//输入评论
//    VideoDownloadDatabaseHelper videoDownloadDatabaseHelper;//数据库
//    boolean Whether_to_execute = false;
//    private List<CommentBase> commentsList;//进行存储评论
//    //评论的控件
//
//
//    //创建定时器获取基本数据
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Message msg = Message.obtain();
//            handler.sendMessage(msg);
//            handler.postDelayed(this, 300); //设置多久刷新一次
//        }
//    };
//    Message msg;
//    //定义Handler刷新ui
//    private Handler handler = new Handler() {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (HttpGettingData.getInstance().videoDetailsutli != null && HttpGettingData.getInstance().videoDetailsutliList != null) {
//                videoDetailsutli = new VideoDetailsutli();
//                videoDetailsutliList = new ArrayList<>();
//                videoDetailsutli = HttpGettingData.getInstance().videoDetailsutli;
//                videoDetailsutliList = HttpGettingData.getInstance().videoDetailsutliList;
//
//                if (videoDetailsutliList.size() > 0 && videoDetailsutliList != null) {
//                    System.out.println("执行生成推荐");
//                    //生成推荐
//                    recommend();
//                    Whether_to_execute = true;
//                    //取消加载提醒
//                    List<BrowseRecordsBase> browseRecordsBases = new ArrayList<>();
//                    browseRecordsBases = detailsDao.findByBrowse_index(detailsUrl);
//                    if (browseRecordsBases.size() > 0) {
//                        detailsDao.update_index(detailsUrl, "0");
//                    } else {
//                        detailsDao.add(detailsUrl, detailsName, detailsImage, "0");
//                    }
//                    progesbaren.setVisibility(View.GONE);
//                    playerScrollviewen.setVisibility(View.VISIBLE); //显示控件
//                }
//
//                if (videoDetailsutli.getList_anthology().length > 0) {
//                    //进行数据赋值
//                    initview(line);
//                    //进行视频播放
//                    initData();
//                }
//                handler.removeCallbacks(runnable);      //解除定时器
//            }
//        }
//    };
//
///*    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    initExpandableListView(commentsList);
//                    break;
//            }
//        }
//    };*/
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_videodetails);
//        ButterKnife.bind(this);
//    }
//
//
//    public void initView() {
//        //获取传入的值
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
//        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
//        //设置状态栏透明
//        StatusBarUtil.setTranslucentStatus(this);
//        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
//        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
//        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
//            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
//            //这样半透明+白=灰, 状态栏的文字能看得清
//            StatusBarUtil.setStatusBarColor(this, 0x55000000);
//        }
//
//        Whether_to_execute = true;
//        if (bundle != null) {
//            detailsImage = bundle.getString("detailsImage");
//            detailsName = bundle.getString("detailsName");
//            detailsUrl = bundle.getString("detailsUrl");
//            if (detailsUrl.length() > 15 && detailsUrl.substring(0, 1).equals("/")) {
//                detailsUrl = detailsUrl.substring(detailsUrl.lastIndexOf("/") + 1, detailsUrl.lastIndexOf("."));
//            }
//        }
//        if (HttpGettingData.getInstance().videoData != null || HttpGettingData.getInstance().videoDetailsutli != null
//                || HttpGettingData.getInstance().videoDetailsutliList != null) {
//            HttpGettingData.getInstance().videoData = null;
//            HttpGettingData.getInstance().videoDetailsutli = null;
//            HttpGettingData.getInstance().videoDetailsutliList = null;
//        }
//        HttpGettingData.getInstance().httpPostting(detailsUrl);
//        //实例化数据库类
//        detailsDao = new DetailsDao(getApplicationContext());
//        //进行EventBus的注册
//        EventBus.getDefault().register(this);
//        handler.postDelayed(runnable, 300);     //调用定时器
//        player_Details_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                diyi(v);
//            }
//        });
//        //评论
//        expandableListView = findViewById(R.id.detail_page_lv_comment);
//        bt_comment = findViewById(R.id.detail_page_do_comment);
//        bt_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCommentDialog();
//            }
//        });
//
//        //请求数据
//        runComment(20, 1, "51831");
///*        videododetailsSmartrefreshLayout2.setRefreshFooter(new ClassicsFooter(this)); //设置刷新页脚
//        videododetailsSmartrefreshLayout2.setOnLoadMoreListener(new OnLoadMoreListener() {   //设置“加载更多侦听器”
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                Toast.makeText(Videodetails.this,"正在加载", Toast.LENGTH_SHORT).show();
//
//                refreshlayout.finishLoadMore(2000*//*,false*//*);//传入false表示加载失败
//            }
//        });*/
//
//    }
//
///*    //进行调用Handler 东西的方法
//    public void setmHandler(int what) {
//        msg = mHandler.obtainMessage();
//        msg.what = what;
//        // 在工作线程中 通过Handler发送消息到消息队列中
//        mHandler.sendMessage(msg);
//    }*/
//
//    //弹出评论
//    private void showCommentDialog() {
//        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
//        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
//        final EditText commentText = commentView.findViewById(R.id.dialog_comment_et);
//        final Button bt_comment = commentView.findViewById(R.id.dialog_comment_bt); //发布
//        dialog.setContentView(commentView);
//        bt_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String commentContent = commentText.getText().toString().trim();
//                if (!TextUtils.isEmpty(commentContent)) {
//                    //commentOnWork(commentContent);
//                    dialog.dismiss();
//                    //进行添加上传
//                } else {
//                    Toast.makeText(Videodetails.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        commentText.addTextChangedListener(new TextWatcher() {
//            //  更改文字之前
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            //判断是否输入文字进行判断
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
//                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
//                } else {
//                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        dialog.show();
//    }
//
//
//    public ObjectAnimator objectAnimator;
//
//    public void initview(int Video_line) {
//        //填充内容
//        titleen.setText(videoDetailsutli.getVod_name()[0]);
//        //填充内容
//        stateen.setText("状态:" + videoDetailsutli.getVod_state()[0]);
//        scoreAllen.setText(videoDetailsutli.getVod_score_all()[0] + "分");
//        textlike.setText("播放:" + videoDetailsutli.getVod_hits()[0]);
//        yearen.setText(videoDetailsutli.getVod_year()[0]);
//        Hidden.setText("\t\t" + videoDetailsutli.getVod_content()[0]);
//        //隐藏文本域并且不占据空间
//        Hidden.setVisibility(View.GONE);
//        //调用填充控件方法
//        indicatorStyle();
//        //绑定详情的监听事件
//        particulars.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View v) {
//                if (Hidden.getVisibility() == 0) {
//                    objectAnimator = ObjectAnimator.ofFloat(particulars, "rotation", 90f, 0);
//                    objectAnimator.setDuration(200);
//                    objectAnimator.start();
//                    Hidden.setVisibility(View.GONE); //不可见
//                } else {
//                    objectAnimator = ObjectAnimator.ofFloat(particulars, "rotation", 0f, 90);
//                    objectAnimator.setDuration(200);
//                    objectAnimator.start();
//                    Hidden.setVisibility(View.VISIBLE); //可见
//                }
//            }
//        });
//        videoDownloadDatabaseHelper = new VideoDownloadDatabaseHelper(this);
//        list = (ArrayList<VideoTaskItem>) videoDownloadDatabaseHelper.getDownloadInfos();
//
//        indicatorStyleline(Video_line);
//
//    }
//
//    Pop_Download download;
//
//    //进行下载
//    public void diyi(View view) {
//        if (videoDetailsutli != null && videoDetailsutliList.size() > 0 && Whether_to_execute) {
//            if (download != null) {
//                download = null;
//            }
//            download = new Pop_Download(this, line, videoDetailsutli, videoDetailsutliList, detailsName, detailsImage, detailsUrl, list);
//            download.size = videoDetailsutli.getList_anthology()[line].length;
//            new XPopup.Builder(this)
//                    .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
//                    .asCustom(download)
//                    .show();
//        } else {
//            Toast.makeText(Videodetails.this, "数据没有 ", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    /**
//     * 创建填充线路
//     */
//    public void indicatorStyleline(int Video_line) {
//        final CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            //getCount数量
//            @Override
//            public int getCount() {
//                return videoDetailsutli.getList_anthology().length;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
//                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.gray));     //未选中颜色
//                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.title_text));   //选中颜色
//                simplePagerTitleView.setTextSize(12);   //字体大小
//                if (videoDetailsutli.getList_anthology()[line].length > 1) {
//                    simplePagerTitleView.setText("线路:" + (index + 1));    //填充内容
//                } else {
//                    simplePagerTitleView.setText("线路:" + (index + 1));    //填充内容
//                }
//
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //处理点击事件将集数传递到播放器上---------关键
//                        linemagicinden.onPageScrolled(index, 0, 0);
//                        linemagicinden.onPageSelected(index);
//                        linemagicinden.onPageScrollStateChanged(0);
//                        commonNavigator.onPageScrolled(index, 0, 0);
//                        commonNavigator.onPageSelected(index);
//                        commonNavigator.onPageScrollStateChanged(0);
//                        List<BrowseRecordsBase> browseRecordsBases = new ArrayList<>();
//                        browseRecordsBases = detailsDao.findByBrowse_index(detailsUrl);
//                        if (browseRecordsBases.size() > 0) {
//                            detailsDao.update_index(detailsUrl, String.valueOf(index));
//                        } else {
//                            detailsDao.add(detailsUrl, detailsName, detailsImage, String.valueOf(index));
//                        }
//                        line = index;
//
//                        if (videoDetailsutli.getList_anthology()[line].length > 1) {
//                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_number_name()[line][0]);
//                        } else {
//                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_number_name()[line][0]);
//                        }
//                        videoPlay(urllist);
//                        playerMagicinden.removeAllViews(); //清空布局容器
//                        indicatorStyle();
//                    }
//                });
//                simplePagerTitleView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        return true;
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            //LinePagerIndicator 当标题被选中时，标题下方出现一条直线
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                return null;
//            }
//        });
//        linemagicinden.setNavigator(commonNavigator);
//    }
//
//    /**
//     * 创建填充选集
//     */
//    public void indicatorStyle() {
//        final CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            //getCount数量
//            @Override
//            public int getCount() {
//                return videoDetailsutli.getList_anthology()[line].length;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
//                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.gray));     //未选中颜色
//                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.title_text));   //选中颜色
//                simplePagerTitleView.setTextSize(12);   //字体大小
//                simplePagerTitleView.setText(videoDetailsutli.getVod_number_name()[line][index]);    //填充内容
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //处理点击事件将集数传递到播放器上---------关键
//                        playerMagicinden.onPageScrolled(index, 0, 0);
//                        playerMagicinden.onPageSelected(index);
//                        playerMagicinden.onPageScrollStateChanged(0);
//                        commonNavigator.onPageScrolled(index, 0, 0);
//                        commonNavigator.onPageSelected(index);
//                        commonNavigator.onPageScrollStateChanged(0);
//                        List<BrowseRecordsBase> browseRecordsBases = new ArrayList<>();
//                        browseRecordsBases = detailsDao.findByBrowse_index(detailsUrl);
//                        if (browseRecordsBases.size() > 0) {
//                            detailsDao.update_index(detailsUrl, String.valueOf(index));
//                        } else {
//                            detailsDao.add(detailsUrl, detailsName, detailsImage, String.valueOf(index));
//                        }
//
//                        if (videoDetailsutli.getList_anthology()[line].length > 1) {
//                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][index], videoDetailsutli.getVod_number_name()[line][index]);
//                        } else {
//                            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][index], videoDetailsutli.getVod_number_name()[line][index]);
//                        }
//
//                        videoPlay(urllist);//跳转播放
//                    }
//                });
//                simplePagerTitleView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//
//                        return true;
//                    }
//                });
//                return simplePagerTitleView;
//            }
//
//            //LinePagerIndicator 当标题被选中时，标题下方出现一条直线
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                return null;
//            }
//        });
//        playerMagicinden.setNavigator(commonNavigator);
//    }
//
//    /**
//     * 动态生成推荐视频
//     */
//    public void recommend() {
//        playerRecommend.removeAllViews(); //清空布局容器
//        for (int i = 0; i < videoDetailsutliList.size(); i++) {
//            //1.添加外部LinerLayout.
//            LinearLayout layout = new LinearLayout(this);
//            //转换参数类型
//            int heightmode = DensityUtils.dp2px(this, 100);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
//                    heightmode
//            );
//            layout.setLayoutParams(layoutParams);   //给layout设置布局参数
//            layout.setPadding(6, 12, 6, 12);
//            layout.setOrientation(LinearLayout.HORIZONTAL); //设置横向布局
//            //2.生成内容
//            //2.1视频图片
//            ImageView imagediss = new ImageView(this);
//            heightmode = DensityUtils.dp2px(this, 140);
//            LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
//                    heightmode,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//            );
//            imagediss.setLayoutParams(imageLayout); //给ImageView设置布局参数
//            imagediss.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
//            //设置图片路径
//            if (isDestroy(Videodetails.this)) {
//                Glide.with(this)
//                        .load(videoDetailsutliList.get(i).getList_pic())
//                        .centerCrop()
//                        .placeholder(R.drawable.defaultpicture)
//                        .into(imagediss);
//            } else {
//                Glide.with(this)
//                        .load(videoDetailsutliList.get(i).getList_pic())
//                        .centerCrop()
//                        .placeholder(R.drawable.defaultpicture)
//                        .into(imagediss);
//            }
//
//            imagediss.setPadding(12, 0, 12, 0);
//            layout.addView(imagediss);      //将图片添加进布局
//
//            //2.2加载第二个容器
//            LinearLayout layoutSecond = new LinearLayout(this);
//            LinearLayout.LayoutParams layoutSecondParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,   //宽度内容占满整行
//                    ViewGroup.LayoutParams.MATCH_PARENT    //高度内容自适应
//                    , 1      //设置自适应
//            );
//            layoutSecond.setLayoutParams(layoutSecondParams);
//            layoutSecond.setOrientation(LinearLayout.VERTICAL); //设置横向布局
//            layout.addView(layoutSecond);
//            //2.2.1设置标题
//            TextView textTitle = new TextView(this);
//            heightmode = DensityUtils.dp2px(this, 40);
//            LinearLayout.LayoutParams textTitleParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
//                    heightmode
//            );
//            //设置视频标题
//            textTitle.setLayoutParams(textTitleParams);
//            textTitle.setPadding(DensityUtils.dp2px(this, 4), 0, 0, 0);    //设置边距
//            textTitle.setTextColor(getResources().getColor(R.color.textCloroMode));      //设置字体颜色
//            textTitle.setTextSize(DensityUtils.dp2px(this, 5)); //设置字体大小
//            textTitle.setText(videoDetailsutliList.get(i).getList_videoname());        //设置内容
//            layoutSecond.addView(textTitle);    //将控件添加进容器里
//            //设置状态
//            TextView textState = new TextView(this);
//            LinearLayout.LayoutParams textStateParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            textState.setLayoutParams(textStateParams);
//            textState.setPadding(DensityUtils.dp2px(this, 4), DensityUtils.dp2px(this, 17), 0, 0);
//            textState.setText(videoDetailsutliList.get(i).getList_familiar());
//            textState.setTextSize(DensityUtils.dp2px(this, 4));
//            layoutSecond.addView(textState);    //将控件添加进容器里
//            //2.2.3设置集数和点击数
//            TextView textCount = new TextView(this);
//            LinearLayout.LayoutParams textCountParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            textCount.setLayoutParams(textCountParams);
//            textCount.setPadding(DensityUtils.dp2px(this, 4), 0, 0, 2);    //设置边距
//            textCount.setTextSize(DensityUtils.dp2px(this, 4));
//            textCount.setText("共" + videoDetailsutliList.get(i).getList_index() + "话\t\t点击:" + videoDetailsutliList.get(i).getList_hits());
//            layoutSecond.addView(textCount);
//            //2.3设置评分
//            TextView textScore = new TextView(this);
//            heightmode = DensityUtils.dp2px(this, 80);
//            LinearLayout.LayoutParams textScoreParams = new LinearLayout.LayoutParams(
//                    heightmode,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//            );
//            textScore.setLayoutParams(textScoreParams);
//            textScore.setText(videoDetailsutliList.get(i).getList_score_all());
//            textScore.setPadding(DensityUtils.dp2px(this, 26), DensityUtils.dp2px(this, 12), DensityUtils.dp2px(this, 8), 0);    //设置边距
//            textScore.setTextSize(DensityUtils.dp2px(this, 7));
//            textScore.setTextColor(getResources().getColor(R.color.textScore));
//            layout.addView(textScore);
//            //设置跳转
//            final int finalI = i;
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Videodetails.this, Videodetails.class);
//                    intent.putExtra("detailsName", videoDetailsutliList.get(finalI).getList_videoname()); //视频名称
//                    intent.putExtra("detailsUrl", videoDetailsutliList.get(finalI).getList_recommend());
//                    intent.putExtra("detailsImage", videoDetailsutliList.get(finalI).getList_pic());//推荐视频图片
//                    finish();
//                    startActivity(intent);
//                }
//            });
//            playerRecommend.addView(layout);
//            //2.4设置分割线
//            if (i < videoDetailsutliList.size()) {
//                View view = new View(this);
//                LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        1
//                );
//                viewParams.setMargins(20, 10, 20, 10);
//                view.setLayoutParams(viewParams);
//                view.setBackgroundResource(R.drawable.divider);
//                playerRecommend.addView(view);
//            }
//        }
//    }
//
//
//    public static boolean isDestroy(Activity mActivity) {
//        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    //视频下载
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
//    public void handleData(MessageEvent mMessageEvent) {
//        if (mMessageEvent.getVideoTaskItem() != null && mMessageEvent.getVideoTaskItem().getUrl() != null) {
//            Toast.makeText(Videodetails.this, "开始下载", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    //进行数据初始化
//    protected void initData() {
//        if (videoDetailsutli.getList_anthology()[line].length > 1) {
//            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_name()[0] + "第" + (1) + "集");
//        } else {
//            urllist = new GSYVideoModel(videoDetailsutli.getList_anthology()[line][0], videoDetailsutli.getVod_name()[0]);
//        }
//        initPlayer();
//        videoPlay(urllist);
//    }
//
//
//    int iterate = 0;//执行多少次
//
//    // 初始化播放器
//    private void initPlayer() {//EXOPlayer内核，支持格式更多
//        //外部辅助的旋转，帮助全屏
//        orientationUtils = new OrientationUtils(this, player);
//        //初始化不打开外部的旋转
//        orientationUtils.setEnable(false);
//        //设置返回键显示
//        player.getBackButton().setVisibility(View.VISIBLE);
//        gsyVideoOption = new GSYVideoOptionBuilder();
//        gsyVideoOption
//                .setIsTouchWiget(true)     // 是否可以滑动界面改变进度，声音等
//                .setRotateViewAuto(false)  //设置旋转视图自动
//                .setLockLand(true)
//                .setAutoFullWithSize(true) //设置自动满尺寸
//                .setShowFullAnimation(false)  //设置显示完整动画
//                .setNeedLockFull(true)
//                .setCacheWithPlay(false)   //设置带播放缓存
//                .setNeedShowWifiTip(true)   //显示流量提示
//                .setVideoAllCallBack(new GSYSampleCallBack() {   //设置视频回调
//                    @Override  // 开始播放了才能旋转和全屏
//                    public void onPrepared(String url, Object... objects) {
//                        super.onPrepared(url, objects);
//                        orientationUtils.setEnable(true);
//                        isPlay = true;
//                    }
//
//                    //播放错误，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
//                    @Override
//                    public void onPlayError(String url, Object... objects) {
//                        if (iterate < 3) {
//                            System.out.println("播放错误开始播放");
//                            player.startPlayLogic();
//                        }
//                        System.out.println(iterate);
//                        iterate++;
//                    }
//
//                    @Override  //全屏退出
//                    public void onQuitFullscreen(String url, Object... objects) {
//                        super.onQuitFullscreen(url, objects);
//                        // title
//                        Log.i("play", "***** onQuitFullscreen **** " + objects[0]);
//                        // 当前非全屏player
//                        Log.i("play", "***** onQuitFullscreen **** " + objects[1]);
//                        if (orientationUtils != null) {
//                            orientationUtils.backToProtVideo();
//                        }
//                    }
//                }).setLockClickListener((view, lock) -> {  //设置锁定点击监听器
//            if (orientationUtils != null) {
//                //配合下方的onConfigurationChanged
//                orientationUtils.setEnable(!lock);
//            }
//        }).build(player);
//        // 全屏按钮
//        player.getFullscreenButton().setOnClickListener(v -> {
//            //直接横屏
//            orientationUtils.resolveByClick();
//            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
//            player.startWindowFullscreen(Videodetails.this, true, true);
//        });
//        //设置返回按键功能
//        player.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                finish();
//            }
//        });
//    }
//
//    /**
//     * 视频播放
//     */
//    private void videoPlay(GSYVideoModel list) {
//
//        // 超时时间
//        GSYVideoManager.instance().setTimeOut(16 * 1000, true);
//        String format = list.getUrl().substring(list.getUrl().lastIndexOf(".") + 1, list.getUrl().length());
//        System.out.println(format);
//        if (format.equals("mp4")) {
//            //ijk关闭log
//            IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
//            PlayerFactory.setPlayManager(IjkPlayerManager.class);
//            CacheFactory.setCacheManager(ProxyCacheManager.class); //代理缓存模式，支持所有模式，不支持m3u8等
//            System.out.println("IjkPlayerManager" + list.getUrl());
//        } else if (format.equals("m3u8")) {
//            PlayerFactory.setPlayManager(Exo2PlayerManager.class);
//            System.out.println("Exo2PlayerManager" + list.getUrl());
//            CacheFactory.setCacheManager(ExoPlayerCacheManager.class); // exo缓存模式，支持m3u8，只支持exo
//        }
//        System.out.println(list.getTitle() + "   " + list.getUrl());
//        player.setUp(list.getUrl(), false, list.getTitle());
//        player.startPlayLogic();
//
//    }
//
//
//    // 视频暂停
//    private void videoPause() {
//        player.getCurrentPlayer().onVideoPause();
//        isPause = true;
//    }
//
//    // 视频终止
//    private void videoStop() {
//        GSYVideoManager.releaseAllVideos();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo();
//        }
//        if (GSYVideoManager.backFromWindowFull(this)) {
//            return;
//        }
//        super.onBackPressed();
//        //释放所有
//        player.setVideoAllCallBack(null);
//    }
//
//    @Override
//    protected void onPause() {
//        player.getCurrentPlayer().onVideoPause();
//        super.onPause();
//        isPause = true;
//    }
//
//    @Override
//    protected void onResume() {
//        player.getCurrentPlayer().onVideoResume(false);
//        super.onResume();
//        isPause = false;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isPlay) {
//            player.getCurrentPlayer().release();
//        }
//        if (orientationUtils != null) {
//            orientationUtils.releaseListener();
//        }
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        //如果旋转了就全屏
//        if (isPlay && !isPause) {
//            player.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
//        }
//    }
//
//
//    /**
//     * 初始化评论和回复列表
//     */
//    public void initExpandableListView(List<CommentBase> commentsList) {
//
//
//    }
//
//    ///评论
//    public void comment() {
//
//    }
//
//    //获取轮播图数据
//    public void runComment(int s, int p, String rid) {
//        //进行实例化数据
//        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
//        CommentInterfaces request = retrofit.create(CommentInterfaces.class);
//        //对 发送请求数据进行封装
//        Call<ResponseBody> call = request.commentList(s, p, rid);
//        //发送网络请求(异步)
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override//获取成功
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                String jsonStr = null;
//                JSONArray jsonArray = null;
//                commentsList = new ArrayList<>();
//                try {
//                    jsonStr = response.body().string();
//                    jsonArray = new JSONArray(jsonStr);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        CommentBase base = new CommentBase();
//                        base.setComment_id(jsonObject.getInt("comment_id"));
//                        base.setComment_mid(jsonObject.getInt("comment_mid"));
//                        base.setComment_rid(jsonObject.get("comment_rid").toString());
//                        base.setComment_pid(jsonObject.get("comment_pid").toString());
//                        base.setUser_id(jsonObject.get("user_id").toString());
//                        base.setComment_status(jsonObject.get("comment_status").toString());
//                        base.setComment_name(jsonObject.get("comment_name").toString());
//                        base.setComment_ip(jsonObject.get("comment_ip").toString());
//                        base.setComment_time(jsonObject.get("comment_time").toString());
//                        base.setComment_content(jsonObject.get("comment_content").toString());
//                        base.setComment_up(jsonObject.get("comment_up").toString());
//                        base.setComment_down(jsonObject.get("comment_down").toString());
//                        base.setComment_reply(jsonObject.get("comment_reply").toString());
//                        base.setComment_report(jsonObject.get("comment_report").toString());
//                        commentsList.add(base);
//                    }
//                    //setmHandler(1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override //获取失败
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(Videodetails.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}
