/*
package cn.wappt.m.apptv.views.classification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DataInforVidbase;
import cn.wappt.m.apptv.base.SortLateral;
import cn.wappt.m.apptv.interfaces.SortInterfaces;
import cn.wappt.m.apptv.utils.Constants;
import cn.wappt.m.apptv.utils.GlideRoundTransform;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.SavecategoryUtils;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.details.Videodetails;
import cn.wappt.m.apptv.views.search.SearchActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClassificationActivtymy extends AppCompatActivity {


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
        years.add(0,"年份:");
        years.add(1,"全部");

        refreshLayout.setRefreshHeader(new ClassicsHeader(this)); //设置刷新标题
        refreshLayout.setRefreshFooter(new ClassicsFooter(this)); //设置刷新页脚
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {    //在刷新监听器上设置
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(2000*/
/*,false*//*
);//传入false表示刷新失败
                iv_fab.hide();
                Toast.makeText(ClassificationActivtymy.this,"正在刷新中", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {   //设置“加载更多侦听器”
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Toast.makeText(ClassificationActivtymy.this,"正在加载", Toast.LENGTH_SHORT).show();
                page=page+1;
                msg =mHandler.obtainMessage();
                msg.what=2;
                // 在工作线程中 通过Handler发送消息到消息队列中
                mHandler.sendMessage(msg);
                iv_fab.hide();
                refreshlayout.finishLoadMore(2000*/
/*,false*//*
);//传入false表示加载失败
            }
        });


        mHandler=new Handler(){
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                // 根据不同线程发送过来的消息，执行不同的UI操作
                switch (msg.what){
                    case 0:
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(adapter.getItemCount());
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
                    case 2:
                        msgs=0;
                        if (Integer.valueOf(sortname).intValue()>5) {
                            Lateralloadzxlx(amount, page, yearsname, dp, Integer.valueOf(sortname).intValue(), areaname, msgs);//获取数据
                        }else {
                            Lateralloadzxdl(amount, page, yearsname, dp, Integer.valueOf(sortname).intValue(), areaname, msgs);//获取数据
                        }
                        break;
                    case  3:
                        initRecyclerView();
                        break;

                }
            }
        };
        if (bool(preferencesname)) {
          runList();
        }else {
            msg =mHandler.obtainMessage();
            msg.what=1;
            // 在工作线程中 通过Handler发送消息到消息队列中
            mHandler.sendMessage(msg);
         //   adapter.notifyDataSetChanged();
        }
        basicParamInit();
        iv_screen_seek=findViewById(R.id.iv_screen_seek);
        iv_screen_back=findViewById(R.id.iv_screen_back);

        iv_screen_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                ClassificationActivtymy.this.finish();
            }
        });
        iv_screen_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索
                Intent intent= new Intent(ClassificationActivtymy.this, SearchActivity.class);
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



    */
/**
     * 计算屏幕的宽度
     *//*

    private void basicParamInit() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight=metric.heightPixels;
    }



    */
/**
     * 初始化RecyclerView
     *//*

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
        */
/**
         * 当Item被回收时调用此方法
         *//*

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
    */
/**
     * 嵌套的水平RecyclerView 分类
     *
     *//*

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
                hor_recyclerview.setLayoutManager(new LinearLayoutManager(ClassificationActivtymy.this, LinearLayoutManager.HORIZONTAL, false));
                //设置背景
               hor_recyclerview.setBackgroundResource(R.color.colorTabLabnull);
                //设置Adapter
                hor_recyclerview.setAdapter(new HorizontalAdapter());
        }
        */
/**
         * 在条目回收时调用，保存X轴滑动的距离
         *//*

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
    */
/**
     * 嵌套的水平RecyclerView 地区
     *//*

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
            hor_recyclerview.setLayoutManager(new LinearLayoutManager(ClassificationActivtymy.this, LinearLayoutManager.HORIZONTAL, false));
            //设置背景
            hor_recyclerview.setBackgroundResource(R.color.colorTabLabnull);
            //设置Adapter
            hor_recyclerview.setAdapter(new HorizontalAdapter());
        }
        */
/**
         * 在条目回收时调用，保存X轴滑动的距离
         *//*

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
    */
/**
     * 嵌套的水平RecyclerView 年份
     *//*



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
            hor_recyclerview.setLayoutManager(new LinearLayoutManager(ClassificationActivtymy.this, LinearLayoutManager.HORIZONTAL, false));
            hor_recyclerview.layout(0,0,0,0);
            //设置背景
            hor_recyclerview.setBackgroundResource(R.color.colorTabLabnull);
            //设置Adapter
            hor_recyclerview.setAdapter(new HorizontalAdapter());
        }
        */
/**
         * 在条目回收时调用，保存X轴滑动的距离
         *//*

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


    */
/**
     * GridView形状的RecyclerView
     *//*

    private class GridViewHolder extends BaseHolder<List<DataInforVidbase>> {
        private RecyclerView item_recyclerview;
        private final int ONE_LINE_SHOW_NUMBER = 3;
        private List<DataInforVidbase> data;
        public GridViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            item_recyclerview =  itemView.findViewById(R.id.item_recyclerview_top);
        }

        @Override
        public void refreshData(List<DataInforVidbase> data, int position) {
            super.refreshData(data, position);
            this.data = data;
            //每行显示4个，水平显示
            item_recyclerview.setLayoutManager(new GridLayoutManager(ClassificationActivtymy.this, ONE_LINE_SHOW_NUMBER, LinearLayoutManager.VERTICAL, false));
            //设置Adapter
            item_recyclerview.setAdapter(new GridAdapter());
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
    */
/**
     * 全部数据
     *//*

    private class ItemViewHolder extends BaseHolder<DataInforVidbase> {


        private ConstraintLayout constraintLayout;
        private ImageView imageView;
        private TextView textupdate;
        private TextView textname;
        public ItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            constraintLayout=itemView.findViewById(R.id.imageview_ConstraintLayout);

            ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
            layoutParams.width = screenWidth /3;
            layoutParams.height = screenHeight/3;
            constraintLayout.setLayoutParams(layoutParams);

            //生成数据
            imageView=new ImageView(itemView.getContext());
            LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                    layoutParams.width,
                    layoutParams.height-50
            );
            imageView.setLayoutParams(imageLayout); //给ImageView设置布局参数
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
            LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(
                    layoutParams.width,
                    50
            );
            textname=new TextView(itemView.getContext());
            textname.setLayoutParams(textLayout);
            constraintLayout.addView(imageView);
            constraintLayout.addView(textname);
            FrameLayout.LayoutParams  hint_page_params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            hint_page_params.setMargins(10,0,10, 10);//设置边距
            constraintLayout.setLayoutParams(hint_page_params);
            textupdate=new TextView(itemView.getContext());

*/
/*            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();

            itemView.setLayoutParams(layoutParams);

            textname=itemView.findViewById(R.id.imageview_item_name);
            imageView=itemView.findViewById(R.id.imageview_item);
            textupdate=itemView.findViewById(R.id.imageview_item_Update);*//*


*/
/*  <TextView
            android:id="@+id/imageview_item_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="TextView"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent" />

    <ImageView
            android:id="@+id/imageview_item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toTopOf="@+id/imageview_item_name"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/defaultpicture" />

    <TextView
            android:id="@+id/imageview_item_Update"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="10dp"
            android:text="TextView"
            app:layout_constraintBottom_toBottomOf="@+id/imageview_item"
            app:layout_constraintEnd_toEndOf="@+id/imageview_item" />*//*





    */
/*
/*  <TextView
            android:id="@+id/imageview_item_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="TextView"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent" />
    <ImageView
            android:id="@+id/imageview_item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toTopOf="@+id/imageview_item_name"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/defaultpicture" />

    <TextView
            android:id="@+id/imageview_item_Update"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="10dp"
            android:text="TextView"
            app:layout_constraintBottom_toBottomOf="@+id/imageview_item"
            app:layout_constraintEnd_toEndOf="@+id/imageview_item" />*//*



       */
/*     constraintLayout=itemView.findViewById(R.id.imageview_ConstraintLayout);
            ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
            layoutParams.height = screenHeight/4;
            layoutParams.width=screenWidth/3;
            constraintLayout.setLayoutParams(layoutParams);

            imageView=new ImageView(itemView.getContext());
            LinearLayout.LayoutParams image = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,     LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageView.setLayoutParams(image); //给ImageView设置布局参数*//*

    */
/*  imageView=new ImageView(itemView.getContext());

            imageView.setLayoutParams(imageLayout); //给ImageView设置布局参数*//*

        }
        //刷新数据
        @Override
        public void refreshData(final DataInforVidbase data, final int position) {
            RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(10));
            //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
            Glide.with(itemView.getContext())
                    .load(data.getVod_pic())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.defaultpicture)
                    .apply(options)
                    .into(imageView)
          */
/*      Glide.with(itemView.getContext())
                        .load(data.getVod_pic())

                        .centerCrop()
                        .apply(RequestOptions.bitmapTransform(mation))
                        .into(imageView)*//*
;
             textname.setText(data.getVod_name());
             textupdate.setText("更新到的什么");
            // /设置点击监听器
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(ClassificationActivtymy.this, Videodetails.class);
                    intent.putExtra("detailsName",data.getVod_name());
                    intent.putExtra("detailsUrl", data.getVod_id());
                    intent.putExtra("detailsImage", data.getVod_pic());
                    startActivity(intent, bundle);
                }
            });
        }
    }
    */
/**
     * 横向滑动的子条目分类
     *//*

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




    */
/**
     * 横向滑动的子条目地区
     *//*

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
    */
/**
     * 横向滑动的子条目年份
     *//*

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

    */
/*                         进行数据交互                                                 *//*

    //获取分类的数据
    public void runList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    String url= Constants.AppUrl+"vodshow/"+sortID+"-----------.html";
                    document = Jsoup.connect(url).timeout(80000).get();
                    ;//这里可用get也可以post方式，具体区别请自行了解
                    Elements sortDiv = document.select("div.container");    //解析来获取每条新闻的标题与链接地
                    Elements classification=sortDiv.select("div#hl01").select("li");
                    Elements regin=sortDiv.select("div#hl03").select("li");
                    Elements yearsname=sortDiv.select("div#hl04").select("li");
                    if (classification.size() != 0) {
                        for (int i = 3; i <classification.size(); i++) {

                            String hrefList = classification.get(i).select("a").attr("href");
                            String nameList = classification.get(i).select("a").text();
                            hrefList= hrefList.substring(hrefList.lastIndexOf("/")+1,hrefList.lastIndexOf("/")+3);
                            if (hrefList.indexOf("-")>0){
                                hrefList=hrefList.substring(0,hrefList.length()-1);
                            }
                            SortLateral sortLateral=new SortLateral();
                            sortLateral.setSortId(Integer.parseInt(hrefList));
                            sortLateral.setSortName(nameList);
                            sort.add(sortLateral);
                        }
                        if (regin.size()!=0){
                            for (int i = 3; i <regin.size() ; i++) {
                                String nameList = regin.get(i).select("a").text();
                                area.add(nameList);
                            }
                        }
                        if (yearsname.size()!=0){
                            for (int i = 3; i <yearsname.size() ; i++) {
                                years.add( yearsname.get(i).select("a").text());
                            }
                        }
                            SavecategoryUtils.getInstance().addSharedClassifi(getApplicationContext(),sort,area,years,preferencesname);
                    }

                    msg=mHandler.obtainMessage();
                    msg.what=1;
                    // 在工作线程中 通过Handler发送消息到消息队列中
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public Boolean bool(String preferencesname){
        boolean boolen=true;
        if (preferencesname!=null) {
            SharedPreferences sp = getSharedPreferences(preferencesname, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String Json = sp.getString("sort", null);
            if (SavecategoryUtils.getInstance().getDataList(getApplicationContext(), preferencesname, "sort").size() >= 12 && SavecategoryUtils.getInstance().getDataList(getApplicationContext(), preferencesname, "sort") != null && SavecategoryUtils.getInstance().getDataList(getApplicationContext(), preferencesname, "area") != null && SavecategoryUtils.getInstance().getDataList(getApplicationContext(), preferencesname, "years") != null) {
                sort = gson.fromJson(Json, new TypeToken<List<SortLateral>>() {
                }.getType()); //将json字符串转换成List集合
                //  sort=getDataList("sort");
                area = SavecategoryUtils.getInstance().getDataList(getApplicationContext(), preferencesname, "area");
                years = SavecategoryUtils.getInstance().getDataList(getApplicationContext(), preferencesname, "years");
                boolen = false;
            }
        }
        return  boolen;
    }



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
                        dataInfor.add(dataInforVid);
                    }

                    if (!(msgs==0)) {
                        msg = mHandler.obtainMessage();
                        msg.what = msgs;
                        // 在工作线程中 通过Handler发送消息到消息队列中
                        mHandler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ClassificationActivtymy.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
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
                        dataInfor.add(dataInforVid);
                    }
                    if (!(msgs == 0)) {
                    msg = mHandler.obtainMessage();
                    msg.what = msgs;
                    // 在工作线程中 通过Handler发送消息到消息队列中
                    mHandler.sendMessage(msg);

                }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ClassificationActivtymy.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
*/
