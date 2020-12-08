package cn.wappt.m.apptv.views.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DataInforVidbase;
import cn.wappt.m.apptv.interfaces.RecommendInterdaces;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.Screen_adjustmentUtil;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.search.SearchActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author: wsq
 * @date: 2020/11/2
 * Description:
 */
public class FeaturedActivty extends AppCompatActivity {
    List<DataInforVidbase> dataInfor; //存储获取数据
    @BindView(R.id.iv_recommend_screen_back)
    ImageView ivRecommendScreenBack; //返回
    @BindView(R.id.tv_recommend_screen_title)
    TextView tvScreenTitle;   //标题
    @BindView(R.id.iv_recommend_screen_seek)
    ImageView ivRecommendScreenSeek; //搜索

    @BindView(R.id.sort_recommend_recyclerView)
    RecyclerView sortRecommendRecyclerView;
    @BindView(R.id.recommend_smartrefreshLayout)
    SmartRefreshLayout recommendSmartrefreshLayout;
    @BindView(R.id.iv_fab2)
    FloatingActionButton ivFab2;
    ConstraintLayout featured_relativeLayout2;
    public static int  screenWidth;//屏幕宽度
    private int HORIZONTAL_VIEW_X = 0;//水平RecyclerView滑动的距离
    Handler mHandler;
    Message msg;
    int page = 1; //当前页码
    int amount = 18; //数量
    FeaturedAdapter adapter;//适配器
    int recommend = 1; //推荐
    int judgment = 2;//判断
public  static int  screenHeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        dataInfor = new ArrayList<>();
        featured_relativeLayout2=findViewById(R.id.featured_relativeLayout2);
        ivRecommendScreenBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                FeaturedActivty.this.finish();
            }
        });
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
        ivRecommendScreenSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索
                Intent intent = new Intent(FeaturedActivty.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        basicParamInit();
        recommendSmartrefreshLayout.setRefreshHeader(new ClassicsHeader(this)); //设置刷新标题
        recommendSmartrefreshLayout.setRefreshFooter(new ClassicsFooter(this)); //设置刷新页脚
        recommendSmartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {    //在刷新监听器上设置
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    page = 1;
                    initRecyclerView(dataInfor, screenWidth);
                }
                ivFab2.hide();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(FeaturedActivty.this, "正在刷新中", Toast.LENGTH_SHORT).show();
            }
        });
        recommendSmartrefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {   //设置“加载更多侦听器”
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Toast.makeText(FeaturedActivty.this, "正在加载", Toast.LENGTH_SHORT).show();
                page = page + 1;
                msg = mHandler.obtainMessage();
                msg.what = 2;
                // 在工作线程中 通过Handler发送消息到消息队列中
                mHandler.sendMessage(msg);
                ivFab2.hide();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        mHandler = new Handler() {
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                // 根据不同线程发送过来的消息，执行不同的UI操作
                switch (msg.what) {
                    case 0:
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        break;
                    case 1:
                        initRecyclerView(dataInfor, screenWidth);
                        break;
                    case 2:
                        Lateralloadsjtj(page, judgment, amount, recommend, 3);
                    case 3:
                        if (adapter!=null){
                            adapter.notifyDataSetChanged();
                        }

                        break;

                }
            }
        };
        Lateralloadsjtj(page, judgment, amount, recommend, 1);


        sortRecommendRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        ivFab2.hide();
                    } else {
                        //显示回到顶部按钮
                        ivFab2.show();
                        ivFab2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(0);
                            }
                        });

                    }//获取RecyclerView滑动时候的状态
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                    ivFab2.hide();
                }
            }
        });
    }

            /**
             * 初始化RecyclerView
             */
            public void initRecyclerView(List dataInfor, int screenWidth) {
                adapter = new FeaturedAdapter(this, dataInfor, screenWidth);
                adapter.setHasStableIds(true);
                //定义布局管理器为Grid管理器，设置一行放3个
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
                sortRecommendRecyclerView.setLayoutManager(layoutManager);
                sortRecommendRecyclerView.setAdapter(adapter);
                //设置分隔线
                sortRecommendRecyclerView.addItemDecoration(new MyFeatureDecoration());

            }

    /**
     * 计算屏幕的宽度
     */
    private void basicParamInit() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        ViewTreeObserver vto = featured_relativeLayout2.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                featured_relativeLayout2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                featured_relativeLayout2.getHeight();
                screenHeight=(metric.heightPixels-featured_relativeLayout2.getHeight()- Screen_adjustmentUtil.CLASS_SCREEN);
                featured_relativeLayout2.getWidth();
            }
        });
    }


            //加载数据
            public void Lateralloadsjtj(int p, int pd, int s, int tj, int msgs) {
                //进行实例化数据
                Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
                RecommendInterdaces request = retrofit.create(RecommendInterdaces.class);
                //对 发送请求数据进行封装
                Call<ResponseBody> call = request.sjtjsort(p, pd, s, tj);
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
                                System.out.println(dataInforVid);
                            }
                            if (!(msgs == 0)) {
                                msg = mHandler.obtainMessage();
                                msg.what = msgs;
                                // 在工作线程中 通过Handler发送消息到消息队列中
                                mHandler.sendMessage(msg);
                            }
                            if (adapter != null) {
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
                        Toast.makeText(FeaturedActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
