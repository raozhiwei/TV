package cn.wappt.m.apptv.views.classification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DataInforVidbase;
import cn.wappt.m.apptv.base.SortLateral;
import cn.wappt.m.apptv.interfaces.SortInterfaces;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.RoundedCornersTransformation;
import cn.wappt.m.apptv.utils.Screen_adjustmentUtil;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.details.Videodetails;
import cn.wappt.m.apptv.views.search.SearchActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClassificationActivty extends AppCompatActivity {


    RefreshLayout refreshLayout;
    RecyclerView recyclerView;//外层recyclerview
    List<DataInforVidbase> dataInfor; //存储获取数据
    TextView textView;
    String textViewnaem;
    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    private int HORIZONTAL_VIEW_X = 0;//水平RecyclerView滑动的距离
    List<SortLateral> sort;//分类
    List area;//地区
    List years;//年份
    Handler mHandler;
    Message msg;
    int sortID=1;//分类的id
    int page=0; //页码
    int amount=0;//数量
    int sortname;  //分类id

    String areaname; //地区
    String yearsname; //年份
    int dp=3;
    int   sotrnameId=1;//跳转过来的数据
    int i=0;
    int msgs=0;//选择的地点
    private RecyclerViewAdapter adapter;
    String  preferencesname;
    FloatingActionButton iv_fab;
    ImageView class_zanwuimg;
   ConstraintLayout relativeLayout2;
   ImageView iv_screen_seek; //搜索
    ImageView iv_screen_back; //返回
    //选中记录
    private List<Boolean> sotyisClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private List<Boolean> amountisClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private List<Boolean> yearsisClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        //分类选择的数据
        sort=new ArrayList<SortLateral>();
        area=new ArrayList();
        years=new ArrayList();
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
        //分类的数据
        textView =findViewById(R.id.tv_screen_title);
        refreshLayout = findViewById(R.id.smartrefreshLayout);
        recyclerView=findViewById(R.id.sort_recyclerView);
        iv_fab=findViewById(R.id.iv_fab);
        relativeLayout2=findViewById(R.id.tj_relativeLayout2);
       // class_zanwuimg=findViewById(R.id.class_zanwuimg);
        //获取传入的值
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null) {
            sotrnameId =bundle.getInt("id");
            textViewnaem = bundle.getString("name");
            sortID=bundle.getInt("sortID");
            preferencesname=bundle.getString("preferencesname");
            System.out.println("preferencesname::    "+preferencesname);
            textView.setText(textViewnaem);
        }
        sort.add(0,new SortLateral("分类:",0));
        sort.add(1,new SortLateral("全部",sortID));
        area.add(0,"地区:");
        area.add(1,"全部");

        //设置年份
        years.add(0,"年份:");
        years.add(1,"全部");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        int year= Integer.parseInt(sdf.format(date));
        int index=2;
        for (int j = year; j>=2010 ; j--) {
            String yeardate= String.valueOf(j);
            years.add(index,(yeardate));
            index++;
        }


        switch (sortID){
            case 1:
                area.add(2,"大陆");
                area.add(3,"香港");
                area.add(4,"台湾");
                area.add(5,"美国");
                area.add(6,"法国");
                area.add(7,"英国");
                area.add(8,"日本");
                area.add(9,"韩国");
                area.add(10,"德国");
                area.add(11,"印度");
                area.add(12,"意大利");
                area.add(13,"西班牙");
                area.add(14,"加拿大");
                area.add(15,"其他");
             //   runLisr(1);
                sort.add(new SortLateral("动作片",6));
                sort.add(new SortLateral("喜剧片",7));
                sort.add(new SortLateral("爱情片",8));
                sort.add(new SortLateral("科幻片",9));
                sort.add(new SortLateral("恐怖片",10));
                sort.add(new SortLateral("剧情片",11));
                sort.add(new SortLateral("战争片",12));
                sort.add(new SortLateral("犯罪片",20));
                sort.add(new SortLateral("动画片",21));
                sort.add(new SortLateral("记录片",22));
                sort.add(new SortLateral("悬疑片",56));
                sort.add(new SortLateral("冒险片",57));
                sort.add(new SortLateral("惊悚片",58));
                sort.add(new SortLateral("奇幻片",59));
                sort.add(new SortLateral("灾难片",60));
                sort.add(new SortLateral("武侠片",61));

                break;
            case 2:
                area.add(2,"大陆");
                area.add(3,"韩国");
                area.add(4,"香港");
                area.add(5,"台湾");
                area.add(6,"日本");
                area.add(7,"美国");
                area.add(8,"英国");
                area.add(9,"印度");
                area.add(10,"泰国");
                area.add(11,"新加坡");
                area.add(12,"其他");
                sort.add(new SortLateral("国产剧",13));
                sort.add(new SortLateral("香港剧",14));
                sort.add(new SortLateral("日本剧",15));
                sort.add(new SortLateral("欧美剧",16));
                sort.add(new SortLateral("海外剧",41));
                sort.add(new SortLateral("台湾剧",42));
                sort.add(new SortLateral("韩国剧",43));
                sort.add(new SortLateral("印度剧",44));
                sort.add(new SortLateral("泰国剧",45));
                sort.add(new SortLateral("其他剧",51));
                break;
            case 3:
                area.add(2,"大陆");
                area.add(3,"韩国");
                area.add(4,"香港");
                area.add(5,"台湾");
                area.add(6,"日本");
                area.add(7,"美国");
                area.add(8,"英国");
                area.add(9,"印度");
                area.add(10,"其他");
                sort.add(new SortLateral("大陆综艺",23));
                sort.add(new SortLateral("日韩综艺",24));
                sort.add(new SortLateral("港台综艺",25));
                sort.add(new SortLateral("欧美综艺",26));
                break;
            case 4:
                area.add(2,"中国大陆");
                area.add(3,"日本");
                area.add(4,"美国");
                area.add(5,"英国");
                area.add(6,"其他");
                sort.add(new SortLateral("国产动漫",27));
                sort.add(new SortLateral("日本动漫",28));
                sort.add(new SortLateral("欧美动漫",29));
                sort.add(new SortLateral("海外动漫",30));
                sort.add(new SortLateral("香港动漫",52));
                sort.add(new SortLateral("台湾动漫",53));
                sort.add(new SortLateral("韩国动漫",54));
                sort.add(new SortLateral("其他动漫",55));
                break;
        }

        refreshLayout.setRefreshHeader(new ClassicsHeader(this)); //设置刷新标题
        refreshLayout.setRefreshFooter(new ClassicsFooter(this)); //设置刷新页脚
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {    //在刷新监听器上设置
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (dataInfor!=null &&dataInfor.size()>0){
                    adapter.notifyDataSetChanged();
                    refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                    iv_fab.hide();
                    Toast.makeText(ClassificationActivty.this,"正在刷新中", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ClassificationActivty.this,"刷新失败，数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {   //设置“加载更多侦听器”
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Toast.makeText(ClassificationActivty.this,"正在加载", Toast.LENGTH_SHORT).show();
                page=page+1;
                msgs=0;
                if (Integer.valueOf(sortname).intValue()>5) {
                    Lateralloadzxlx(amount, page, yearsname, dp, Integer.valueOf(sortname).intValue(), areaname, msgs);//获取数据
                }else {
                    Lateralloadzxdl(amount, page, yearsname, dp, Integer.valueOf(sortname).intValue(), areaname, msgs);//获取数据
                }
                iv_fab.hide();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        mHandler=new Handler(){
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                // 根据不同线程发送过来的消息，执行不同的UI操作
                switch (msg.what){
                    case 0:
                        if (dataInfor.size()>0){
                            adapter.notifyDataSetChanged();
                            adapter.notifyItemRemoved(adapter.getItemCount());
                        }else {
                            recyclerView.removeAllViews(); //清空布局容器
                            class_zanwuimg.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 1:
                        i=0;
                        dataInfor=new ArrayList<DataInforVidbase>();
                        if (sortname==0){
                            sortname= sotrnameId;
                        }
                        amount=18;
                        page=1;
                        if (yearsname != null && areaname != null && sortname != 0) {
                            dp = 5;
                        } else if (yearsname != null && sortname != 0 && areaname == null) {
                            dp = 2;
                        } else if (areaname != null && sortname != 0 && yearsname == null) {
                            dp = 4;
                        } else if (sortname != 0) {
                            dp = 3;
                        } else if (areaname != null) {
                            dp = 6;
                        } else if (yearsname != null) {
                            dp = 2;
                        }
                           msgs=3;
                        if (Integer.valueOf(sortname).intValue()>5) {
                            Lateralloadzxlx(amount, page, yearsname, dp, Integer.valueOf(sortname).intValue(), areaname,msgs);//获取数据
                        }else {
                            Lateralloadzxdl(amount, page, yearsname, dp, Integer.valueOf(sortname).intValue(), areaname,msgs);//获取数据
                        }
                        //判断是什么格式查询条件
                        break;
                    case  3:
                        initRecyclerView();
                        break;


                }
            }
        };

            msg =mHandler.obtainMessage();
            msg.what=1;
            // 在工作线程中 通过Handler发送消息到消息队列中
            mHandler.sendMessage(msg);


        basicParamInit();
        iv_screen_seek=findViewById(R.id.iv_screen_seek);
        iv_screen_back=findViewById(R.id.iv_screen_back);

        iv_screen_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                ClassificationActivty.this.finish();
            }
        });
        iv_screen_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索
                Intent intent= new Intent(ClassificationActivty.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获得recyclerView的线性布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //获取到第一个item的显示的下标  不等于0表示第一个item处于不可见状态 说明列表没有滑动到顶部 显示回到顶部按钮
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition == 0) {
                        iv_fab.hide();
                    } else {
                        //显示回到顶部按钮
                        iv_fab.show();
                        iv_fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(0);
                            }
                        });

                    }//获取RecyclerView滑动时候的状态
                }
                else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                    iv_fab.hide();
                }
            }
        });
    }



    /**
     * 计算屏幕的宽度
     */
    private void basicParamInit() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
   /*     int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        relativeLayout2.measure(w, h);
        int height = relativeLayout2.getMeasuredHeight();*/
        screenWidth = metric.widthPixels;
        ViewTreeObserver vto = relativeLayout2.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                relativeLayout2.getHeight();
                screenHeight=(metric.heightPixels-relativeLayout2.getHeight()- Screen_adjustmentUtil.CLASS_SCREEN);
                relativeLayout2.getWidth();
            }
        });
    }



    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        //竖直排列、正向排序
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter  = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    //创建适配器
    private   class  RecyclerViewAdapter extends RecyclerView.Adapter<BaseHolder> {
        //条目样式
        private final int HORIZONTAL_VIEW = 1000;
        private final int GRID_VIEW = 1002;
        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            i=i+1;
            switch (i) {
                case 1:
                    return new SortHorizontalViewHolder(R.layout.recyclerview_top, parent, viewType);
                case 2:
                    return new AreaHorizontalViewHolder(R.layout.recyclerview_top, parent, viewType);
                case 3:
                    return new YearsHorizontalViewHolder(R.layout.recyclerview_top, parent, viewType);
                case 4:
                    return new GridViewHolder(R.layout.recyclerview_top, parent, viewType);
            }
         return null;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            if (holder instanceof SortHorizontalViewHolder) {
                holder.refreshData(sort, position);
            }else if (holder instanceof AreaHorizontalViewHolder){
                holder.refreshData(area, position);
            }else if (holder instanceof YearsHorizontalViewHolder){
                holder.refreshData(years, position);
            } else if (holder instanceof GridViewHolder) {
                holder.refreshData(dataInfor, position);
            }

        }

        @Override
        public int getItemCount() {
            return 4;
        }
        @Override
        /**
         * 当Item被回收时调用此方法
         */
        public void onViewDetachedFromWindow(BaseHolder holder) {
            Log.i("mengyuan", "onViewDetachedFromWindow:" + holder.getClass().toString());
            if (holder instanceof SortHorizontalViewHolder) {
                ((SortHorizontalViewHolder) holder).saveStateWhenDestory();
            }else if (holder instanceof AreaHorizontalViewHolder){
                ((AreaHorizontalViewHolder) holder).saveStateWhenDestory();;
            }else if (holder instanceof YearsHorizontalViewHolder){
                ((YearsHorizontalViewHolder) holder).saveStateWhenDestory();;
            }
        }
          //  获取项目视图类型
        @Override
        public int getItemViewType(int position) {
            if (position == 0) return HORIZONTAL_VIEW;
            if (position == 1) return GRID_VIEW;
            return HORIZONTAL_VIEW;
        }
    }


    //----------------------Holder----------------------------
    /**
     * 嵌套的水平RecyclerView 分类
     *
     */
    private class SortHorizontalViewHolder extends BaseHolder<List<Object>> {
        private RecyclerView hor_recyclerview;
        private List<Object> data;
        private int scrollX;//纪录X移动的距离
        private boolean isLoadLastState = false;//是否加载了之前的状态

        public SortHorizontalViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            hor_recyclerview =  itemView.findViewById(R.id.item_recyclerview_top);
            //为了保存移动距离，所以添加滑动监听
            hor_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //每次条目重新加载时，都会滑动到上次的距离
                    if (!isLoadLastState) {
                        isLoadLastState = true;
                        hor_recyclerview.scrollBy(HORIZONTAL_VIEW_X, 0);
                    }
                    //dx为每一次移动的距离，所以我们需要做累加操作
                    scrollX += dx;
                }
            });

        }
        @Override
        public void refreshData(List data, int position) {
            sotyisClicks=new ArrayList<>();
                this.data = data;
               for(int i = 0;i<data.size();i++){
                   sotyisClicks.add(false);
               }
                //设置水平RecyclerView水平显示
                hor_recyclerview.setLayoutManager(new LinearLayoutManager(ClassificationActivty.this, LinearLayoutManager.HORIZONTAL, false));
                //设置背景
               hor_recyclerview.setBackgroundResource(R.color.colorTabLabnull);
                //设置Adapter
                hor_recyclerview.setAdapter(new HorizontalAdapter());
        }
        /**
         * 在条目回收时调用，保存X轴滑动的距离
         */
        public void saveStateWhenDestory() {
            HORIZONTAL_VIEW_X = scrollX;
            isLoadLastState = false;
            scrollX = 0;
        }

        private class HorizontalAdapter extends RecyclerView.Adapter<BaseHolder> {
            @Override
            public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new SortlateralItemViewHolder(R.layout.item_lateral_sliding, parent, viewType);
            }

            @Override
            public void onBindViewHolder(BaseHolder holder, int position) {
                holder.refreshData(data.get(position), position);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        }

    }

    //----------------------Holder----------------------------
    /**
     * 嵌套的水平RecyclerView 地区
     */
    private class AreaHorizontalViewHolder extends BaseHolder<List<Object>> {
        private List<RecyclerView> recyclerViewList;
        private RecyclerView hor_recyclerview;
        private List<Object> data;
        private int scrollX;//纪录X移动的距离
        private boolean isLoadLastState = false;//是否加载了之前的状态

        public AreaHorizontalViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            hor_recyclerview =  itemView.findViewById(R.id.item_recyclerview_top);
            //为了保存移动距离，所以添加滑动监听
            hor_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //每次条目重新加载时，都会滑动到上次的距离
                    if (!isLoadLastState) {
                        isLoadLastState = true;
                        hor_recyclerview.scrollBy(HORIZONTAL_VIEW_X, 0);
                    }
                    //dx为每一次移动的距离，所以我们需要做累加操作
                    scrollX += dx;
                }
            });
        }
        @Override
        public void refreshData(List data, int position) {
            this.data = data;
            amountisClicks=new ArrayList<>();
            for(int i = 0;i<data.size();i++){
                amountisClicks.add(false);
            }
            //设置水平RecyclerView水平显示
            hor_recyclerview.setLayoutManager(new LinearLayoutManager(ClassificationActivty.this, LinearLayoutManager.HORIZONTAL, false));
            //设置背景
            hor_recyclerview.setBackgroundResource(R.color.colorTabLabnull);
            //设置Adapter
            hor_recyclerview.setAdapter(new HorizontalAdapter());
        }
        /**
         * 在条目回收时调用，保存X轴滑动的距离
         */
        public void saveStateWhenDestory() {
            HORIZONTAL_VIEW_X = scrollX;
            isLoadLastState = false;
            scrollX = 0;
        }

        private class HorizontalAdapter extends RecyclerView.Adapter<BaseHolder> {
            @Override
            public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ArealateralItemViewHolder(R.layout.item_lateral_sliding, parent, viewType);
            }

            @Override
            public void onBindViewHolder(BaseHolder holder, int position) {
                holder.refreshData(data.get(position), position);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        }

    }
    //----------------------Holder----------------------------
    /**
     * 嵌套的水平RecyclerView 年份
     */


    private class YearsHorizontalViewHolder extends BaseHolder<List<Object>> {
        private RecyclerView hor_recyclerview;
        private List<Object> data;

        private int scrollX;//纪录X移动的距离
        private boolean isLoadLastState = false;//是否加载了之前的状态

        public YearsHorizontalViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            hor_recyclerview =  itemView.findViewById(R.id.item_recyclerview_top);
            //为了保存移动距离，所以添加滑动监听
            hor_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //每次条目重新加载时，都会滑动到上次的距离
                    if (!isLoadLastState) {
                        isLoadLastState = true;
                        hor_recyclerview.scrollBy(HORIZONTAL_VIEW_X, 0);
                    }
                    //dx为每一次移动的距离，所以我们需要做累加操作
                    scrollX += dx;
                }
            });

        }
        @Override
        public void refreshData(List data, int position) {
            this.data = data;
            yearsisClicks =new ArrayList<>();
            for (int i = 0; i <data.size() ; i++) {
                yearsisClicks.add(false);
            }
            //设置水平RecyclerView水平显示
            hor_recyclerview.setLayoutManager(new LinearLayoutManager(ClassificationActivty.this, LinearLayoutManager.HORIZONTAL, false));
            hor_recyclerview.layout(0,0,0,0);
            //设置背景
            hor_recyclerview.setBackgroundResource(R.color.colorTabLabnull);
            //设置Adapter
            hor_recyclerview.setAdapter(new HorizontalAdapter());
        }
        /**
         * 在条目回收时调用，保存X轴滑动的距离
         */
        public void saveStateWhenDestory() {
            HORIZONTAL_VIEW_X = scrollX;
            isLoadLastState = false;
            scrollX = 0;
        }

        private class HorizontalAdapter extends RecyclerView.Adapter<BaseHolder> {
            @Override
            public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new YearslateralItemViewHolder(R.layout.item_lateral_sliding, parent, viewType);
            }

            @Override
            public void onBindViewHolder(BaseHolder holder, int position) {
                holder.refreshData(data.get(position), position);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        }

    }


    /**
     * GridView形状的RecyclerView
     */
    private class GridViewHolder extends BaseHolder<List<DataInforVidbase>> {
        private RecyclerView item_recyclerview;
        private ConstraintLayout constraintLayout11;
        private final int ONE_LINE_SHOW_NUMBER = 3;
        private List<DataInforVidbase> data;
        public GridViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            item_recyclerview =itemView.findViewById(R.id.item_recyclerview_top);
            constraintLayout11=itemView.findViewById(R.id.item_constraintlayout_top);
        }

        @Override
        public void refreshData(List<DataInforVidbase> data, int position) {
            super.refreshData(data, position);
            this.data = data;
            if (data.size()<=0){
                item_recyclerview.removeAllViews();
                //创建图像
                ImageView iview = new ImageView(item_recyclerview.getContext());
                LinearLayout.LayoutParams iviewlyout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
                iview.setLayoutParams(iviewlyout);
                iview.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
                iview.setBackgroundResource(R.drawable.zanwu);
                constraintLayout11.addView(iview);
            }else {
                //每行显示4个，水平显示
                item_recyclerview.setLayoutManager(new GridLayoutManager(ClassificationActivty.this, ONE_LINE_SHOW_NUMBER, LinearLayoutManager.VERTICAL, false));
                item_recyclerview.setAdapter(new GridAdapter());
            }

        }
        private class GridAdapter extends RecyclerView.Adapter<BaseHolder> {
            @Override
            public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemViewHolder(R.layout.item_x2_imageview, parent, viewType);
            }

            @Override
            public void onBindViewHolder(BaseHolder holder, int position) {
                holder.refreshData(data.get(position), position);
            }

            @Override
            public int getItemCount() {
                System.out.println("GridViewHolder:dataInfor.size():"+data.size());
                return data.size();

            }
        }
    }
    /**
     * 全部数据
     */
    private class ItemViewHolder extends BaseHolder<DataInforVidbase> {

        private LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        private ImageView imageView;
        private TextView textViews;
        private TextView textupdateView;
        private  int h;
        private  int w;
        public ItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            linearLayout =  itemView.findViewById(R.id.linearLayoutimageview_item);
            ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
            layoutParams.width =screenWidth/3;
            layoutParams.height=screenHeight/4;
            linearLayout.setLayoutParams(layoutParams);
            w=LinearLayout.LayoutParams.MATCH_PARENT;
            h=layoutParams.height-50;
            RelativeLayout.LayoutParams constraintLayoutparms = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    layoutParams.height-50
            );
            relativeLayout=new RelativeLayout(itemView.getContext());
            relativeLayout.setLayoutParams(constraintLayoutparms);
            //生成数据
            imageView=new ImageView(relativeLayout.getContext());
            RelativeLayout.LayoutParams imageLoyout = new RelativeLayout.LayoutParams(
                    constraintLayoutparms.width,
                    constraintLayoutparms.height
            );
            imageView.setLayoutParams(imageLoyout); //给ImageView设置布局参数
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式

            RelativeLayout.LayoutParams textupdateLoyout = new RelativeLayout.LayoutParams(
                    constraintLayoutparms.width,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            textupdateView=new TextView(relativeLayout.getContext());
            textupdateView.setLayoutParams(textupdateLoyout);
             //添加相应的规则
            textupdateLoyout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            textupdateLoyout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            //设置控件的位置

            textupdateView.setBackgroundResource(R.color.textcolorstate);
            textupdateView.setPadding(0,0,14,8);
            textupdateView.setTextSize(11);
            textupdateView.setTextColor(Color.WHITE);
            textupdateView.setGravity(Gravity.RIGHT);
            textupdateView.setSingleLine(true);
            textupdateView.setEllipsize(TextUtils.TruncateAt.END);
            relativeLayout.addView(imageView);
            relativeLayout.addView(textupdateView);
            LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(
                    layoutParams.width,
                    50
            );

            textViews=new TextView(itemView.getContext());
            textViews.setLayoutParams(textLayout);
            linearLayout.addView(relativeLayout);
            linearLayout.addView(textViews);
            FrameLayout.LayoutParams  hint_page_params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            hint_page_params.setMargins(10,0,10, 10);//设置边距
            linearLayout.setLayoutParams(hint_page_params);
        }
        //刷新数据
        @Override
        public void refreshData(final DataInforVidbase data, final int position) {
            // 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
            //顶部左边圆角
            RoundedCornersTransformation transformation = new RoundedCornersTransformation
                    (20, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
            //顶部右边圆角
            RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                    (20, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);
            RoundedCornersTransformation transformatbootonleft= new RoundedCornersTransformation
                    (20, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT);
            RoundedCornersTransformation transformatbottom= new RoundedCornersTransformation
                    (20, 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT);
            //组合各种Transformation,
            MultiTransformation<Bitmap> mation = new MultiTransformation<>
                    //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    (new CenterCrop(), transformation, transformation1,transformatbootonleft,transformatbottom);
            //设置图片圆角角度
            //  RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(10));

            Glide.with(itemView.getContext())
                    .load(data.getVod_pic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.defaultpicture)
                    .apply(RequestOptions.bitmapTransform(mation).override(w,h))
                    .into(imageView);
            if (data.getVod_total()!=null){
                textupdateView.setText(data.getVod_total());
            }else {
                textupdateView.setText(data.getVod_score());
            }
            textViews.setText(data.getVod_name());
            // /设置点击监听器
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(ClassificationActivty.this, Videodetails.class);
                    intent.putExtra("detailsName",data.getVod_name());
                    intent.putExtra("detailsUrl", data.getVod_id());
                    intent.putExtra("detailsImage", data.getVod_pic());
                    startActivity(intent, bundle);
                }
            });
        }
    }

    /**
     * 横向滑动的子条目分类
     */
    private class SortlateralItemViewHolder extends BaseHolder<Object> {
        private TextView textView;
        public SortlateralItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            textView =  itemView.findViewById(R.id.lateral_sliding_id);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            textView.setLayoutParams(layoutParams);
            layoutParams.width = screenWidth/6;
            layoutParams.height =80;

        }
        //刷新数据
        @Override
        public void refreshData(final Object data, final int position) {
            if (data instanceof SortLateral) {
               SortLateral sortLateral = new SortLateral();
                sortLateral= (SortLateral) data;
                    if (sortLateral.getSortId()==sortname){
                        sotyisClicks.set(position,true);
                    }
                    if(sotyisClicks.get(position)){
                        itemView.setBackgroundResource(R.color.colorAccent);
                    }else{
                        itemView.setBackgroundResource(R.color.colorTabLabnull);
                    }
                textView.setText(sortLateral.getSortName());
                final SortLateral finalSortLateral = sortLateral;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalSortLateral.getSortId()!=0) {
                            for (int i = 0; i < sotyisClicks.size(); i++) {
                                sotyisClicks.set(i, false);
                            }
                            sotyisClicks.set(position, true);
                            textView.setBackgroundResource(R.color.colorAccent);
                        }
                        msg=mHandler.obtainMessage();
                         if (position==1) {
                             sortname=sortID;
                             msg.what=1;
                            // 在工作线程中 通过Handler发送消息到消息队列中
                            mHandler.sendMessage(msg);
                        } else if (position>1){
                              sortname=finalSortLateral.getSortId();
                              sotrnameId=finalSortLateral.getSortId();
                              msg.what=1;
                            // 在工作线程中 通过Handler发送消息到消息队列中
                            mHandler.sendMessage(msg);
                            }
                    }
                });
            }
        }
    }




    /**
     * 横向滑动的子条目地区
     */
    private class  ArealateralItemViewHolder  extends BaseHolder<Object> {
        private TextView textView;
        public ArealateralItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            textView =  itemView.findViewById(R.id.lateral_sliding_id);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            textView.setLayoutParams(layoutParams);
            layoutParams.width = screenWidth/6;
            layoutParams.height =80;
        }
        //刷新数据
        @Override
        public void refreshData(final Object data, final int position) {
                textView.setText(data.toString());
                String areas=data.toString();
                if (areas==area.get(1)){
                    areas=null;
                }
             if (areaname==areas){
                 amountisClicks.set(position,true);
              }
            if(amountisClicks.get(position)){
                itemView.setBackgroundResource(R.color.colorAccent);
            }else{
                itemView.setBackgroundResource(R.color.colorTabLabnull);
            }
            // /设置点击监听器
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position>=1){
                            for(int i = 0; i <amountisClicks.size();i++) {
                                amountisClicks.set(i, false);
                            }
                            amountisClicks.set(position,true);
                        }
                        msg=mHandler.obtainMessage();
                        if (position==1) {
                            areaname=null;
                            msg.what=1;
                            // 在工作线程中 通过Handler发送消息到消息队列中
                            mHandler.sendMessage(msg);
                        } else if (position>1){
                            areaname=data.toString();
                            msg.what=1;
                            // 在工作线程中 通过Handler发送消息到消息队列中
                            mHandler.sendMessage(msg);
                        }

                    }
                });

        }
    }
    /**
     * 横向滑动的子条目年份
     */
    private class YearslateralItemViewHolder extends BaseHolder<Object> {
        private TextView textView;
        public YearslateralItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            textView =  itemView.findViewById(R.id.lateral_sliding_id);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            textView.setLayoutParams(layoutParams);
            layoutParams.width = screenWidth/6;
            layoutParams.height =80;
        }
        //刷新数据
        @Override
        public void refreshData(final Object data, final int position) {
            textView.setText(data.toString());
            String year=data.toString();
            if (year==years.get(1)){
                year=null;
            }
            if (yearsname==year){
                yearsisClicks.set(position,true);
            }
            if(yearsisClicks.get(position)){
                itemView.setBackgroundResource(R.color.colorAccent);
            }else{
                itemView.setBackgroundResource(R.color.colorTabLabnull);
            }
                // /设置点击监听器
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position>=1){
                            for(int i = 0; i <yearsisClicks.size();i++) {
                                yearsisClicks.set(i, false);
                            }
                            yearsisClicks.set(position,true);
                        }
                        msg=mHandler.obtainMessage();
                        if (position==1) {
                            yearsname=null;
                            msg.what=1;
                            // 在工作线程中 通过Handler发送消息到消息队列中
                            mHandler.sendMessage(msg);
                        } else if (position>1){
                            yearsname=data.toString();
                            msg.what=1;
                            // 在工作线程中 通过Handler发送消息到消息队列中
                            mHandler.sendMessage(msg);
                        }
                    }
                });
        }
    }


 /*   public  void  runLisr(int sortIDs){
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        RecommendInterdaces request = retrofit.create(RecommendInterdaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.classificationid();
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONObject jsonArray = new JSONObject(jsonStr);
                    JSONArray  jsondata=jsonArray.getJSONArray("class");
                    int index=2;
                    for (int i = 0; i <jsondata.length() ; i++) {
                        JSONObject jsonObject=jsondata.getJSONObject(i);

                        switch (sortIDs){
                            case 1:
                                if (jsonObject.get("type_name").toString().substring(jsonObject.get("type_name").toString().length()-1,jsonObject.get("type_name").toString().length()).equals("片")&&jsonObject.get("type_name").toString()!="电影") {
                                  int type_id= Integer.parseInt(jsonObject.get("type_id").toString());
                                    if (type_id!=sortIDs){
                                        sort.add(index, new SortLateral(jsonObject.get("type_name").toString(),type_id ));  index++;
                                    }
                                }
                                break;
                            case 2:
                                if (jsonObject.get("type_name").toString().substring(jsonObject.get("type_name").toString().length()-1,jsonObject.get("type_name").toString().length()).equals("剧")&&jsonObject.get("type_name").toString()!="连续剧"){
                                    int type_id= Integer.parseInt(jsonObject.get("type_id").toString());
                                    if (type_id!=sortIDs){
                                        sort.add(index, new SortLateral(jsonObject.get("type_name").toString(),type_id ));  index++;
                                    }
                                }
                                break;
                            case 3:
                                if (jsonObject.get("type_name").toString().substring(jsonObject.get("type_name").toString().length()-1,jsonObject.get("type_name").toString().length()).equals("艺")&&jsonObject.get("type_name").toString()!="综艺"){
                                    int type_id= Integer.parseInt(jsonObject.get("type_id").toString());
                                    if (type_id!=sortIDs){
                                        sort.add(index, new SortLateral(jsonObject.get("type_name").toString(),type_id ));  index++;
                                    }
                                }
                                break;
                            case 4:
                                if (jsonObject.get("type_name").toString().substring(jsonObject.get("type_name").toString().length()-1,jsonObject.get("type_name").toString().length()).equals("漫")&&jsonObject.get("type_name").toString()!="动漫"){
                                    int type_id= Integer.parseInt(jsonObject.get("type_id").toString());
                                    if (type_id!=sortIDs){
                                        sort.add(index, new SortLateral(jsonObject.get("type_name").toString(),type_id ));  index++;
                                    }

                                }
                                break;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



         *//*       msg = mHandler.obtainMessage();
                msg.what = 1;
                // 在工作线程中 通过Handler发送消息到消息队列中
                mHandler.sendMessage(msg);*//*
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ClassificationActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/


    //大类加载数据
    public  void   Lateralloadzxdl(int amounts, int pages, String yearsnames, int dps, int sortnames, String areanames, final int msgs) {
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        SortInterfaces request = retrofit.create(SortInterfaces.class);
        //对 发送请求数据进行封装
        System.out.println(amounts+" "+pages+" "+ yearsnames+ " "+ dps+ " "+ sortnames+  " "+areanames);
        Call<ResponseBody> call = request.zxdlsort(amounts, pages,  yearsnames,  dps,  sortnames,  areanames);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        DataInforVidbase dataInforVid=new DataInforVidbase();
                        dataInforVid.setVod_id((String) jsonObject.get("vod_id"));
                        dataInforVid.setVod_name((String) jsonObject.get("vod_name"));
                        dataInforVid.setVod_pic( jsonObject.get("vod_pic").toString().replace("mac", "https"));
                        dataInforVid.setVod_score((String) jsonObject.get("vod_score"));
                        dataInforVid.setVod_total(jsonObject.get("vod_remarks").toString());
                        dataInfor.add(dataInforVid);
                    }

                    if (!(msgs==0)) {
                        msg = mHandler.obtainMessage();
                        msg.what = msgs;
                        // 在工作线程中 通过Handler发送消息到消息队列中
                        mHandler.sendMessage(msg);
                    }
                   if (msgs==0&&adapter!=null){
                        adapter.notifyDataSetChanged();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Toast.makeText(ClassificationActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //小类加载数据
    public  void   Lateralloadzxlx(int amounts, int pages, String yearsnames, int dps, int sortnames, String areanames, final int msgs) {
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        SortInterfaces request = retrofit.create(SortInterfaces.class);
        //对 发送请求数据进行封装
        System.out.println(amounts+" "+pages+" "+ yearsnames+ " "+ dps+ " "+ sortnames+  " "+areanames);
        Call<ResponseBody> call = request.zxlxsort(amounts, pages,  yearsnames,  dps,  sortnames,  areanames);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            @Override//获取成功
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DataInforVidbase dataInforVid = new DataInforVidbase();
                        dataInforVid.setVod_id((String) jsonObject.get("vod_id"));
                        dataInforVid.setVod_name((String) jsonObject.get("vod_name"));
                        dataInforVid.setVod_pic(jsonObject.get("vod_pic").toString().replace("mac", "https"));
                        dataInforVid.setVod_score((String) jsonObject.get("vod_score"));
                        dataInforVid.setVod_total(jsonObject.get("vod_remarks").toString());
                        dataInfor.add(dataInforVid);
                    }
                    if (!(msgs==0)) {
                        msg = mHandler.obtainMessage();
                        msg.what = msgs;
                        // 在工作线程中 通过Handler发送消息到消息队列中
                        mHandler.sendMessage(msg);
                    }
                    if (msgs==0&&adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ClassificationActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
