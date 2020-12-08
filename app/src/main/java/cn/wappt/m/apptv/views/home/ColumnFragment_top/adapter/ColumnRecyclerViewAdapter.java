package cn.wappt.m.apptv.views.home.ColumnFragment_top.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.Columnbase;
import cn.wappt.m.apptv.utils.RoundedCornersTransformation;
import cn.wappt.m.apptv.views.details.Videodetails;
import cn.wappt.m.apptv.views.home.ColumnFragment;

/**
 * @author: wsq
 * @date: 2020/10/27
 * Description:
 */
public class ColumnRecyclerViewAdapter  extends RecyclerView.Adapter<ColumnRecyclerViewAdapter.ColumnViewHolder> {
    List<Columnbase> columbase; //存储获取数据
    private Context mContext;
    private LayoutInflater inflater; //接受布局文件
    private String [] id;
   private  boolean isisExpanded=false;
    public ColumnRecyclerViewAdapter(List<Columnbase> columbase, Context mContext) {
        this.columbase = columbase;
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
        id=new String[3];
        for (int i = 0; i <columbase.size() ; i++) {
            if (i<3){
                System.out.println(id[i]);
                id[i]=columbase.get(i).getVod_id();
            }else {
                break;
            }
        }
    }
    @NonNull
    @Override
    public ColumnRecyclerViewAdapter.ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.column_fragment_looklike,parent, false);
        ColumnViewHolder holder= new ColumnViewHolder(view);
        return holder;
    }



    //填充视图
    @Override
    public void onBindViewHolder(@NonNull ColumnRecyclerViewAdapter.ColumnViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams =  holder.constraintLayout.getLayoutParams();
        layoutParams.width = ColumnFragment.screenWidth;
        layoutParams.height=ColumnFragment.screenHeight/4-30;
        holder.constraintLayout.setLayoutParams(layoutParams);

        Columnbase columnbase = columbase.get(position);
        if (columbase.get(position).getVod_id().equals(id[0])) {
            holder.imageView_Leaderboard.setBackgroundResource(R.drawable.leaderboard1);
        } else if (columbase.get(position).getVod_id().equals(id[1])) {
            holder.imageView_Leaderboard.setBackgroundResource(R.drawable.leaderboard2);
        } else if (columbase.get(position).getVod_id().equals(id[2])) {
            holder.imageView_Leaderboard.setBackgroundResource(R.drawable.leaderboard3);
        } else {
            holder.imageView_Leaderboard.setBackgroundResource(R.color.colorTabLabnull);
        }
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
        //  RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(10));
        holder.coulum_image_view .setScaleType(ImageView.ScaleType.CENTER_CROP);
  //      run(mContext,holder.coulum_image_view,columnbase.getVod_pic(),mation);
        //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
        Glide.with(mContext)
                .load(columnbase.getVod_pic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.defaultpicture)
                .apply(RequestOptions.bitmapTransform(mation))
                .into(holder.coulum_image_view);
        holder.column_name.setText(columnbase.getVod_name());
       // holder.text_video_Introduction.setText(columnbase.getVod_blurb());
        holder.text_video_Introduction.setText(columnbase.getVod_blurb());
        holder.text_video_type.setText(columnbase.getVod_class());
        holder.text_video_Introduction.setMovementMethod(ScrollingMovementMethod.getInstance());
        holder.text_video_score.setText(columnbase.getVod_score());
        Columnbase finalColumnbase = columnbase;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(mContext, Videodetails.class);
                intent.putExtra("detailsName", finalColumnbase.getVod_name());
                intent.putExtra("detailsUrl", finalColumnbase.getVod_id());
                intent.putExtra("detailsImage", finalColumnbase.getVod_pic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent, bundle);
               }
        });

    }

/*
    public  void  run(Context context,ImageView imageView,String url, MultiTransformation<Bitmap> mation){
        // 因为runOnUiThread是Activity中的方法，Context是它的父类，所以要转换成Activity对象才能使用
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
*/



    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    //返回item个数
    @Override
    public int getItemCount() {
        return columbase.size();
    }

    public class ColumnViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_Leaderboard;
        private ImageView coulum_image_view;
        private RelativeLayout RelativeLayout_column_look;

      //  private ScrollView scrollView;
        private TextView column_name;
        private TextView text_video_type;
        private TextView text_video_Introduction;
        private TextView text_video_score;
ConstraintLayout constraintLayout;

        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
          //  scrollView= itemView.findViewById(R.id.ScrollView_text_video_Introduction);
            column_name =  itemView.findViewById(R.id.column_name);
            constraintLayout=itemView.findViewById(R.id.column_constraintLayout_adapter);
            text_video_type =  itemView.findViewById(R.id.text_video_type);
            imageView_Leaderboard =  itemView.findViewById(R.id.imageView_Leaderboard);
            coulum_image_view =  itemView.findViewById(R.id.coulum_image_view);
            text_video_Introduction =  itemView.findViewById(R.id.text_video_Introduction);
            text_video_score =  itemView.findViewById(R.id.text_video_score);
            text_video_score.setTextColor(itemView.getResources().getColor(R.color.title_text));
        }

    }
}
