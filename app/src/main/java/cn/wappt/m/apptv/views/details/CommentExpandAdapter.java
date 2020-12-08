package cn.wappt.m.apptv.views.details;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.CommentBase;
import cn.wappt.m.apptv.base.CommentLikeBase;
import cn.wappt.m.apptv.base.Commentback;
import cn.wappt.m.apptv.interfaces.CommentInterfaces;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.views.user.view.Round_head;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author: wsq
 * @date: 2020/12/1
 * Description:
 */
public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<CommentBase> commentBases;

    private Context context;
    boolean isLikezhen ;
    boolean isLikecai ;
    boolean isLikz;
    boolean isLikc;
    public CommentExpandAdapter(List<CommentBase> commentBases, Context context) {
        this.commentBases = commentBases;
        this.context = context;

    }

    //获取组数
    @Override
    public int getGroupCount() {

  return commentBases.size();
    }

    //获取子数
    @Override
    public int getChildrenCount(int groupPosition) {
        if(commentBases.get(groupPosition).getCommentBases() == null){
            return 0;
        }else {
            return commentBases.get(groupPosition).getCommentBases().size()>0 ? commentBases.get(groupPosition).getCommentBases().size():0;
        }
    }

    //获取组
    @Override
    public Object getGroup(int groupPosition) {
        return commentBases.get(groupPosition);
    }

    //得到孩子
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return commentBases.get(groupPosition).getCommentBases().get(childPosition);
    }

    //获取组ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取儿童编号
    @Override
    public long getChildId(int groupPosition, int childPosition) {
           return getCombinedChildId(groupPosition, childPosition);
    }
    //具有稳定的ID
    @Override
    public boolean hasStableIds() {
        return true;
    }




    //获取组视图 进行添加
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;
        if (convertView ==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_item_layout,viewGroup,false);
            groupHolder =new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder= (GroupHolder) convertView.getTag();
        }
         isLikezhen = false;
         isLikecai = false;
         isLikz=true;
         isLikc=true;
        groupHolder.tv_name.setText(commentBases.get(groupPosition).getComment_name());
        groupHolder.tv_time.setText(commentBases.get(groupPosition).getComment_time());
        groupHolder.tv_content.setText(commentBases.get(groupPosition).getComment_content());
        groupHolder.cai.setColorFilter(Color.parseColor("#aaaaaa"));
        groupHolder.zhen.setColorFilter(Color.parseColor("#aaaaaa"));
        int sum=0;
        if (commentBases.get(groupPosition).getComment_down()!=null &&commentBases.get(groupPosition).getComment_up()!=null){
            sum = Integer.parseInt(commentBases.get(groupPosition).getComment_up())-Integer.parseInt(commentBases.get(groupPosition).getComment_down());
        }
        groupHolder.text_like.setText(String.valueOf(sum));
        View finalConvertView = convertView;
        groupHolder.zhen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLikz){
                      if(isLikezhen){
                        isLikezhen = false;
                        isLikc=true;
                        groupHolder.zhen.setColorFilter(Color.parseColor("#aaaaaa"));
                    }else {
                        isLikc=false;
                        isLikezhen = true;
                        groupHolder.zhen.setColorFilter(Color.parseColor("#FF5C5C"));
                        comment_like(groupPosition,finalConvertView.getContext(),groupHolder.text_like,4,commentBases.get(groupPosition).getComment_id(),"up");
                    }
                }
                }
            });
        groupHolder.cai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLikc){
                if(isLikecai){
                    isLikecai = false;
                    isLikz=true;
                    groupHolder.cai.setColorFilter(Color.parseColor("#aaaaaa"));
                }else {
                   int  sum = Integer.parseInt(commentBases.get(groupPosition).getComment_up())-Integer.parseInt(commentBases.get(groupPosition).getComment_down());
                   if (sum>0){
                       isLikz=false;
                       isLikecai = true;
                       groupHolder.cai.setColorFilter(Color.parseColor("#FF5C5C"));
                       comment_like(groupPosition,context,groupHolder.text_like,4,commentBases.get(groupPosition).getComment_id(),"down");
                   }
                }
            }
            }
        });
        groupHolder.ConstraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog builder = new AlertDialog.Builder(context)
                        .create();
                builder.show();
                builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
                TextView msg =  builder.findViewById(R.id. browserecords_tv_msg);
                Button cancle =  builder.findViewById(R.id.browserecords_btn_cancle);
                Button sure =  builder.findViewById(R.id.browserecords_btn_sure);
               msg.setText("是否举报该用户的不良言论");
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
                        commentReport(context,commentBases.get(groupPosition).getComment_id());
                        //删除弹框
                        builder.dismiss();
                    }
                });
                return false;
            }
        });
        return convertView;
    }




    //获得子视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,parent, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        String replyUser = commentBases.get(groupPosition).getCommentBases().get(childPosition).getComment_name();
        if(!TextUtils.isEmpty(replyUser)){
            childHolder.tv_name.setText(replyUser + ":");
        }else {
            childHolder.tv_name.setText("游客"+":");
        }
        childHolder.tv_time.setText(commentBases.get(groupPosition).getCommentBases().get(childPosition).getComment_time());
        childHolder.tv_content.setText(commentBases.get(groupPosition).getCommentBases().get(childPosition).getComment_content());
        return convertView;
    }

    //是儿童可选的
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    private class GroupHolder{
        private Round_head logo;
        private TextView tv_name, tv_content, tv_time,text_like;
        private ImageView cai;
        private ImageView zhen;
        private ConstraintLayout ConstraintLayout;

        public GroupHolder(View view) {
            ConstraintLayout =view.findViewById(R.id.lineatlayet_comment_item);
            logo =view.findViewById(R.id.comment_item_logo);
            tv_content =  view.findViewById(R.id.comment_item_content);
            tv_name =  view.findViewById(R.id.comment_item_userName);
            tv_time =  view.findViewById(R.id.comment_item_time);
            cai =  view.findViewById(R.id.comment_item_cai);
            zhen=view.findViewById(R.id.comment_item_zhan);
            text_like=view.findViewById(R.id.text_comment_like);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content,tv_time;
        public ChildHolder(View view) {
            tv_name =  view.findViewById(R.id.reply_item_user);
            tv_content =  view.findViewById(R.id.reply_item_content);
            tv_time=view.findViewById(R.id.time_item_content);

        }
    }


    /**
     * func:评论成功后插入一条数据
     * @param commentDetailBean 新的评论数据
     */
    public void addTheCommentData(CommentBase commentDetailBean){
        if(commentDetailBean!=null){
            commentBases.add(commentDetailBean);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    /**
     * func:回复成功后插入一条数据
     * 新的回复数据
     */
    public void addTheReplyData(CommentBase base, int groupPosition){
        if(base!=null){
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+base.toString() );
            if(commentBases.get(groupPosition).getCommentBases()!= null ){
                commentBases.get(groupPosition).getCommentBases().add(base);
            }else {
                List<CommentBase> replyList = new ArrayList<>();
                replyList.add(base);
                commentBases.get(groupPosition).setCommentBases(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * func:添加和展示所有回复
     * @param listcomment 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<CommentBase> listcomment, int groupPosition){
        if(commentBases.get(groupPosition).getCommentBases()!= null ){
            commentBases.get(groupPosition).getCommentBases().clear();
            commentBases.get(groupPosition).getCommentBases().addAll(listcomment);
        }else {
            commentBases.get(groupPosition).setCommentBases(listcomment);
        }
        notifyDataSetChanged();
    }




    /**
     * 评论赞，踩
     *
     * @param mid  值填4
     * @param id   要点赞那条数据的id
     * @param type 值为up是赞，为down 是踩
     * @return
     */
    public void comment_like(int groupPosition,Context context,TextView textView, int mid, int id, String type) {

        //创建retrofit
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        CommentInterfaces request = retrofit.create(CommentInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.comment_like(new CommentLikeBase(mid,id,type));
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONObject jsonArray = new JSONObject(jsonStr);
                    int code = jsonArray.getInt("code");
                    String msg = jsonArray.getString("msg");
                    if (code==1){
                        Commentback base=new Commentback();
                        JSONObject data = jsonArray.getJSONObject("data");
                        int up = data.getInt("up");
                        int down = data.getInt("down");
                        base.setDown(down);
                        base.setUp(up);
                        commentBases.get(groupPosition).setComment_up(String.valueOf(up));
                        commentBases.get(groupPosition).setComment_down(String.valueOf(down));
                        System.out.println(base);
                        if (type.equals("up")){
                            Toast.makeText(context.getApplicationContext(),"点赞成功",Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(context.getApplicationContext(),"踩成功",Toast.LENGTH_SHORT).show();

                        }
                   } else {
                        Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (type.equals("up")){
                    Toast.makeText(context.getApplicationContext(),"点赞失败",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context.getApplicationContext(),"踩失败",Toast.LENGTH_SHORT).show();

                }  }
        });

    }



/*
    public  void  run(Context context,TextView imageView,Commentback commentback){
        // 因为runOnUiThread是Activity中的方法，Context是它的父类，所以要转换成Activity对象才能使用
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int sum=commentback.getUp()-commentback.getDown();
                String sumdo=String.valueOf(sum);
                imageView.setText(sumdo);
            }
        });
    }
*/



    /**
     *   评论举报
     * @param id  编号
     * @return
     */
    public void commentReport(Context context,  int id) {
        //创建retrofit
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        CommentInterfaces request = retrofit.create(CommentInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.commentReport(id);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONObject jsonArray = new JSONObject(jsonStr);
                    int code=jsonArray.getInt("code");
                    String msg=jsonArray.getString("msg");
                    System.out.println(code+"   "+msg);
                    Toast.makeText(context.getApplicationContext(), "举报成功", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(),"举报失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
