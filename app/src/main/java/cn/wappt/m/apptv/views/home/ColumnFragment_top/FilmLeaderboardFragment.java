package cn.wappt.m.apptv.views.home.ColumnFragment_top;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.Columnbase;
import cn.wappt.m.apptv.dao.ColumnDao;
import cn.wappt.m.apptv.interfaces.ColumnInterfaces;
import cn.wappt.m.apptv.utils.DensityUtils;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.SpaceItemDecoration;
import cn.wappt.m.apptv.views.home.ColumnFragment_top.adapter.ColumnRecyclerViewAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * @author: wsq
 * @date: 2020/10/27
 * Description: 电影排行榜
 */
public class FilmLeaderboardFragment extends Fragment  {

    RefreshLayout refreshLayout;
   // RecyclerViewAdapter adapter;
    ColumnRecyclerViewAdapter columnRecyclerViewAdapter;
    RecyclerView recyclerView;
    List<Columnbase> columnbases; //存储获取数据
    private View view;
    Handler mHandler;
    Message msg;
    private  String type="电影";
    private int screenWidth;//屏幕宽度
    ColumnDao dao;
    int stor=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.column_fragment_cartoon, container, false);
        refreshLayout = view.findViewById(R.id.smartrefreshLayout_column);
         initData();
        return view;
    }




    //刷新加载
    private void initData() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(view.getContext())); //设置刷新标题
         refreshLayout.setOnRefreshListener(new OnRefreshListener() {    //在刷新监听器上设置
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                recyclerView.removeAllViews();//清空布局
                columnbases=new ArrayList<>();
                dao.delecttype(type);
                Leaderboard(getContext(),20,1,stor);
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                Toast.makeText(view.getContext(),"正在刷新中", Toast.LENGTH_SHORT).show();
            }
        });
         mHandler=new Handler(){
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                // 根据不同线程发送过来的消息，执行不同的UI操作
                switch (msg.what){
                    case 0:
                        initRecyclerView();
                        break;

                }
            }
        };
        dao=new ColumnDao(getContext());
        columnbases=new ArrayList<>();
        columnbases=dao.findByColumnTypes(type);
        if (columnbases== null || columnbases.size()==0 ){
            columnbases=new ArrayList<>();
            dao.delect();
            Leaderboard(getContext(),20,1,stor);
        }else if (columnbases.size()==20){
            setmHandler(0);
        }
    }
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        columnRecyclerViewAdapter=new ColumnRecyclerViewAdapter(columnbases,getActivity());
        recyclerView=view.findViewById(R.id.sort_recyclerView_column);
        LinearLayoutManager  layoutManager = new LinearLayoutManager(getActivity().getApplicationContext() );
        recyclerView.setLayoutManager(layoutManager);
        //设置间距
        recyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getActivity(),10)));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        recyclerView.setAdapter(columnRecyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    public void Leaderboard(Context context, int amounts, int pages, int sortnames ) {
        //进行实例化数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        ColumnInterfaces request = retrofit.create(ColumnInterfaces.class);

        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.bdxssort(amounts, pages, sortnames);
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
                        Columnbase columnbase=new Columnbase();
                        columnbase.setVod_pic(jsonObject.get("vod_pic").toString().replace("mac", "https"));
                        columnbase.setVod_blurb((String) jsonObject.get("vod_blurb"));
                        columnbase.setVod_name((String) jsonObject.get("vod_name"));
                        columnbase.setVod_score((String) jsonObject.get("vod_score"));
                        columnbase.setVod_id((String) jsonObject.get("vod_id"));
                        columnbase.setVod_class((String) jsonObject.get("vod_class"));
                        columnbases.add(columnbase);
                        dao.add(type,columnbase);
                    }
                    if (columnRecyclerViewAdapter==null){
                        //存储排行榜信息
                        setmHandler(0);
                    }else {
                        columnRecyclerViewAdapter.notifyDataSetChanged();//刷新
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override //获取失败
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //进行调用Handler 东西的方法
    public  void setmHandler(int what){
        msg =mHandler.obtainMessage();
        msg.what=what;
        // 在工作线程中 通过Handler发送消息到消息队列中
        mHandler.sendMessage(msg);
    }

}
