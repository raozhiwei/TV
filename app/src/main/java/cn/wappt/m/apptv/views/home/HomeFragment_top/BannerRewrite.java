package cn.wappt.m.apptv.views.home.HomeFragment_top;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.DataBean;
import cn.wappt.m.apptv.views.details.Videodetails;

public class BannerRewrite  extends BannerAdapter<DataBean, BannerRewrite.BannerViewHolder> {
    Context context;
    public BannerRewrite(List<DataBean> mDatas , Context context) {
        //设置数据，也可以调用banner提供的方法
        super(mDatas);
        this.context=context;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        //注意布局文件，item布局文件要设置为match_parent，这个是viewpager2强制要求的
        //或者调用BannerUtils.getView(parent,R.layout.banner_image_title_num);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image_title_num, parent, false);
        return new BannerViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindView(BannerViewHolder holder, DataBean data, int position, int size) {
        run(holder.itemView.getContext(),holder.imageView,data.getBanner_style());
        holder.title.setText(data.getBanner_title());
        //可以在布局文件中自己实现指示器，亦可以使用banner提供的方法自定义指示器，目前样式较少，后面补充
        holder.numIndicator.setText((position + 1) + "/" + size);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("跳转");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, Videodetails.class);
                intent.putExtra("detailsName",data.getBanner_title().toString());
                intent.putExtra("detailsUrl",data.getBanner_url().toString());
                intent.putExtra("detailsImage", data.getBanner_style().toString());
                context.startActivity(intent,bundle);
            }
        });

    }
    public  void  run(Context context,ImageView imageView,String url){
        // 因为runOnUiThread是Activity中的方法，Context是它的父类，所以要转换成Activity对象才能使用
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(imageView);
            }
        });
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView numIndicator;

        public BannerViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            title = view.findViewById(R.id.bannerTitle);
            numIndicator = view.findViewById(R.id.numIndicator);
        }
    }
}
