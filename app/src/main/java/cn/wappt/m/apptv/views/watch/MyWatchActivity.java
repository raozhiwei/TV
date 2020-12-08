package cn.wappt.m.apptv.views.watch;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.BrowseRecordsBase;
import cn.wappt.m.apptv.dao.DetailsDao;
import cn.wappt.m.apptv.utils.StatusBarUtil;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description:进行浏览数据
 */
public class MyWatchActivity extends AppCompatActivity  implements View.OnClickListener, MyWatchRecyclerViewAdapter.OnItemClickListener{
    //编辑模式
    private static final int MYLIVE_MODE_CHECK = 0; //检查模式
    private static final int MYLIVE_MODE_EDIT = 1; //编辑模式
    private int mEditMode = MYLIVE_MODE_CHECK;  //当前是什么模式
    private boolean isSelectAll = false;//是否全选
    private boolean editorStatus = false; //编辑状态
    private int index = 0;//删除编号
    LinearLayoutManager linearLayoutManager;
    MyWatchRecyclerViewAdapter myWatchRecyclerViewAdapter;
    DetailsDao dao;
    List<BrowseRecordsBase> browseRecordsBases;
    @BindView(R.id.browserecords_iv_back)
    ImageView ivBack;//返回
    @BindView(R.id.browserecords_btn_editor)//编辑
    TextView btnEditor;
    @BindView(R.id.my_watch_recyclelerView)
    RecyclerView recyclerView;
    @BindView(R.id.browserecords_tv)
    TextView browserecordsTv; //已选择
    @BindView(R.id.browserecords_tv_select_num)
    TextView browserecordsTvSelectNum; //选择编号
    @BindView(R.id.browserecords_btn_delete)
    Button browserecordsBtnDelete;//删除
    @BindView(R.id.browserecords_select_all)
    TextView browserecordsSelectAll; //全选
    @BindView(R.id.ll_mycollection_bottom_dialog)
    LinearLayout llMycollectionBottomDialog; //底部对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_watch_view);
        ButterKnife.bind(this);
        //加载数据
        dao = new DetailsDao(getApplicationContext());
        browseRecordsBases = new ArrayList<>();
        browseRecordsBases = dao.findByBrowse();
                if(browseRecordsBases.size()>0&&browseRecordsBases!=null ){
                    initRecyclerView();
                    if (browseRecordsBases.size()>0){
                        myWatchRecyclerViewAdapter.notifyAdapter(browseRecordsBases,false);
                    }
                    initListener();
                }
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

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        myWatchRecyclerViewAdapter = new MyWatchRecyclerViewAdapter( getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecorationHeader);
        //创建并设置Adapter
        recyclerView.setAdapter(myWatchRecyclerViewAdapter);
    }


    //进行点击事件创建
    private void initListener(){
        myWatchRecyclerViewAdapter.setOnItemClickListener(this);
        browserecordsBtnDelete.setOnClickListener(this);
        browserecordsSelectAll.setOnClickListener(this);
        btnEditor.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }


    //点击
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //删除
            case R.id.browserecords_btn_delete:
                deleteVideo();
                break;
                //全选
            case R.id.browserecords_select_all:
                selectAllMain();
                break;
                //编辑
            case R.id.browserecords_btn_editor:
                updataEditMode();
                break;
            case  R.id.browserecords_iv_back:
                MyWatchActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0){
            browserecordsBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(this)
                .create();
        builder.show();
        if (builder.getWindow() == null) return;

        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg =  builder.findViewById(R.id. browserecords_tv_msg);
        Button cancle =  builder.findViewById(R.id.browserecords_btn_cancle);
        Button sure =  builder.findViewById(R.id.browserecords_btn_sure);
        if (msg == null || cancle == null || sure == null) return;
        if (index == 1) {
            msg.setText("删除后不可恢复，是否删除该条目？");
        } else {
            msg.setText("删除后不可恢复，是否删除这" + index + "个条目？");
        }
        //选中关闭弹窗
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = myWatchRecyclerViewAdapter.getBrowseRecordsBases().size(), j =0 ; i > j; i--) {
                    BrowseRecordsBase myLive = myWatchRecyclerViewAdapter.getBrowseRecordsBases().get(i-1);
                    if (myLive.isSelect()) {
                        System.out.println("删除："+myLive.getVod_id());
                        dao.delectid(myLive.getVod_id());
                        myWatchRecyclerViewAdapter.getBrowseRecordsBases().remove(myLive);
                        index--;
                    }
                }
                index = 0;
                browserecordsTvSelectNum.setText(String.valueOf(0));
                setBtnBackground(index);
                if (myWatchRecyclerViewAdapter.getBrowseRecordsBases().size() == 0){
                    llMycollectionBottomDialog .setVisibility(View.GONE);//消失底部对话框
                }
                //刷新
                myWatchRecyclerViewAdapter.notifyDataSetChanged();
                //删除弹框
                builder.dismiss();
            }
        });
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (myWatchRecyclerViewAdapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = myWatchRecyclerViewAdapter.getBrowseRecordsBases().size(); i < j; i++) {
                myWatchRecyclerViewAdapter.getBrowseRecordsBases().get(i).setSelect(true);
            }
            index = myWatchRecyclerViewAdapter.getBrowseRecordsBases().size();//获取全部长度
            browserecordsBtnDelete.setEnabled(true);
            browserecordsSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = myWatchRecyclerViewAdapter.getBrowseRecordsBases().size(); i < j; i++) {
                myWatchRecyclerViewAdapter.getBrowseRecordsBases().get(i).setSelect(false);
            }
            index = 0;
            browserecordsBtnDelete.setEnabled(false);
            browserecordsSelectAll.setText("全选");
            isSelectAll = false;
        }
        myWatchRecyclerViewAdapter.notifyDataSetChanged();
        //设置是否选中的背景图片
        setBtnBackground(index);
        browserecordsTvSelectNum.setText(String.valueOf(index));
    }


    //进行改变模式
    private void updataEditMode() {
        mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
        if (mEditMode == MYLIVE_MODE_EDIT) {
            btnEditor.setText("取消");
            llMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            btnEditor.setText("编辑");
            llMycollectionBottomDialog.setVisibility(View.GONE);
            editorStatus = false;
            clearAll();
        }
        myWatchRecyclerViewAdapter.setEditMode(mEditMode);
    }


    private void clearAll() {
        browserecordsTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        browserecordsSelectAll.setText("全选");
        setBtnBackground(0);
    }




    @Override
    public void onItemClickListener(int pos, List<BrowseRecordsBase> browseRecordsBases) {
        //是否是编辑者
        if (editorStatus) {
            BrowseRecordsBase base = browseRecordsBases.get(pos);
            boolean isSelect = base.isSelect();
            if (!isSelect) {
                index++;
                base.setSelect(true);
                if (index == browseRecordsBases.size()) {
                    isSelectAll = true;
                    browserecordsSelectAll.setText("取消全选");
                }

            } else {
                base.setSelect(false);
                index--;
                isSelectAll = false;
                browserecordsSelectAll.setText("全选");
            }
            setBtnBackground(index);
            browserecordsTvSelectNum.setText(String.valueOf(index));
            myWatchRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            browserecordsBtnDelete.setBackgroundResource(R.drawable.button_shape);
            browserecordsBtnDelete.setEnabled(true);
            browserecordsBtnDelete.setTextColor(Color.WHITE);
        } else {
            browserecordsBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            browserecordsBtnDelete.setEnabled(false);
            browserecordsBtnDelete.setTextColor(ContextCompat.getColor(this, R.color.color_E5E5E5));
        }
    }




}
