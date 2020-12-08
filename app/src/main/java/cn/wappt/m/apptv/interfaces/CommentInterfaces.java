package cn.wappt.m.apptv.interfaces;

import cn.wappt.m.apptv.base.CommentLikeBase;
import cn.wappt.m.apptv.base.LoginReceive;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author: wsq
 * @date: 2020/11/30
 * Description:
 */
public interface CommentInterfaces {

    /**
     *评论加回复（游客）
     * @param comment_content  内容（必须包含中文）
     * @param comment_rid   关联数据id
     * @param comment_mid    模块id，1视频2文字3专题
     * @param comment_pid     数据上级id（评论回复要用到）注：就是评论那条数据id
     * @return
     */
    @Headers(
            "X-Requested-With: XMLHttpRequest")
    @POST("index.php/comment/saveData")
    Call<LoginReceive> commentPlusTourist(@Query("comment_pid") String  comment_pid, @Query("comment_content") String  comment_content
       , @Query("comment_mid") String comment_mid, @Query("comment_rid") String comment_rid);


    /**
     * 评论加回复（登录）
     * @param content 内容（必须包含中文）
     * @param rid 关联数据id
     * @param mid 模块id，1视频2文字3专题
     * @param pid 数据上级id（评论回复要用到）注：就是评论那条数据id
     * @param name 用户
     * @param pwd 密码
     * @return
     */
    @POST("plhf.php")
    Call<LoginReceive> commentPlus(@Query("content") String  content,
             @Query("rid") String  rid
                    , @Query("mid") String mid
                    ,@Query("pid") String pid
                    ,@Query("name") String name
                    ,@Query("pwd") String pwd);
    /**
     *   评论列表
     * @param s //数量
     * @param p //页码
     * @param rid  //关联数据id
     * @return
     */
    @POST("pllb.php")
    Call<ResponseBody> commentList
            (@Query("s") int s,
             @Query("p") int p, @Query("rid") String rid);




    /**
     *   评论举报
     * @param id  编号
     * @return
     */
    @Headers({
            "Accept: */*",
            "Accept-Encoding: gzip, deflate, br",
            "User-Agent: PostmanRuntime/7.26.8",
            "X-Requested-With: XMLHttpRequest"})
    @POST("index.php/comment/report.html")
    Call<ResponseBody> commentReport  (@Query("id") int id);


    /**
     *     评论赞，踩
     * @param mid	 值填4
     * @param id     要点赞那条数据的id
     * @param type   	值为up是赞，为down是踩
     * @return
     */
    @Headers({
            "Accept: application/json, text/javascript, */*; q=0.01",
            "Accept-Encoding: gzip, deflate, br",
            "Accept-Language: zh-CN,zh;q=0.9",
            "User-Agent: PostmanRuntime/7.26.8",
            "X-Requested-With: XMLHttpRequest"})
    @POST("index.php/ajax/digg.html")
    Call<ResponseBody> comment_like(@Body CommentLikeBase headerMap);
}
