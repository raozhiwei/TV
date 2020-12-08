package cn.wappt.m.apptv.views.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jeffmony.downloader.model.VideoTaskItem;
import com.jeffmony.downloader.model.VideoTaskState;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DownloadVideoTaskItemBase;


public class VideoDownloadListAdapter extends RecyclerView.Adapter<VideoDownloadListAdapter.VideoDownloadListHolder> {

    private static final String TAG = "VideoListAdapter";
    private Context mContext;
    private ArrayList<VideoTaskItem> videoList=new ArrayList<>();
    List<DownloadVideoTaskItemBase> bases;
    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;
    private int secret = 0;
    private String title = "";
    private LayoutInflater inflater; //接受布局文件
    private OnItemClickListener mOnItemClickListener;
    DownloadVideoTaskItemBase videoTaskItemBase;
boolean editorStatus;
    public VideoDownloadListAdapter(@NonNull Context context) {
        this.mContext = context;
        inflater=LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public VideoDownloadListAdapter.VideoDownloadListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.download_list_m3u8, parent, false);
        VideoDownloadListHolder holder= new VideoDownloadListHolder(view);
        mContext=view.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDownloadListAdapter.VideoDownloadListHolder holder, int position) {
     //进行数据填充
        VideoTaskItem item = videoList.get(position);
        for (int i = 0; i <bases.size() ; i++) {
            if (item.getUrl().equals(bases.get(i).getUrl())){
                videoTaskItemBase=bases.get(i);
            }
        }
        if (item.getFileName()!=null) {
            holder.url_text.setText(item.getFileName());
        }
        holder.url_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (videoTaskItemBase==null){
            videoTaskItemBase =new DownloadVideoTaskItemBase();
        }
        holder.url_text .setText(videoTaskItemBase.getName());
        //获取图片地址
        Glide.with(mContext)
                .load(videoTaskItemBase.getImageurl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.defaultpicture)
                .into(holder.url_image);
        if (mEditMode==MYLIVE_MODE_CHECK){
            holder.download_check_box.setVisibility(View.GONE);//消失
        }else {
            holder.download_check_box.setVisibility(View.VISIBLE);//显示
            //判断是否选择
            if (videoTaskItemBase.isSelect()){
                holder.download_check_box.setImageResource(R.mipmap.ic_checked);
            }else {
                holder.download_check_box.setImageResource(R.mipmap.ic_uncheck);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);
                System.out.println(bases
                );
                System.out.println(videoList);

                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClickListener(position,bases,videoList);
                }else {

                }

            }
        });
        setStateText(holder.download_txt, item);
        setDownloadInfoText(holder.download_txt, item,holder.progressBarStyleHorizontal);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
    //设置编辑模式
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener
                (int pos,List<DownloadVideoTaskItemBase> downloadVideoTaskItemBases, ArrayList<VideoTaskItem> videoLists);
    }
    public List<DownloadVideoTaskItemBase> getBrowseRecordsBases() {
        return bases;
    }

    //进行数据刷新
    public void notifyAdapter(List<DownloadVideoTaskItemBase> downloadVideoTaskItemBases, ArrayList<VideoTaskItem> videoLists, boolean isAdd) {
        if (!isAdd) {
            this.videoList= videoLists;
            this.bases = downloadVideoTaskItemBases;
        } else {
            this.videoList=videoLists;
            this.bases.addAll(downloadVideoTaskItemBases);
        }
        notifyDataSetChanged();
    }


    private void setStateText(TextView stateView, VideoTaskItem item) {
        switch (item.getTaskState()) {
            case VideoTaskState.PENDING:

                stateView.setText("等待中");
                break;
            case VideoTaskState.PREPARE:

                stateView.setText("准备好");
                break;
            case VideoTaskState.START:

                break;
            case VideoTaskState.DOWNLOADING:

                break;
            case VideoTaskState.PAUSE:
                stateView.setText("下载暂停");
                break;
            case VideoTaskState.SUCCESS:
                System.out.println("SUCCESS下载完成");
                stateView.setText("下载完成");
                break;
            case VideoTaskState.ERROR:

                stateView.setText("下载错误");
                break;
            default:
                stateView.setText("未下载");
                break;

        }
    }

    private void setDownloadInfoText(TextView infoView, VideoTaskItem item, ProgressBar progressBar) {
        switch (item.getTaskState()) {
            case VideoTaskState.DOWNLOADING:
                infoView.setText(" 速度:" + item.getSpeedString() +" , 已下载:" + item.getDownloadSizeString());
                progressBar.setProgress((int) item.getPercent());
                break;
            case VideoTaskState.SUCCESS:
                progressBar.setProgress((int) item.getPercent());
                break;
            case VideoTaskState.PAUSE:
                progressBar.setProgress((int) item.getPercent());
                infoView.setText("进度:" + item.getPercentString());
                break;
            default:
                break;
        }
    }

    public void notifyChanged(ArrayList<VideoTaskItem> items, VideoTaskItem item) {
        for (int index = 0; index < getItemCount(); index++) {
            if ((videoList.get(index)).equals(item)) {

                items.set(index,item);
                notifyDataSetChanged();
            }
        }
    }


    public class VideoDownloadListHolder extends RecyclerView.ViewHolder {
        ImageView download_check_box;
        ImageView url_image;
        TextView download_txt;
        ProgressBar progressBarStyleHorizontal;
        TextView  url_text;
        RelativeLayout playBtn ;
        public VideoDownloadListHolder(@NonNull View itemView) {
            super(itemView);
            download_check_box=itemView.findViewById(R.id.download_check_box);
            playBtn=itemView.findViewById(R.id.download_play_btn);
            url_image=itemView.findViewById(R.id.url_image);
            download_txt=itemView.findViewById(R.id.download_txt);
            progressBarStyleHorizontal=itemView.findViewById(R.id.pb);
            url_text=itemView.findViewById(R.id.url_text);
        }
    }



}
