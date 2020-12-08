

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wsq
 * @date: 2020/10/10
 * Description:
 */
public class Videodetailss  extends AppCompatActivity {

 /*   //详情页面的数据
    //创建ui控件
    private TextView titleen;
    private TextView stateen;
    private TextView textlike;
    private TextView Hidden;
    private TextView yearen;
    private ImageView particulars;
    private ScrollView scrollviewen;
    private Button Favoritesbuttonen;
    private TextView score_allen;
    private MagicIndicator magicinden;
    private ProgressBar progesbaren;
    //视频推荐父级容器
    private LinearLayout recommend;
    private String detailsName;//电影名称
    private String detailsImage;//图片
    private String detailsUrl;//地址
    //创建对象
    public VideoDetails video;
    public ObjectAnimator objectAnimator ;
    public HttpClientHome httpClientHome = new HttpClientHome();
    public List listnep = new ArrayList();      //获取集数
    public List listquitll = new ArrayList();   //推荐视频id

    private List<String> list_recommend;    //推荐的id

    //播放器数据
    private final String STATE_RESUME_WINDOW = "resumeWindow";//状态恢复窗口
    private final String STATE_RESUME_POSITION = "resumePosition";//状态恢复位置
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";//状态播放器全屏
    private SimpleExoPlayerView mExoPlayerView;
    private MediaSource[] mVideoSource;  //媒体来源
    private boolean mExoPlayerFullscreen = false; //是否全屏
    private FrameLayout mFullScreenButton; //全屏按钮
    private ImageView mFullScreenIcon; //全屏图标
    private Dialog mFullScreenDialog; //全屏对话框
    SimpleExoPlayer player;;//简单的Exo播放器存储视频
    int line=Constants.line;//线路
    ArrayList url;
    int  i;    //获取当前播放列表id
    private int mResumeWindow;
    private long mResumePosition;
    ConcatenatingMediaSource concatenatedSource;
    //将推荐视频id传给接口拿到
    String [] videoid;
    ArrayList<VideoTaskItem> list;
    DetailsDao detailsDao; //数据库

    //创建定时器获取基本数据
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            handler.sendMessage(msg);
            handler.postDelayed(this,300); //设置多久刷新一次
        }
    };
    //创建定时器获取推荐视频数据
    Runnable runtab = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            hanse.sendMessage(msg);
            hanse.postDelayed(this,300); //设置多久刷新一次
        }
    };
    //创建定时器视频数据
    Runnable videodetails = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            Playhandler.sendMessage(msg);
            Playhandler.postDelayed(this,500); //设置多久刷新一次
        }
    };
    //定义Handler刷新ui
    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (video.getVod_content() !=null) {
                initview();
                handler.removeCallbacks(runnable);      //解除定时器
            }
        }
    };


    //定义Handler调用生成推荐视频方法
    private Handler hanse = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (httpClientHome.imageurl != null && httpClientHome.imageurl.length >0 ) {
                if (httpClientHome.imageurl[0] !=null) {
                    if (httpClientHome.imageurl[httpClientHome.imageurl.length-1] !=null) {
                        recommend();
                        System.out.println("加载视频详情");
                        //取消加载提醒
                        List<BrowseRecordsBase> browseRecordsBases=new ArrayList<>();
                        browseRecordsBases=detailsDao.findByBrowse_index(detailsUrl);
                        if (browseRecordsBases.size()>0){
                            detailsDao.update_index(detailsUrl,"0");
                        }else {
                            detailsDao.add(detailsUrl, detailsName, detailsImage, "0");
                        }
                        progesbaren.setVisibility(View.GONE);
                        scrollviewen.setVisibility(View.VISIBLE); //显示控件
                        hanse.removeCallbacks(runtab);      //解除定时器
                    }
                }
            }
        }
    };

    //定义Handler调用播放视频方法
    private Handler Playhandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (httpClientHome.httpvideomode!=null&& httpClientHome.httpvideomode.length>0) {
                url=new ArrayList();
                for (int j = 0; j < httpClientHome.httpvideomode[line].length; j++) {
                    url.add(httpClientHome.httpvideomode[line][j]);
                }
                System.out.println("结束了：");
                System.out.println(httpClientHome.httpvideomode[line].length);
                System.out.println(url.size());
                System.out.println(url);
                if (url.size() == httpClientHome.httpvideomode[line].length) {
                    Playm3u8();
                    initExoPlayer(concatenatedSource);
                    Playhandler.removeCallbacks(videodetails);      //解除定时器
                }
            }
        }
    };




    private  SharedPreferences sp;
    //主线程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetails);
        video=new VideoDetails();
        this.getSupportActionBar().hide();
        scrollviewen = findViewById(R.id.scrollviewen);
        scrollviewen.setVisibility(View.INVISIBLE); //隐藏控件
        //获取传入的值
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null) {
            detailsImage = bundle.getString("detailsImage");
            detailsName = bundle.getString("detailsName");
            detailsUrl = bundle.getString("detailsUrl");
        }
        System.out.println(detailsUrl);
        //传入地址
        initcount(detailsUrl);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        handler.postDelayed(runnable,300);     //调用定时器
        hanse.postDelayed(runtab,300);     //调用定时器
        Playhandler.postDelayed(videodetails,200);//调用播放定时器
        list=new ArrayList();
        sp = getSharedPreferences("m3u8_list", MODE_PRIVATE);//创建sp对象,如果有key为""的sp就取出

        //实例化数据库类
        detailsDao=new DetailsDao(getApplicationContext());
        //进行EventBus的注册
        EventBus.getDefault().register(this);
    }


    public void initview(){
        //调用接口获取视频地址
        httpClientHome.httpclient(video.getVod_id());
        //初始化控件
        recommend = findViewById(R.id.recommend);
        progesbaren = findViewById(R.id.progesbaren);
        titleen = findViewById(R.id.titleen);
        stateen = findViewById(R.id.stateen);
        Favoritesbuttonen = findViewById(R.id.Favoritesbuttonen);
        score_allen = findViewById(R.id.score_allen);
        textlike = findViewById(R.id.textlike);
        particulars = findViewById(R.id.particulars);
        Hidden = findViewById(R.id.Hidden);
        yearen = findViewById(R.id.yearen);
        magicinden = findViewById(R.id.magicinden);
        //填充内容
        titleen.setText(video.getVod_name());
        stateen.setText("状态:"+video.getVod_state());
        score_allen.setText(video.getVod_score_all()+"分");
        textlike.setText("喜欢:"+video.getVod_like());
        yearen.setText(video.getVod_year());
        Hidden.setText("\t\t"+video.getVod_content());
        //隐藏文本域并且不占据空间
        Hidden.setVisibility(View.GONE);
        //调用填充控件方法
        indicatorStyle();
        //初始化动画
        //将推荐视频id传给接口拿到
        videoid  = new String[video.getList_recommend().size()];
        //将数组传给接口
        for (int i = 0; i <video.getList_recommend().size() ; i++) {
            String [] temporarydata = video.getList_recommend().get(i).split("/");
            videoid[i] = temporarydata[2].substring(0,temporarydata[2].length()-5);
        }
        httpClientHome.httpclienttow(videoid);
        //绑定详情的监听事件
        particulars.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (Hidden.getVisibility() == 0) {
                    objectAnimator = ObjectAnimator.ofFloat(particulars,"rotation",90f,0);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    Hidden.setVisibility(View.GONE); //不可见
                }else{
                    objectAnimator = ObjectAnimator.ofFloat(particulars,"rotation",0f,90);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    Hidden.setVisibility(View.VISIBLE); //可见
                }
            }
        });
        String peopleListJson = sp.getString("MessageEvent", "");  //如果值为空，则将第二个参数作为默认值赋值
        if (peopleListJson != "")  //防空判断
        {
            Gson gson = new Gson();
            System.out.println(gson.fromJson(peopleListJson, new TypeToken<List<MessageEvent>>() {
            }.getType()).toString());
            list = gson.fromJson(peopleListJson, new TypeToken<List<VideoTaskItem>>() {
            }.getType()); //将json字符串转换成List集合
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void handleData(MessageEvent mMessageEvent) {
        Toast.makeText(Videodetails.this, "开始下载", Toast.LENGTH_SHORT).show();
        if (mMessageEvent.getVideoTaskItem()!= null) {
            list.add(mMessageEvent.getVideoTaskItem());
            SharedPreferences.Editor editor = sp.edit();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            System.out.println("视频详情:"+json);
            editor.putString("MessageEvent", json).commit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    *//**
     * 动态生成推荐视频
     *//*
    public void recommend(){
        recommend = findViewById(R.id.recommend);
        recommend.removeAllViews(); //清空布局容器
        list_recommend=new ArrayList<>();    //推荐的id
        //循环遍历推荐视频
        for (int i = video.getList_recommend().size()-1; i > 0 ; i--) {
            //1.添加外部LinerLayout.
            LinearLayout layout = new LinearLayout(this);
            //转换参数类型
            int heightmode = px2dip(this,90);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,   //宽度内容占满整行
                    //ViewGroup.LayoutParams.WRAP_CONTENT    //高度内容自适应
                    heightmode
            );
            layout.setLayoutParams(layoutParams);   //给layout设置布局参数
            layout.setPadding(6,12,6,12);
            layout.setOrientation(LinearLayout.HORIZONTAL); //设置横向布局

            //2.生成内容
            //2.1视频图片
            ImageView imagediss = new ImageView(this);
            heightmode = px2dip(this,140);
            LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                    heightmode,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            imagediss.setLayoutParams(imageLayout); //给ImageView设置布局参数
            imagediss.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
            //设置图片路径
            Glide.with(this)
                    .load(httpClientHome.imageurl[i])
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
            textTitle.setPadding(px2dip(this,4),0,0,0);    //设置边距
            textTitle.setTextColor(getResources().getColor(R.color.textCloroMode));      //设置字体颜色
            textTitle.setTextSize(dip2px(this,5)); //设置字体大小
            textTitle.setText(httpClientHome.title[i]);        //设置内容
            layoutSecond.addView(textTitle);    //将控件添加进容器里

            //2.2.2设置状态
            TextView textState = new TextView(this);
            LinearLayout.LayoutParams textStateParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textState.setLayoutParams(textStateParams);
            textState.setPadding(px2dip(this,4),px2dip(this,7),0,0);
            textState.setText(httpClientHome.remarks[i]);
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
            textCount.setText("共"+httpClientHome.number[i]+"话\t\t点击:"+httpClientHome.count[i]);
            layoutSecond.addView(textCount);

            //2.3设置评分
            TextView textScore = new TextView(this);
            heightmode = px2dip(this,80);
            LinearLayout.LayoutParams textScoreParams = new LinearLayout.LayoutParams(
                    heightmode,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            textScore.setLayoutParams(textScoreParams);
            textScore.setText(httpClientHome.score[i]);
            textScore.setPadding(px2dip(this,26),px2dip(this,12),px2dip(this,8),0);    //设置边距
            textScore.setTextSize(dip2px(this,7));
            textScore.setTextColor(getResources().getColor(R.color.textScore));
            layout.addView(textScore);
            //设置跳转
            final int finalI = i;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Videodetails.this, Videodetails.class);
                    list_recommend=video.getList_recommend();
                    intent.putExtra("detailsName", httpClientHome.title[finalI]); //视频名称
                    intent.putExtra("detailsUrl", Constants.detailsHttp+httpClientHome.id[finalI]+".html");
                    intent.putExtra("detailsImage", httpClientHome.imageurl[finalI]);//推荐视频图片
                    startActivity(intent);
                }
            });

            recommend.addView(layout);

            //2.4设置分割线
            if (i < video.getList_recommend().size()) {
                View view = new View(this);
                LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );
                viewParams.setMargins(20,10,20,10);
                view.setLayoutParams(viewParams);
                view.setBackgroundResource(R.drawable.divider);
                recommend.addView(view);
            }
        }
    }

    *//**
     *根据手机的分辨率从dp的单位转换成px（像素）
     *//*
    public static int dip2px(Context context, float dpValue){
        final float scal=context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scal + 0.5f);
    }
    *//**
     * 根据手机的分辨率从px（像素）的单位转换成dp
     *//*
    public static int px2dip(Context context,float pxValue){
        final float scal=context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scal + 0.5f);
    }


    *//**
     * 创建填充选集
     *//*
    public void indicatorStyle(){
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            //getCount数量
            @Override
            public int getCount() {
                return video.getList_anthology().size();
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.gray));     //未选中颜色
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.colorPrimary));   //选中颜色
                simplePagerTitleView.setTextSize(12);   //字体大小
                simplePagerTitleView.setText("第"+(index+1)+"集");    //填充内容
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //处理点击事件将集数传递到播放器上---------关键
                        magicinden.onPageScrolled(index, 0, 0);
                        magicinden.onPageSelected(index);
                        magicinden.onPageScrollStateChanged(0);
                        commonNavigator.onPageScrolled(index, 0, 0);
                        commonNavigator.onPageSelected(index);
                        commonNavigator.onPageScrollStateChanged(0);
                        List<BrowseRecordsBase> browseRecordsBases=new ArrayList<>();
                        browseRecordsBases=detailsDao.findByBrowse_index(detailsUrl);
                        if (browseRecordsBases.size()>0){
                            detailsDao.update_index(detailsUrl,String.valueOf(index));
                        }else {
                            detailsDao.add(detailsUrl, detailsName, detailsImage, String.valueOf(index));
                        }
                        mExoPlayerView.getPlayer().seekToDefaultPosition(index);//跳转
                    }
                });
                simplePagerTitleView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Videodetails.this);
                        builder.setMessage("确定下载?");
                        builder.setTitle("提示");
                        //添加AlertDialog.Builder对象的setPositiveButton()方法
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean correct=true;
                                String detailname="";
                                if ( video.getList_anthology().size()>0) {
                                    detailname =detailsName+"第"+(index+1)+"集";
                                }else {
                                    detailname=detailsName;
                                }
                                //获取数据
                                String peopleListJson = sp.getString("MessageEvent", "");  //如果值为空，则将第二个参数作为默认值赋值
                                if (peopleListJson != "")  //防空判断
                                {
                                    Gson gson = new Gson();

                                    List<VideoTaskItem> items = gson.fromJson(peopleListJson, new TypeToken<List<VideoTaskItem>>() {
                                    }.getType()); //将json字符串转换成List集合
                                    for (int i = 0; i <items.size() ; i++) {
                                        System.out.println(items.get(i));
                                        System.out.println(detailname);
                                        if (items.get(i).getFileName().equals(detailname))
                                        {
                                            correct=false;
                                            break;
                                        }
                                    }
                                }
                                if (list!=null){
                                    if (correct){
                                        //存储数据
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(detailname, detailsImage).commit();
                                        Intent intent=new Intent(Videodetails.this, DownVideoService.class);
                                        intent.putExtra("name",detailname);
                                        intent.putExtra("url",url.get(index).toString());
                                        intent.putExtra("Imageurl",detailsImage);
                                        //发送数据给service
                                        startService(intent); //启动服务
                                    }else {
                                        Toast.makeText(Videodetails.this, "已经在下载列表", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    if (correct){
                                        //存储数据
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(detailname, detailsImage).commit();
                                        Intent intent=new Intent(Videodetails.this, DownVideoService.class);
                                        intent.putExtra("name",detailname);
                                        intent.putExtra("url",url.get(index).toString());
                                        intent.putExtra("Imageurl",detailsImage);
                                        //发送数据给service
                                        startService(intent); //启动服务
                                    }else {
                                        Toast.makeText(Videodetails.this, "已经在下载列表", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                *//*   Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();*//*
                            }
                        });
                        //添加AlertDialog.Builder对象的setNegativeButton()方法
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<BrowseRecordsBase> browseRecordsBases= detailsDao.findByBrowse();
                                System.out.println(browseRecordsBases.size());
                                for (int j = 0; j <browseRecordsBases.size() ; j++) {
                                    System.out.println(browseRecordsBases.get(j));
                                }
                            }
                        });
                        builder.create().show();
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
        magicinden.setNavigator(commonNavigator);
    }



    *//**
     * 根据视频地址查询视频详情
     * @param url
     *//*
    public void initcount(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                video = new VideoDetails();
                StringBuilder sb;
                Document document = null;
                try {
                    //获取视频id
                    document = Jsoup.connect(Constants.AppUrl+url).timeout(80000).get();
                    Elements videoid = document.select("div.content_thumb.fl");    //解析
                    if (videoid.size() != 0) {
                        String [] id = videoid.select("a").attr("href").split("/");
                        video.setVod_id(id[2].substring(0,id[2].length()-9));               //截取id
                        video.setVod_name(videoid.select("a").attr("title"));  //视频名
                    }
                    //获取评分
                    Elements videoscore_all = document.select("div#rating.fn-left");
                    //非空验证
                    if (videoscore_all.select("span").size() == 0) {
                        video.setVod_score_all("0.0");
                    }else {
                        video.setVod_score_all(videoscore_all.select("span").text());       //添加评分
                    }

                    //获取年份地区和类型
                    Elements videoclass = document.select("div.content_detail.content_min.fl").select("li").eq(0);
                    if (videoclass.size() != 0) {
                        video.setVod_year(videoclass.select("a").get(0).text());//年份
                    }

                    //获取状态
                    Elements videostate = document.select("div.content_detail.content_min.fl").select("li").eq(1);
                    if (videoclass.select("span").size()>0) {
                        String vstate = videostate.select("span").get(1).text();
                        if (vstate != null) {
                            if (vstate.equals("超清") || vstate.equals("完结") || vstate.equals("蓝光")) {
                                video.setVod_state("已完结");
                            } else {
                                video.setVod_state("连载中");
                            }
                        } else {
                            video.setVod_state("未知");
                        }
                    }
                    //获取喜欢次数
                    Elements videolike = document.select("div.playbtn.o_like").select("a");
                    video.setVod_like(videolike.select("em").text());

                    //获取集数
                    Elements videolistnep = document.select("div.playlist_full").select("ul").eq(0).select("li");
                    if (videolistnep.size() != 0) {
                        String impot;
                        for (int i = 0; i <videolistnep.size() ; i++) {
                            listnep.add( videolistnep.select("li").select("a").get(i).text());
                        }
                        video.setList_anthology(listnep);
                    }else{
                        listnep.add("暂无");
                    }

                    //获取简介
                    Elements videocontent = document.select("div.content_desc.full_text.clearfix");
                    if (videocontent.size() != 0) {
                        video.setVod_content(videocontent.select("span").text());
                    }

                    //获取推荐列表信息
                    Elements videovideoid = document.select("ul.vodlist.vodlist_sh.list_scroll.clearfix").select("li");
                    if (videovideoid.size()!=0) {
                        for (int i = 0; i <videovideoid.size() ; i++) {
                            listquitll.add(videovideoid.get(i).select("a").attr("href"));
                        }
                    }
                    video.setList_recommend(listquitll);    //添加推荐视频id
                    if (video.getVod_id()!=null) {
                        httpClientHome.httpclient(video.getVod_id());
                    }
                    //输出推荐列表数据
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //播放器
    //在保存实例状态
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }


    //初始化全屏对话框
    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) { //使用主题
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    ///打开全屏对话框
    private void openFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView); //删除当前视图
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)); //添加内容视图
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Videodetails.this, R.drawable.ic_fullscreen_skrink)); //设置图像可绘制
        mExoPlayerFullscreen = true; //设置为全屏
        mFullScreenDialog.show(); //显示
    }


    //关闭全屏对话框
    private void closeFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((RelativeLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView); //添加之前的view
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss(); //全屏对话框注销
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Videodetails.this, R.drawable.ic_fullscreen_expand));
    }

    //初始化全屏按钮
    private void initFullscreenButton() {
        mFullScreenIcon = findViewById(R.id.exo_fullscreen_icon);//获取全屏图标
        mFullScreenButton = findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen) {//判断是否全屏
                    openFullscreenDialog();//打开全屏对话框
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
                } else {
                    closeFullscreenDialog(); //关闭全屏对话框
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                }
            }
        });
    }
    //初始化ExoPlayer
    private void initExoPlayer(ConcatenatingMediaSource concatenatedSource) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);
        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }
        player.prepare(concatenatedSource);//把视频放进去
        // mExoPlayerView.setPlayer();//.prepare(mVideoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }
    //播放m3u8
    public  void Playm3u8() {
        concatenatedSource = null;
        mVideoSource = new MediaSource[url.size()];
        if (mExoPlayerView == null) {
            mExoPlayerView = findViewById(R.id.exoplayer); //获取播放控件
            initFullscreenDialog();//初始化全屏对话框
            initFullscreenButton();//初始化全屏按钮
        }
        for (int i = 0; i < url.size(); i++) {
            String userAgent = Util.getUserAgent(Videodetails.this, getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(Videodetails.this, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(url.get(i).toString());//转换我URI类型
            System.out.println(url.get(i));
            if ("m3u8".equals(url.get(i).toString().substring(url.get(i).toString().lastIndexOf(".")+1))){
                mVideoSource[i] = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(daUri);

            }else {
                mVideoSource[i]=new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(daUri);
            }
            //先播放第一个视频，再播放第二个视频
            if (mExoPlayerFullscreen) {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Videodetails.this, R.drawable.ic_fullscreen_skrink));
                mFullScreenDialog.show();
            }
        }
        concatenatedSource = new ConcatenatingMediaSource(mVideoSource);
    }
    //进行时
    @Override
    protected void onResume() {
        if (url!=null){
            Playm3u8();
            initExoPlayer(concatenatedSource);
        }
        super.onResume();
    }
    //暂停
    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
            mExoPlayerView.getPlayer().release();
        }
        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }*/
}
