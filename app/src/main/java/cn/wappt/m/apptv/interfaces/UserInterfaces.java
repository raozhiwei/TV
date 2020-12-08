package cn.wappt.m.apptv.interfaces;

import cn.wappt.m.apptv.base.UserUpdateReceive;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author: wsq
 * @date: 2020/10/27
 * Description:排行榜接口
 */
public interface UserInterfaces {

    /**获取用户信息
     * @return
     */
    @GET("/jbzl.php")
    Call<ResponseBody> selecyuser(@Query("name") String n, @Query("pwd") String p);
    /**修改用户
     * @return
     */
    @POST("/xgyhxx.php")
    Call<UserUpdateReceive> updateuser(@Query("name") String n, @Query("pwd") String p0, @Query("pwd1") String p1
             , @Query("pwd2") String p2
            , @Query("qq") String QQ
            , @Query("email") String pemail
            , @Query("phone") String phone
            , @Query("question") String question
            , @Query("answer") String answer);

}
