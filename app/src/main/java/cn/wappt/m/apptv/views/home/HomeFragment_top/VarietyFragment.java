package cn.wappt.m.apptv.views.home.HomeFragment_top;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.util.BannerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DataBean;
import cn.wappt.m.apptv.base.FilmBase;
import cn.wappt.m.apptv.interfaces.RecommendInterdaces;
import cn.wappt.m.apptv.interfaces.SortInterfaces;
import cn.wappt.m.apptv.utiandent.Pageload;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.views.home.HomeFragment_top.adapter.Home_Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.综艺
 */
public class VarietyFragment extends Fragment  implements OnBannerListener {


    //轮播图
    List<DataBean> dataBeans;     //详细链接

    List<FilmBase> film;
    List<FilmBase> films;
    Banner banner;
    View view;
    Context context;
    String[] name = {
            "综艺特别推荐", "内地综艺","日韩综艺","港台综艺","欧美综艺"
    };//存储的分类
    int [] nameid={3,23,24,25,26};
    LinearLayout sv;
    Message msg;
    int dp=3;  //判断
    int amount=6;   //数量
    String yearsname="";
    String areaname="";
    int index=1;
    int msgs=3;
    Handler mHandler;
   SmartRefreshLayout smartRefreshLayout;
    NestedScrollView   home_NestedScrollView;
    int Dataduplication=0;
    Home_Fragment adapter;
    RecyclerView recyclerView;
    Pageload pd ;
    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choiceness2, container, false);
        //在主线程中 通过匿名内部类 创建Handler类对象
        film=new ArrayList();
        pd = new Pageload(getContext());
        recyclerView  =view.findViewById(R.id.home_relatibeLayout2);
        dataBeans=new ArrayList<>();

        pd.setMessage("正在加载...");
        //设置ProgressDialog 是否可以按返回键取消；
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        //显示ProgressDialog
        pd.show();
        mHandler=new Handler(){
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                // 根据不同线程发送过来的消息，执行不同的UI操作
                switch (msg.what){
                    case 1:
                        //生成轮播图
                        if (banner==null){
                            initView();
                        }else {
                            //刷新数据
                            banner.setDatas(dataBeans);
                        }
                        break;
                    case 2:
                        if (adapter!=null){
                            adapter.index=0;
                        }
                        initRecyclerView(films,name,nameid,"综艺",0);
                        pd.cancel();
                        break;
                    case 3:
                        if (film.size()==(nameid.length*6)) {
                            Collections.sort(film, new Comparator<FilmBase>() {
                                @Override
                                public int compare(FilmBase o1, FilmBase o2) {
                                    //升序
                                    return o1.getIds() - o2.getIds();
                                }
                            });
                            films=new ArrayList<>();
                            films=film;
                            Dataduplication++;
                            setmHandler(2);
                        }
                        break;
                }
            }
        };
        smartRefreshLayout=view.findViewById(R.id.choiceness_smartrefreshLayout2);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(recyclerView.getContext())); //设置刷新标题
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {    //在刷新监听器上设置
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                recyclerView.removeAllViews();//清空布局
                if (adapter!=null){
                    adapter.index=0;
                }
                if (dataBeans!=null){//轮播图
                    setmHandler(1);
                }else{
                    runLBT();//轮播图
                }
                if (film!=null &&film.size()==(name.length*6)){
                    setmHandler(2);
                }else {
                    getdate(amount,index , yearsname, dp, areaname, msgs);
                }
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(recyclerView.getContext(),"正在刷新中", Toast.LENGTH_SHORT).show();
            }
        });

        if (dataBeans!=null&&dataBeans.size()>0){//轮播图
            setmHandler(1);
        }else{
            runLBT();//轮播图
        }
        if (film!=null &&film.size()==(name.length*6)){
            setmHandler(msgs);
        }else {
            getdate(amount,index , yearsname, dp, areaname, msgs);
        }
        return view;
    }



    public static ExecutorService newFixedThreadPools(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
    ExecutorService fixedThreadPool;
    public void  getdate(int amount,int index,String yearsname,int dp,String areaname,int msgs){
        film=new ArrayList();
        //创建及执行
        fixedThreadPool  = newFixedThreadPools(5);
        for (int i = 0; i <name.length ; i++) {
            int finalI = i;
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    Lateralload(amount,index , yearsname, dp,nameid[finalI] , areaname, msgs, finalI);//获取数据
                }
            };
            fixedThreadPool.execute(runnable);
        }
    }


    //进行调用Handler 东西的方法
    public  void setmHandler(int what){
        msg =mHandler.obtainMessage();
        msg.what=what;
        // 在工作线程中 通过Handler发送消息到消息队列中
        mHandler.sendMessage(msg);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            banner.stop();
        } else {
            banner.start();
        }
    }

    /**
     * 初始化RecyclerView
     */
    public void initRecyclerView( List dataInfor ,String[] names,int [] nameids,String fragmentNames,int index) {
        adapter = new Home_Fragment(getActivity(),dataInfor , names,  nameids, fragmentNames,index);
        LinearLayoutManager linearLayoutManager =  new  LinearLayoutManager( getActivity(),
                LinearLayoutManager.VERTICAL,  false ) {
            @Override//禁止滑动
            public boolean canScrollVertically() {
                return false;
            }
        };

        //如果确定每个item的内容不会改变RecyclerView的大小，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    //生成轮播图
    public void initView() {
        banner = view.findViewById(R.id.bannertio2);
        //添加生命周期观察者
        banner.addBannerLifecycleObserver(this);
        banner.setAdapter(new BannerRewrite(dataBeans,recyclerView.getContext()));
        banner.setIndicator(new CircleIndicator(recyclerView.getContext()));
        banner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT);
        banner.setOrientation(Banner.HORIZONTAL);
        banner.setBannerRound(20);

        banner.setIndicatorMargins(new IndicatorConfig.Margins(0, 0,
                BannerConfig.INDICATOR_MARGIN, (int) BannerUtils.dp2px(12)));
        home_NestedScrollView=view.findViewById(R.id.home_NestedScrollView2);
        home_NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
            // 将透明度声明成局部变量用于判断
            int alpha = 0;
            int count = 0;
            float scale = 0;
            @Override
            public void onScrollChange(NestedScrollView v,int scrollX,int scrollY,int oldScrollX,int oldScrollY){
                if (scrollY==0) {
                    System.out.println("scrollY <= height"+scrollY );
                    banner.start();
                    //  banner.isAutoLoop(true);
                } else {
                    System.out.println("scrollY !=height"+scrollY +"   ");
                    //  banner.isAutoLoop(false);
                    banner.stop();
                }
            }
        });
    }


    //获取轮播图数据
    public void runLBT() {
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        RecommendInterdaces request = retrofit.create(RecommendInterdaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.sjtjlbt(9);
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
                        DataBean dataBean = new DataBean();
                        dataBean.setBanner_style(jsonObject.get("vod_pic_slide").toString().replace("mac", "https"));
                        dataBean.setBanner_title(jsonObject.get("vod_name").toString());
                        dataBean.setBanner_url(jsonObject.get("vod_id").toString());
                        dataBean.setBanner_id(jsonObject.get("type_id_1").toString());
                        String id= String.valueOf(nameid[0]);
                        if (dataBean.getBanner_id().equals(id)){
                            dataBeans.add(dataBean);
                        }
                    }
                    setmHandler(1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public  void   Lateralload(int amounts, int pages, String yearsnames, int dps, int sortnames, String areanames, final int msgs,int ids){
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        SortInterfaces request = retrofit.create(SortInterfaces.class);
        if (ids==0){
            //对 发送请求数据进行封装
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            FilmBase base=new FilmBase();
                            base.setFilm_image(jsonObject.get("vod_pic").toString().replace("mac", "https"));
                            base.setFilm_name(jsonObject.get("vod_name").toString());
                            base.setFilm_url(jsonObject.get("vod_id").toString());
                            base.setFilm_remarks(jsonObject.get("vod_remarks").toString());
                            base.setIds(ids);
                            film.add(base);
                        }
                        setmHandler(msgs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override //获取失败
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    fixedThreadPool.shutdownNow();
                    pd.setMessage("正在重新加载");
                    getdate(amount,index , yearsname, dp, areaname, msgs);
                }
            });
        }else {
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
                            FilmBase base=new FilmBase();
                            base.setFilm_image(jsonObject.get("vod_pic").toString().replace("mac", "https"));
                            base.setFilm_name(jsonObject.get("vod_name").toString());
                            base.setFilm_url(jsonObject.get("vod_id").toString());
                            base.setFilm_remarks(jsonObject.get("vod_remarks").toString());
                            base.setIds(ids);
                            film.add(base);
                        }
                        setmHandler(msgs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override //获取失败
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    fixedThreadPool.shutdownNow();
                    pd.setMessage("正在重新加载");
                    getdate(amount,index , yearsname, dp, areaname, msgs);
                }
            });
        }
    }

    @Override
    public void OnBannerClick(Object data, int position) {

    }

}
