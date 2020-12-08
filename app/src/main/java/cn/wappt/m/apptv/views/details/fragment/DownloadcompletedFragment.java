package cn.wappt.m.apptv.views.details.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeffmony.downloader.VideoDownloadManager;
import com.jeffmony.downloader.database.VideoDownloadDatabaseHelper;
import com.jeffmony.downloader.listener.IDownloadInfosCallback;
import com.jeffmony.downloader.model.VideoTaskItem;
import com.jeffmony.downloader.model.VideoTaskState;
import com.jeffmony.downloader.utils.LogUtils;
import com.jeffmony.downloader.utils.VideoDownloadUtils;
import com.youth.banner.listener.OnBannerListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DownloadHelperUtil;
import cn.wappt.m.apptv.base.DownloadVideoTaskItemBase;
import cn.wappt.m.apptv.dao.DownloadHelperDao;
import cn.wappt.m.apptv.service.DownVideoService;
import cn.wappt.m.apptv.views.details.LocalM3u8;
import cn.wappt.m.apptv.views.details.VideoDownloadListActivity;
import cn.wappt.m.apptv.views.details.VideoDownloadListAdapter;
import cn.wappt.m.apptv.views.watch.DividerItemDecoration;

/**
 * @author: wsq
 * @date: 2020/10/29
 * Description:下载完成的片段
 */
public class DownloadcompletedFragment extends Fragment implements  OnBannerListener ,View.OnClickListener, VideoDownloadListAdapter.OnItemClickListener {


    RecyclerView recyclerView;
    View view;
    //编辑模式
    private static final int MYLIVE_MODE_CHECK = 0; //检查模式
    private static final int MYLIVE_MODE_EDIT = 1; //编辑模式
    private int mEditMode = MYLIVE_MODE_CHECK;  //当前是什么模式
    private boolean isSelectAll = false;//是否全选
    private boolean editorStatus = false; //编辑状态
    private int index = 0;//删除编号
    public Activity mActivity;
    LinearLayoutManager linearLayoutManager;

    //保存数据
    List<DownloadVideoTaskItemBase> downloadVideoTaskItemBases;
    //后台下载
    DownVideoService downVideoService;
    //适配器
    private VideoDownloadListAdapter mAdapter;
    //存储m3u8下载的进度
    private ArrayList<VideoTaskItem> items;
    private  VideoTaskItem videoTaskItem;
    //获取存储
    private SharedPreferences sp;
    //最后进度时间戳
    private long mLastProgressTimeStamp;
    //最后速度时间戳
    private long mLastSpeedTimeStamp;
    //控件
    TextView btnEditor;//编辑
    TextView downloadTv; //已选择
    TextView downloadTvSelectNum; //选择编号
    Button downloadBtnDelete;//删除
    TextView downloadSelectAll; //全选
    LinearLayout llMycollectionBottomDialog; //底部对话框
    List<DownloadHelperUtil> downloadHelperUtils;
    DownloadHelperDao downloadHelperDao;//下载图片数据库
    VideoDownloadDatabaseHelper videoDownloadDatabaseHelper;//下载进度数据库

    private static final String TAG = "DownloadFeatureActivity";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activtity_download_fragment, container, false);
        recyclerView = view.findViewById(R.id.download_fragment_recyclelerView);
        VideoDownloadListActivity mActivity= (VideoDownloadListActivity) getActivity();
        downloadTv=view.findViewById(R.id.download_fragment_tv);
        downloadTvSelectNum=view.findViewById(R.id.download_fragment_tv_select_num);
        downloadBtnDelete=view.findViewById(R.id.download_fragment_btn_delete);
        downloadSelectAll=view.findViewById(R.id.download_fragment_select_all);
        llMycollectionBottomDialog=view.findViewById(R.id.ll_mycollection_download_fragment);
        items=new ArrayList<>();
        initData();
        return view;
    }
    //进行加载数据操作
    public  void initData(){
        if (items.size()<=0) {
            videoDownloadDatabaseHelper   =new VideoDownloadDatabaseHelper(getContext());
            downloadHelperDao =new DownloadHelperDao(getContext());

            items= (ArrayList<VideoTaskItem>) videoDownloadDatabaseHelper.getDownloadInfos();

            ArrayList<VideoTaskItem> taskItems=new ArrayList<>();
            for (int i = 0; i < videoDownloadDatabaseHelper.getDownloadInfos().size(); i++) {
                if ( videoDownloadDatabaseHelper.getDownloadInfos().get(i).getTaskState()==5){
                    taskItems.add(videoDownloadDatabaseHelper.getDownloadInfos().get(i)); }
            }
            items= taskItems;
            downloadHelperUtils=downloadHelperDao.findByBrowse();
            List<DownloadHelperUtil> utils=new ArrayList<>();
            for (int i = 0; i <items.size() ; i++) {
                for (int j = 0; j < downloadHelperUtils.size(); j++) {
                    if (items.get(i).getUrl().equals(downloadHelperUtils.get(j).getDownload_url())) {
                        System.out.println("videoDownloadDatabaseHelper.getDownloadInfos().get(i).getUrl()   " + videoDownloadDatabaseHelper.getDownloadInfos().get(i).getUrl());
                        System.out.println("downloadHelperUtils.get(j).getDownload_url()   " + downloadHelperUtils.get(j).getDownload_url());
                        utils.add(downloadHelperUtils.get(j));
                    }
                }
            }
            downloadVideoTaskItemBases=new ArrayList<>();
            downloadHelperUtils=utils;
            for (int i = 0; i <downloadHelperUtils.size() ; i++) {
                System.out.println( "数据库2:  "+downloadHelperUtils.get(i));
                DownloadVideoTaskItemBase downloadVideoTaskItemBase=new DownloadVideoTaskItemBase(downloadHelperUtils.get(i).getDownload_name(),
                        downloadHelperUtils.get(i).getDownload_urlimage(),downloadHelperUtils.get(i).getDownload_url());
                downloadVideoTaskItemBases.add(downloadVideoTaskItemBase);
            }
        }
        //  items=itemss;
        if(downloadVideoTaskItemBases!=null ){
            initDatas();
            if (downloadVideoTaskItemBases.size()>0){
                mAdapter.notifyAdapter(downloadVideoTaskItemBases,items,false);
            }
            initListener();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //进行点击事件创建
    private void initListener(){
        mAdapter.setOnItemClickListener(this);
        downloadBtnDelete.setOnClickListener(this);
        downloadSelectAll.setOnClickListener(this);

    }


    /**
     * 初始化控件并且填充参数
     */
    private void initDatas() {
        mAdapter = new VideoDownloadListAdapter(getContext());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecorationHeader);
        //创建并设置Adapter
        recyclerView.setAdapter(mAdapter);

        btnEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataEditMode();
            }
        });
        VideoDownloadManager.getInstance().fetchDownloadItems(mInfosCallback);
    }

    @Override
    public void onItemClickListener(int pos, List<DownloadVideoTaskItemBase> downloadVideoTaskItemBases, ArrayList<VideoTaskItem> videoLists) {
        //是否是编辑者
        System.out.println("下载的id"+pos);
        //判断是否是编辑模式
        if (editorStatus) {
            DownloadVideoTaskItemBase base=new DownloadVideoTaskItemBase();
            for (int i = 0; i <downloadVideoTaskItemBases.size() ; i++) {
                if (downloadVideoTaskItemBases.get(i).getUrl().equals(videoLists.get(pos).getUrl())){
                    base=downloadVideoTaskItemBases.get(i);
                }
            }
            boolean isSelect = base.isSelect();
            if (!isSelect) {
                index++;
                base.setSelect(true);
                if (index == downloadVideoTaskItemBases.size()) {
                    isSelectAll = true;
                    downloadSelectAll.setText("取消全选");
                }
            } else {
                base.setSelect(false);
                index--;
                isSelectAll = false;
                downloadSelectAll.setText("全选");
            }
            setBtnBackground(index);
            downloadTvSelectNum.setText(String.valueOf(index));
            mAdapter.notifyDataSetChanged();
        }else  if (videoLists.get(pos).isCompleted() &&!editorStatus) {
            Intent intent = new Intent();
            intent.setClass(getContext(), LocalM3u8.class);
            String filePath = videoLists.get(pos).getFilePath();
            File file = new File(filePath);
            if (file.exists()) {
                intent.putExtra("videoUrl", videoLists.get(pos).getFilePath());
                startActivity(intent);
            } else {
                File mp4File = new File(filePath.substring(0, filePath.lastIndexOf("/")), VideoDownloadUtils.OUPUT_VIDEO);
                if ( videoLists.get(pos).isHlsType() && mp4File.exists()) {
                    intent.putExtra("videoUrl", mp4File.getAbsolutePath());
                    startActivity(intent);
                } else {
                    LogUtils.w(TAG, "File = " + filePath + " is gone");
                    VideoDownloadManager.getInstance().deleteVideoTask( videoLists.get(pos).getUrl(), true);
                    videoDownloadDatabaseHelper.deleteDownloadItemByUrl(videoLists.get(pos));
                    VideoDownloadManager.getInstance().removeDownloadInfosCallback(mInfosCallback);
                    items.remove(pos);
                    mAdapter.notifyDataSetChanged();//通知数据集已更改
                    downloadHelperDao.delectid(items.get(pos).getUrl());
                    mAdapter.getBrowseRecordsBases().remove(mAdapter.getBrowseRecordsBases().get(pos));
                    index--;
                }
            }
        }
    }

    //点击
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //删除
            case R.id.download_fragment_btn_delete:
                deleteVideo();
                break;
            //全选
            case R.id.download_fragment_select_all:
                selectAllMain();
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
            downloadBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(getContext())
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
                for (int i = mAdapter.getBrowseRecordsBases().size(), j =0 ; i > j; i--) {
                    DownloadVideoTaskItemBase myLive = mAdapter.getBrowseRecordsBases().get(i-1);
                    if (myLive.isSelect()) {
                        VideoDownloadManager.getInstance().deleteVideoTask(mAdapter.getBrowseRecordsBases().get(i-1).getUrl(), true);
                        VideoDownloadManager.getInstance().removeDownloadInfosCallback(mInfosCallback);
                        items.remove(i-1);
                        mAdapter.notifyDataSetChanged();//通知数据集已更改
                        String url=mAdapter.getBrowseRecordsBases().get(i-1).getUrl();
                        for (int k = 0; k < videoDownloadDatabaseHelper.getDownloadInfos().size() ; k++) {
                            if (videoDownloadDatabaseHelper.getDownloadInfos().get(k).getUrl().equals(url)){
                                videoDownloadDatabaseHelper.deleteDownloadItemByUrl(videoDownloadDatabaseHelper.getDownloadInfos().get(k));
                            }
                        }
                        downloadHelperDao.delectid(mAdapter.getBrowseRecordsBases().get(i-1).getUrl());
                        mAdapter.getBrowseRecordsBases().remove(myLive);
                        index--;
                    }
                }
                index = 0;
                setBtnBackground(index);
                downloadTvSelectNum.setText(String.valueOf(0));
                if (mAdapter.getBrowseRecordsBases().size() == 0){
                    llMycollectionBottomDialog .setVisibility(View.GONE);//消失底部对话框
                }
                //刷新
                mAdapter.notifyDataSetChanged();
                //删除弹框
                builder.dismiss();
            }
        });



    }



    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (mAdapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = mAdapter.getBrowseRecordsBases().size(); i < j; i++) {
                mAdapter.getBrowseRecordsBases().get(i).setSelect(true);
            }
            index = mAdapter.getBrowseRecordsBases().size();//获取全部长度
            downloadBtnDelete.setEnabled(true);
            downloadSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = mAdapter.getBrowseRecordsBases().size(); i < j; i++) {
                mAdapter.getBrowseRecordsBases().get(i).setSelect(false);
            }
            index = 0;
            downloadBtnDelete.setEnabled(false);
            downloadSelectAll.setText("全选");
            isSelectAll = false;
        }
        mAdapter.notifyDataSetChanged();
        //设置是否选中的背景图片
        setBtnBackground(index);
        downloadSelectAll.setText(String.valueOf(index));
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
            for (int i = 0; i <items.size() ; i++) {
                VideoDownloadManager.getInstance().pauseDownloadTask(items.get(i));
            }
            editorStatus = false;
            clearAll();
        }
        mAdapter.setEditMode(mEditMode);
    }

    private void clearAll() {
        downloadTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        downloadSelectAll.setText("全选");
        setBtnBackground(0);
    }


    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            downloadBtnDelete.setBackgroundResource(R.drawable.button_shape);
            downloadBtnDelete.setEnabled(true);
            downloadBtnDelete.setTextColor(Color.WHITE);
        } else {
            downloadBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            downloadBtnDelete.setEnabled(false);
            downloadBtnDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.color_E5E5E5));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (int i = 0; i <items.size() ; i++) {
            if (!items.get(i).isCompleted()) {
                if (items.get(i).getTaskState() == VideoTaskState.DOWNLOADING) {
                    VideoDownloadManager.getInstance().pauseDownloadTask(items.get(i));
                }
            }
        }
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof VideoDownloadListActivity ){
            this.mActivity=(Activity)context;
            btnEditor=mActivity.findViewById(R.id.download_btn_editor);
        }
    }

    private void notifyChanged(final VideoTaskItem item) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity!= null){
                    if (items!=null && items.size()>0){
                        mAdapter.notifyChanged(items, item);
                    }
                }
            }
        });
    }

    //下载的信息回调
    private IDownloadInfosCallback mInfosCallback =
            new IDownloadInfosCallback() {
                @Override
                public void onDownloadInfos(List<VideoTaskItem> items) {
                    for (VideoTaskItem item : items) {
                        notifyChanged(item);
                    }
                }
            };


    @Override
    public void OnBannerClick(Object data, int position) {

    }
}
