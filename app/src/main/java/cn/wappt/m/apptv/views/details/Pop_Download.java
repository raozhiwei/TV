package cn.wappt.m.apptv.views.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jeffmony.downloader.model.VideoTaskItem;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.VideoDetailsutliRecommend;
import cn.wappt.m.apptv.dao.DownloadHelperDao;
import cn.wappt.m.apptv.service.DownVideoService;
import cn.wappt.m.apptv.utiandent.VideoDetailsutli;
import cn.wappt.m.apptv.utils.DensityUtils;

/**
 * @author 纵游四方悠自得
 * @create 2020/11/12--9:43
 * @effect 下载列表，必须先传值再调用
 */
public class Pop_Download extends BottomPopupView implements View.OnClickListener {

    private GridLayout biaoge;
    private TextView butDownloadAll;      //全选
    private TextView butDownladYes;      //开始下载
    private boolean [] countboolean;
    private Button[] but;
    public  int size = 0;
    private boolean allbool = false;
    public  int line=0;//线路
    //获取的数据
    public VideoDetailsutli videoDetailsutli;
    public List<VideoDetailsutliRecommend> videoDetailsutliList;
    public Context context;
    private String detailsName;//电影名称
    private String detailsImage;//图片
    private String detailsUrl;//地址

    public Pop_Download(@NonNull Context context , int line, VideoDetailsutli videodetails, List<VideoDetailsutliRecommend> videoDetailsutliLists,String detailsName,String detailsImage,String detailsUrl, ArrayList<VideoTaskItem> list) {
        super(context);
        this.context=  context;
        this.line=line;
        this.videoDetailsutli=videodetails;
        this.videoDetailsutliList=videoDetailsutliLists;
        this.detailsImage=detailsImage;
        this.detailsName=detailsName;
        this.detailsUrl=detailsUrl;

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.scrollview_download;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        //是否能够下载
        if (size == 0) {
            dismissWith(new Runnable() {
                @Override
                public void run() {
                    Toast ts = Toast.makeText(getContext(),"本剧暂时不支持下载", Toast.LENGTH_LONG);
                    ts.show();
                }
            });
        }
        initviewControl();

        //计算表格生成多少行
        int num = size/4;
        if (size%4 != 0) {
            num+=1;
        }
        biaoge.removeAllViews();    //清空容器
        biaoge.setRowCount(num);
        biaoge.setColumnCount(4);
        for (int i = 0; i <size ; i++) {
            but[i] = new Button(getContext());
            countboolean[i] = false;
            but[i].setText(String.valueOf((i+1)));       //内容
            but[i].setTextSize(DensityUtils.px2sp(getContext(),30));   //字体大小
            but[i].setBackgroundResource(R.drawable.biank);
            but[i].setPadding(5,0,5,0);
            but[i].setTypeface(Typeface.DEFAULT);

            final int finalI = i;
            but[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countboolean[finalI]) {
                        countboolean[finalI] = false;
                    }else{
                        countboolean[finalI] = true;
                    }
                    if (countboolean[finalI]) {
                        but[finalI].setBackgroundResource(R.color.colorPrimary);
                    }else {
                        but[finalI].setBackgroundResource(R.drawable.biank);
                        butDownloadAll.setText("全选");
                        allbool = false;
                    }
                }
            });
            //指定该组件所在的行
            GridLayout.Spec rowSpec = GridLayout.spec(i/4);
            //指定改自建所在的列
            GridLayout.Spec columnSpec  = GridLayout.spec(i%4);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setMargins(DensityUtils.dp2px(getContext(),10),DensityUtils.dp2px(getContext(),4),
                    DensityUtils.dp2px(getContext(),10),DensityUtils.dp2px(getContext(),10));
            params.width = DensityUtils.dp2px(getContext(),78);
            params.height = DensityUtils.dp2px(getContext(),30);
            but[i].setLayoutParams(params);
            biaoge.addView( but[i],params);

        }

    }

    public void initviewControl(){
        biaoge = findViewById(R.id.biaoge);
        butDownladYes = findViewById(R.id.butDownladYes);
        butDownloadAll = findViewById(R.id.butDownloadAll);

        biaoge.setOnClickListener(this);
        butDownladYes.setOnClickListener(this);
        butDownloadAll.setOnClickListener(this);
        countboolean = new boolean[size];
        but = new Button[size];
    }

    // 最大高度为Window的0.85
    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.75f);
    }


    public  void  Download_message(int index){
        boolean Whether_data=false;
        String detailname = "";
        if (videoDetailsutli.getList_anthology()[line].length > 1) {
            detailname = videoDetailsutli.getVod_name() + "第" + (index + 1) + "集";
        } else {
            detailname = videoDetailsutli.getVod_name();
        }
        Intent intent= new Intent(context, DownVideoService.class);
        DownloadHelperDao downloadHelper=new DownloadHelperDao(context);
             int count=downloadHelper.findByBrowse_index(videoDetailsutli.getList_anthology()[line][index]);  //进行判十分进行了存储数据
             Bundle bundle=new Bundle();
            if (count==0) {
                bundle.putString("name",detailname);
                bundle.putString("url", videoDetailsutli.getList_anthology()[line][index]);
                bundle.putString("Imageurl",detailsImage);
                intent.putExtras(bundle);
                downloadHelper .add(videoDetailsutli.getList_anthology()[line][index],detailname,detailsImage);
                Whether_data=true;
            } else {
                Toast.makeText(context, "已经在下载列表", Toast.LENGTH_SHORT).show();
            }

        if (Whether_data){
            //发送数据给service
            context.startService(intent); //启动服务
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.butDownladYes:
                //点击开始下载，根据countboolean下标为ture的则是需要下载
                for (int i = 0; i <countboolean.length ; i++) {
                    if (countboolean[i]) {
                        System.out.println("是否选择"+i+"     "+countboolean[i]);
                        Download_message(i);
                    }
                }
                dismiss();
                break;
            case R.id.butDownloadAll:
                //判断是全选还是取消全选
                if (!allbool) {
                    for (int i = 0; i < size; i++) {
                        but[i].setBackgroundResource(R.color.colorPrimary);
                        countboolean[i] = true;
                    }
                    butDownloadAll.setText("已全选");
                    allbool = true;
                }else{
                    for (int i = 0; i < size; i++) {
                        but[i].setBackgroundResource(R.drawable.biank);
                        countboolean[i] = true;
                    }
                    butDownloadAll.setText("全选");
                    allbool = false;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
