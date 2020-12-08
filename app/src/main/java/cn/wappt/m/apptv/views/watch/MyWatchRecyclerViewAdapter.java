package cn.wappt.m.apptv.views.watch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.BrowseRecordsBase;
import cn.wappt.m.apptv.utils.RoundedCornersTransformation;
import cn.wappt.m.apptv.views.details.Videodetails;


/**
 * @author: wsq
 * @date: 2020/10/28
 * Description:
 */
public class MyWatchRecyclerViewAdapter  extends RecyclerView.Adapter<MyWatchRecyclerViewAdapter.MyWatchViewHolder>{
    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;
    private int secret = 0;
    private String title = "";
    List<BrowseRecordsBase> browseRecordsBases; //存储获取数据
    private Context mContext;
    private LayoutInflater inflater; //接受布局文件
    private OnItemClickListener mOnItemClickListener;

    public MyWatchRecyclerViewAdapter( Context mContext) {
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
        }

    public List<BrowseRecordsBase> getBrowseRecordsBases() {
        if (browseRecordsBases == null) {
            browseRecordsBases = new ArrayList<>();
        }
        return browseRecordsBases;
    }

    //进行数据刷新
    public void notifyAdapter(List<BrowseRecordsBase> browseRecordsBases, boolean isAdd) {
        if (!isAdd) {
            this.browseRecordsBases = browseRecordsBases;
        } else {
            this.browseRecordsBases.addAll(browseRecordsBases);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override   //获取视图
    public MyWatchRecyclerViewAdapter.MyWatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.my_watch_look, parent, false);
        MyWatchViewHolder holder= new MyWatchViewHolder(view);
        return holder;
    }

    //填充视图
    @Override
    public void onBindViewHolder(@NonNull MyWatchRecyclerViewAdapter.MyWatchViewHolder holder, int position) {
        BrowseRecordsBase browseRecordsBase=browseRecordsBases.get(position);
        // 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
        RoundedCornersTransformation transformation = new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
        //顶部右边圆角
        RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);
        RoundedCornersTransformation transformatbootonleft = new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT);
        RoundedCornersTransformation transformatbottom = new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT);
        //组合各种Transformation,
        MultiTransformation<Bitmap> mation = new MultiTransformation<>
                //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                (new CenterCrop(), transformation, transformation1, transformatbootonleft, transformatbottom);
        //设置图片圆角角度
        holder.my_watch_image_imageView4.setBackgroundResource(R.drawable.watch_video);
        //  RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(10));
        Glide.with(mContext)
                .load(browseRecordsBase.getVod_pic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.defaultpicture)
                .apply(RequestOptions.bitmapTransform(mation))
                .into(holder.my_watch_image_view);
        holder.my_watch_name.setText(browseRecordsBase.getVod_name());
        if (Integer.valueOf(browseRecordsBase.getVod_index())>0){
            holder.my_watch_text_index.setText(browseRecordsBase.getVod_name()+"第"+browseRecordsBase.getVod_index()+"集");
        }else {
            holder.my_watch_text_index.setText(browseRecordsBase.getVod_name());
        }
        BrowseRecordsBase finalColumnbase=browseRecordsBase;
        holder.RelativeLayout_my_Watch_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(mContext, Videodetails.class);
                intent.putExtra("detailsName",finalColumnbase.getVod_name());
                intent.putExtra("detailsUrl", finalColumnbase.getVod_id());
                intent.putExtra("detailsImage", finalColumnbase.getVod_pic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(intent, bundle);
                Toast.makeText(mContext, "name:"+finalColumnbase.getVod_name()+ "position:" +"voddetail/"+finalColumnbase.getVod_id()+".html", Toast.LENGTH_SHORT).show();
            }
        });
        //判断是否是编辑模式
        if (mEditMode==MYLIVE_MODE_CHECK){
            holder.my_watch_check_box.setVisibility(View.GONE);//消失
        }else {
            holder.my_watch_check_box.setVisibility(View.VISIBLE);//显示
            //判断是否选择
            if (browseRecordsBase.isSelect()){
                holder.my_watch_check_box.setImageResource(R.mipmap.ic_checked);
            }else {
                holder.my_watch_check_box.setImageResource(R.mipmap.ic_uncheck);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), browseRecordsBases);
            }
        });
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener(int pos,List<BrowseRecordsBase> browseRecordsBases);
    }
    //设置编辑模式
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    @Override    //返回item个数
    public int getItemCount() {
            return browseRecordsBases.size();
    }


    public class MyWatchViewHolder extends RecyclerView.ViewHolder {
        private  ImageView my_watch_check_box;
        private ImageView my_watch_image_view;
        private ImageView my_watch_image_imageView4;
        private RelativeLayout RelativeLayout_my_Watch_look;
        private TextView my_watch_name;
        private TextView my_watch_text_index;
        public MyWatchViewHolder(@NonNull View itemView) {
            super(itemView);
            my_watch_check_box=itemView.findViewById(R.id.my_watch_check_box);
            my_watch_image_view =  itemView.findViewById(R.id.my_watch_image_view);
            my_watch_image_imageView4 =  itemView.findViewById(R.id.my_watch_image_imageView4);
            RelativeLayout_my_Watch_look =  itemView.findViewById(R.id.RelativeLayout_my_Watch_look);
            my_watch_name =  itemView.findViewById(R.id.my_watch_name);
            my_watch_text_index =  itemView.findViewById(R.id.my_watch_text_index);

        }
    }
}
