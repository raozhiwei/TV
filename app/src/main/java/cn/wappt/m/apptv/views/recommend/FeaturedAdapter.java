package cn.wappt.m.apptv.views.recommend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DataInforVidbase;
import cn.wappt.m.apptv.utils.GlideRoundTransform;
import cn.wappt.m.apptv.utils.RoundedCornersTransformation;
import cn.wappt.m.apptv.views.details.Videodetails;

/**
 * @author: wsq
 * @date: 2020/11/2
 * Description:
 */
public class FeaturedAdapter  extends RecyclerView.Adapter<FeaturedAdapter.FeaturedAdapterHolder>{
    Context mContext;
    List<DataInforVidbase> dataInforVidbases;
    private LayoutInflater inflater; //接受布局文件
    private int screenWidth;//屏幕宽度

    public FeaturedAdapter(Context mContext,List dataInfor,int screenWidth) {
        if (dataInforVidbases == null) {
            dataInforVidbases = new ArrayList<>();
        }
        this.mContext = mContext;
       this.dataInforVidbases=dataInfor;
        this.screenWidth=screenWidth;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }


    @NonNull
    @Override   //获取视图
    public FeaturedAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.item_feature_imageview, parent, false);
        FeaturedAdapterHolder holder= new FeaturedAdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedAdapterHolder holder, int position) {
        DataInforVidbase dataInforVidbase=new DataInforVidbase();
        dataInforVidbase=dataInforVidbases.get(position);
        ViewGroup.LayoutParams layoutParams =  holder.linearLayout.getLayoutParams();
        layoutParams.width =FeaturedActivty.screenWidth/3;
        layoutParams.height=FeaturedActivty.screenHeight/4+30;
        holder.linearLayout.setPadding(5,0,5,2);
        holder.linearLayout.setLayoutParams(layoutParams);
      //  holder.linearLayout.setPadding(5,5,5,0);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置图片的展示格式
        // 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
        //顶部左边圆角
        RoundedCornersTransformation transformation = new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
        //顶部右边圆角
        RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);
        RoundedCornersTransformation transformatbootonleft= new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT);
        RoundedCornersTransformation transformatbottom= new RoundedCornersTransformation
                (20, 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT);
        //组合各种Transformation,
        MultiTransformation<Bitmap> mation = new MultiTransformation<>
                //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                (new CenterCrop(), transformation, transformation1,transformatbootonleft,transformatbottom);
        //设置图片圆角角度
        run(mContext,holder.imageView,dataInforVidbase.getVod_pic(),mation);
/*        Glide.with(mContext)
                .load(dataInforVidbase.getVod_pic())
                .centerCrop()
                .placeholder(R.drawable.defaultpicture)
                .apply(RequestOptions.bitmapTransform(mation))
                .into(holder.imageView);*/

        if (dataInforVidbase.getVod_total()!=null){
            holder.feature_set_item.setText(dataInforVidbase.getVod_total());
        }else {
                holder.feature_set_item.setText(dataInforVidbase.getVod_score());
        }
        holder.feature_set_item.setSingleLine(true);
/*        holder.feature_set_item.setTextColor(holder.itemView.getResources().getColor(R.color.title_text));*/
        holder.feature_set_item.setTextSize(11);
        holder.feature_set_item.setGravity(Gravity.RIGHT);
        holder.feature_set_item.setTextColor(Color.WHITE);
        holder.feature_set_item.setBackgroundResource(R.color.textcolorstate);
        holder.textView.setText(dataInforVidbase.getVod_name());

        holder.textView.setSingleLine(true);
        // /设置点击监听器
        DataInforVidbase finalDataInforVidbase = dataInforVidbase;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(mContext, Videodetails.class);
                intent.putExtra("detailsName", finalDataInforVidbase.getVod_name());
                intent.putExtra("detailsUrl", finalDataInforVidbase.getVod_id());
                intent.putExtra("detailsImage", finalDataInforVidbase.getVod_pic());
                mContext.startActivity(intent, bundle);
                Toast.makeText(mContext, "position:" + "/voddetail/"+finalDataInforVidbase.getVod_id()+".html", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void  run(Context context,ImageView imageView,String url, MultiTransformation<Bitmap> mation ){
        // 因为runOnUiThread是Activity中的方法，Context是它的父类，所以要转换成Activity对象才能使用
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(1));
                //（使用Picasso或Glide）url或uri  获取图片后填充进Imageview
                Glide.with(context)
                        .load(url)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.defaultpicture)
                        .apply(RequestOptions.bitmapTransform(mation))
                        .into(imageView);
            }
        });
    }



    @Override
    public int getItemCount() {
        return dataInforVidbases.size();
    }


    public class FeaturedAdapterHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout linearLayout;
        private TextView textView;
        private  ImageView imageView;
        private  TextView feature_set_item;
        public FeaturedAdapterHolder(@NonNull View itemView) {
            super(itemView);
            feature_set_item=itemView.findViewById(R.id.feature_set_item);
            linearLayout =  itemView.findViewById(R.id.feature_item);
            textView=itemView.findViewById(R.id.feature_textview_item);
            imageView=itemView.findViewById(R.id.feature_imageview_item);
        }
    }
}
